package com.cyq.android.ble.utils

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import android.text.TextUtils
import com.cyq.android.ble.BleLog
import com.cyq.android.ble.bean.DeviceInfo
import java.util.concurrent.ConcurrentHashMap

/**
 * @author：YangQi.Chen
 * @date：2024/1/16 17:11
 * @description：广播解析器
 */
object BroadCastParseManager {

    // 缓存扫描到的蓝牙设备信息
    private val mScanDeviceInfoMap = ConcurrentHashMap<String, DeviceInfo>()

    // 记录上一次回调蓝牙扫描结果的时间，间隔时间内再回调通知业务层
    private var mNotifyScanTimer = 0L

    // 回调给业务层设备扫描结果的时间间隔阈值，单位毫秒
    private const val threshold = 500

    private var mINotifyCallback: INotifyScanResult? = null

    // 缓存扫描到的蓝牙设备原始信息
    private var mBluetoothDeviceMap = ConcurrentHashMap<String, BluetoothDevice>()

    /**
     * 解析蓝牙广播
     */
    fun parse(scanResult: ScanResult): DeviceInfo? {
        try {
            val deviceInfo = DeviceInfo()
            deviceInfo.bleAddress = scanResult.device.address
            deviceInfo.bleName = scanResult.device.name ?: ""
            if (!deviceInfo.bleName.contains("Govee")) {
                return null
            }
            deviceInfo.rssi = scanResult.rssi
            deviceInfo.broadcastByteArray = scanResult.scanRecord?.bytes
            scanResult.scanRecord?.serviceUuids?.forEach {
                deviceInfo.serviceUuids.add(it.toString())
            }
            deviceInfo.scanTimeStamp = System.currentTimeMillis()
            mScanDeviceInfoMap[deviceInfo.bleAddress] = deviceInfo
            mBluetoothDeviceMap[deviceInfo.bleAddress] = scanResult.device
            notifyScanCallback()
            return deviceInfo
        } catch (e: Exception) {
            BleLog.e("parse broadcast error:${e.message ?: "unknown"}")
            return null
        }
    }

    fun getBluetoothDevice(bleAddress: String?): BluetoothDevice? {
        if (TextUtils.isEmpty(bleAddress)){
            return null
        }
        return mBluetoothDeviceMap[bleAddress]
    }

    private fun notifyScanCallback() {
        if (System.currentTimeMillis() - mNotifyScanTimer < threshold) {
            return
        }
        mNotifyScanTimer = System.currentTimeMillis()
        // 移除掉30s之前扫描到的设备
        mScanDeviceInfoMap.filter { (System.currentTimeMillis() - it.value.scanTimeStamp) > 30_000 }
        // TODO 开一个单独的线程存储扫描到的设备到数据库中
        mINotifyCallback?.notifyScanResult(mScanDeviceInfoMap)
    }

    fun addNotifyCallback(callback: INotifyScanResult) {
        if (mINotifyCallback == null) {
            mINotifyCallback = callback
        }
    }

    fun removeNotifyCallback() {
        mINotifyCallback = null
    }

    interface INotifyScanResult {
        fun notifyScanResult(mScanDeviceInfoMap: ConcurrentHashMap<String, DeviceInfo>)
    }
}