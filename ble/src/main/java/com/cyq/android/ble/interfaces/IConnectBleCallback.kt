package com.cyq.android.ble.interfaces

import android.bluetooth.BluetoothDevice
import com.cyq.android.ble.bean.DeviceInfo

/**
 * @author：YangQi.Chen
 * @date：2024/1/16 14:30
 * @description：连接蓝牙设备结果的回调
 */
interface IConnectBleCallback {

    fun onStartConnect(device: DeviceInfo)

    /**
     * 连接成功
     */
    fun connectSuccess(device: DeviceInfo)

    /**
     * 连接失败
     */
    fun connectFail(errorCode:Int,errorMessage:String?)
}