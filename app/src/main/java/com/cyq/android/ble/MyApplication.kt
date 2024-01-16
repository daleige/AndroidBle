package com.cyq.android.ble

import android.app.Application

/**
 * @author：YangQi.Chen
 * @date：2024/1/16 15:47
 * @description：
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        BleManager.init(this)
    }
}