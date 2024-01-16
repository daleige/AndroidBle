package com.cyq.android.ble.interfaces

/**
 * @author：YangQi.Chen
 * @date：2024/1/16 14:50
 * @description：蓝牙开关状态监听
 */
interface IBleSwitchStateListener {

    /**
     * 开关状态改变
     * @param isSwitchOn: true:蓝牙开关打开，蓝牙开关关闭
     */
    fun onBleSwitchStateChange(isSwitchOn: Boolean)

}