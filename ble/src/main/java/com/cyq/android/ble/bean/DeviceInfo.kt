package com.cyq.android.ble.bean

/**
 * @author：YangQi.Chen
 * @date：2024/1/16 12:14
 * @description：
 */
class DeviceInfo {
    // 蓝牙地址
    var bleAddress: String = ""

    // 蓝牙名称
    var bleName: String = ""

    // 广播信号强度
    var rssi: Int = -100

    //广播内容
    var broadcastByteArray: ByteArray? = null

    var serviceUuids: ArrayList<String> = arrayListOf()

    // 扫描到广播时的时间戳
    var scanTimeStamp: Long = 0L

}