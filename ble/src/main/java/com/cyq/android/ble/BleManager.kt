package com.cyq.android.ble

import android.app.Application
import com.cyq.android.ble.bean.BleMessage
import com.cyq.android.ble.impl.MyBleManagerImpl
import com.cyq.android.ble.interfaces.IBleManager
import com.cyq.android.ble.interfaces.IBleSwitchStateListener
import com.cyq.android.ble.interfaces.IConnectBleCallback
import com.cyq.android.ble.interfaces.IScanDeviceCallback
import com.cyq.android.ble.interfaces.ISendBleMessageCallback

/**
 * @author：YangQi.Chen
 * @date：2024/1/16 11:44
 * @description：蓝牙管理类
 */
object BleManager : IBleManager {
    private lateinit var mIBleManagerImpl: IBleManager

    override fun init(application: Application) {
        mIBleManagerImpl = MyBleManagerImpl()
        mIBleManagerImpl.init(application)
    }

    override fun startScanDevice() {
        mIBleManagerImpl.startScanDevice()
    }

    override fun stopScanDevice() {
        mIBleManagerImpl.stopScanDevice()
    }

    override fun addScanDeviceListener(scanDeviceCallback: IScanDeviceCallback) {
        mIBleManagerImpl.addScanDeviceListener(scanDeviceCallback)
    }

    override fun removeScanDeviceListener(scanDeviceCallback: IScanDeviceCallback) {
        mIBleManagerImpl.removeScanDeviceListener(scanDeviceCallback)
    }

    override fun removeAllScanDeviceListener() {
        mIBleManagerImpl.removeAllScanDeviceListener()
    }

    override fun sendMessage(message: BleMessage?, callback: ISendBleMessageCallback?) {
        mIBleManagerImpl.sendMessage(message, callback)
    }

    override fun connect(
        bleAddress: String?,
        timeout: Int?,
        connectCallback: IConnectBleCallback?
    ) {
        mIBleManagerImpl.connect(bleAddress, timeout, connectCallback)
    }

    override fun removeSendBleMessageCallback(callback: ISendBleMessageCallback) {
        mIBleManagerImpl.removeSendBleMessageCallback(callback)
    }

    override fun openBleSwitch(switchStateListener: IBleSwitchStateListener?) {
        mIBleManagerImpl.openBleSwitch(switchStateListener)
    }

    override fun closeBleSwitch(switchStateListener: IBleSwitchStateListener?) {
        mIBleManagerImpl.closeBleSwitch(switchStateListener)
    }

    override fun removeBleSwitchStateListener(listener: IBleSwitchStateListener) {
        mIBleManagerImpl.removeBleSwitchStateListener(listener)
    }

    override fun removeAllBleSwitchStateListener() {
        mIBleManagerImpl.removeAllBleSwitchStateListener()
    }

    override fun removeAllListener(bleAddress: String?) {
        mIBleManagerImpl.removeAllListener(bleAddress)
    }

    override fun addConnectBleCallback(bleAddress: String?, callback: IConnectBleCallback?) {
        mIBleManagerImpl.addConnectBleCallback(bleAddress, callback)
    }

    override fun removeConnectBleCallback(bleAddress: String?, callback: IConnectBleCallback?) {
        mIBleManagerImpl.removeConnectBleCallback(bleAddress, callback)
    }

    override fun addBleSwitchStateListener(callback: IBleSwitchStateListener?) {
        mIBleManagerImpl.addBleSwitchStateListener(callback)
    }
}