package com.cyq.android.ble.interfaces

import com.cyq.android.ble.bean.DeviceInfo

/**
 * @author：YangQi.Chen
 * @date：2024/1/16 12:17
 * @description：扫描蓝牙设备信息回调
 */
interface IScanDeviceCallback {

    fun onScanDevice(deviceInfoList: MutableList<DeviceInfo>)

}