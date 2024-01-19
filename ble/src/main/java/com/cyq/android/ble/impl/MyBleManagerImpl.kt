package com.cyq.android.ble.impl

import android.app.Application
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.ParcelUuid
import android.util.Log
import com.cyq.android.ble.BleLog
import com.cyq.android.ble.MyBleManager
import com.cyq.android.ble.bean.BleMessage
import com.cyq.android.ble.bean.DeviceInfo
import com.cyq.android.ble.interfaces.IBleManager
import com.cyq.android.ble.interfaces.IBleSwitchStateListener
import com.cyq.android.ble.interfaces.IConnectBleCallback
import com.cyq.android.ble.interfaces.IScanDeviceCallback
import com.cyq.android.ble.interfaces.ISendBleMessageCallback
import com.cyq.android.ble.utils.BroadCastParseManager
import no.nordicsemi.android.ble.BleManager
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArraySet

/**
 * @author：YangQi.Chen
 * @date：2024/1/16 15:09
 * @description：
 */
class MyBleManagerImpl : IBleManager {
    private var mApplication: Application? = null
    private var mBluetoothManager: BluetoothManager? = null
    private val mScanDeviceCallbackSet = CopyOnWriteArraySet<IScanDeviceCallback>()
    private lateinit var mBleManager: MyBleManager

    companion object {
        private const val TAG = "MyBleManagerImpl"
    }

    private val mScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            result?.let {
                BleLog.d("callbackType:$callbackType,bleName:${it.device.name}  bleAddress:${it.device.address}")
                BroadCastParseManager.parse(result)
            }
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            super.onBatchScanResults(results)
            results?.let {
                BleLog.d("results size:${results.size}")
            }
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            BleLog.d("onScanFailed------->errorCode:$errorCode")
        }
    }

    /**
     * 蓝牙广播扫描结果
     */
    private val mINotifyCallback = object : BroadCastParseManager.INotifyScanResult {
        override fun notifyScanResult(mScanDeviceInfoMap: ConcurrentHashMap<String, DeviceInfo>) {
            // 回调扫描到的设备给业务侧
            mScanDeviceCallbackSet.forEach {
                it.onScanDevice(mScanDeviceInfoMap.values.toMutableList())
            }
        }
    }

    override fun init(application: Application) {
        mApplication = application
        mBluetoothManager =
            application.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager?
        mBleManager = MyBleManager(application.applicationContext)
    }

    override fun startScanDevice() {
        BleLog.i("------->startScanDevice")
        // 设置扫描过滤条件,例如设置蓝牙名称，Mac地址，服务的uuid等
        val scanFilters = arrayListOf<ScanFilter>()
        val scanFilter = ScanFilter.Builder()
            .setServiceUuid(ParcelUuid.fromString("80323644-3537-4F0B-A53B-CF494ECEAAB3"))
            .build()
        scanFilters.add(scanFilter)

        // 设置扫描模式
        val settings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

        mBluetoothManager?.adapter?.bluetoothLeScanner?.startScan(
            scanFilters,
            settings,
            mScanCallback
        )
    }

    override fun stopScanDevice() {
        BleLog.i("------->stopScanDevice")
        mBluetoothManager?.adapter?.bluetoothLeScanner?.stopScan(mScanCallback)
    }

    override fun addScanDeviceListener(scanDeviceCallback: IScanDeviceCallback) {
        mScanDeviceCallbackSet.add(scanDeviceCallback)
        BroadCastParseManager.addNotifyCallback(mINotifyCallback)
    }

    override fun removeScanDeviceListener(scanDeviceCallback: IScanDeviceCallback) {
        mScanDeviceCallbackSet.remove(scanDeviceCallback)
        if (mScanDeviceCallbackSet.isEmpty()) {
            BroadCastParseManager.removeNotifyCallback()
        }
    }

    override fun removeAllScanDeviceListener() {
    }

    override fun sendMessage(message: BleMessage?, callback: ISendBleMessageCallback?) {
    }

    override fun connect(
        bleAddress: String?,
        timeout: Int?,
        connectCallback: IConnectBleCallback?
    ) {
        val bluetoothDevice = BroadCastParseManager.getBluetoothDevice(bleAddress)
        if (bluetoothDevice == null) {
            //TODO 没有扫描到设备，尝试扫描设备并连接
            Log.d(TAG, "--->去扫描设备")
            return
        }
        mBleManager.connect(bluetoothDevice)
    }

    override fun removeSendBleMessageCallback(callback: ISendBleMessageCallback) {
    }

    override fun openBleSwitch(switchStateListener: IBleSwitchStateListener?) {
    }

    override fun closeBleSwitch(switchStateListener: IBleSwitchStateListener?) {
    }

    override fun removeBleSwitchStateListener(listener: IBleSwitchStateListener) {
    }

    override fun removeAllBleSwitchStateListener() {
    }

    override fun removeAllListener(bleAddress: String?) {
    }

    override fun addConnectBleCallback(bleAddress: String?, callback: IConnectBleCallback?) {
    }

    override fun removeConnectBleCallback(bleAddress: String?, callback: IConnectBleCallback?) {
    }

    override fun addBleSwitchStateListener(callback: IBleSwitchStateListener?) {
    }
}