package com.cyq.android.ble.interfaces

import android.app.Application
import com.cyq.android.ble.bean.BleMessage

/**
 * @author：YangQi.Chen
 * @date：2024/1/16 12:06
 * @description：
 */
interface IBleManager {

    fun init(application: Application)

    /**
     * 扫描设备
     */
    fun startScanDevice()

    /**
     * 停止扫描设备
     */
    fun stopScanDevice()

    /**
     * 添加广播扫描结果回调
     */
    fun addScanDeviceListener(scanDeviceCallback: IScanDeviceCallback)

    /**
     * 移除广播扫描结果
     */
    fun removeScanDeviceListener(scanDeviceCallback: IScanDeviceCallback)

    /**
     * 移除所有广播扫描结果的监听
     */
    fun removeAllScanDeviceListener()

    /**
     * 发送蓝牙指令
     */
    fun sendMessage(message: BleMessage?, callback: ISendBleMessageCallback? = null)

    /**
     * 连接蓝牙设备
     * @param bleAddress: 蓝牙地址
     * @param timeout: 连接超时时间，不传默认10秒
     * @param connectCallback: 连接成功或者失败的回调
     */
    fun connect(
        bleAddress: String?,
        timeout: Int = 10_000,
        connectCallback: IConnectBleCallback? = null
    )

    /**
     * 移除
     */
    fun removeSendBleMessageCallback(callback: ISendBleMessageCallback)

    /**
     * 打开蓝牙开关
     */
    fun openBleSwitch(switchStateListener: IBleSwitchStateListener? = null)

    /**
     * 关闭蓝牙开关
     */
    fun closeBleSwitch(switchStateListener: IBleSwitchStateListener? = null)

    /**
     * 移除蓝牙开关状态的监听
     */
    fun removeBleSwitchStateListener(listener: IBleSwitchStateListener)

    /**
     * 移除所有蓝牙开关状态的监听
     */
    fun removeAllBleSwitchStateListener()

    /**
     * 移除相应蓝牙地址的所有监听
     */
    fun removeAllListener(bleAddress: String?)

    /**
     * 添加连接状态的监听
     */
    fun addConnectBleCallback(bleAddress: String?, callback: IConnectBleCallback? = null)

    /**
     * 移除连接状态的监听
     */
    fun removeConnectBleCallback(bleAddress: String?, callback: IConnectBleCallback? = null)

    /**
     * 添加连接状态的监听
     */
    fun addBleSwitchStateListener(callback: IBleSwitchStateListener? = null)
}