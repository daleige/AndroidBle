package com.cyq.android.ble.interfaces

/**
 * @author：YangQi.Chen
 * @date：2024/1/16 14:22
 * @description：发生蓝牙指令结果的回调
 */
interface ISendBleMessageCallback {

    fun onSuccess(result:ByteArray)

    fun onFail(errorCode:Int,errorMessage:String?)
}