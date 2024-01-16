package com.cyq.android.ble

import android.util.Log

/**
 * @author：YangQi.Chen
 * @date：2024/1/16 16:41
 * @description：
 */
object BleLog {
    private const val TAG = "BleLog"

    fun i(message: String?) {
        i(TAG, message)
    }

    fun i(tag: String, message: String?) {
        message?.let {
            Log.i(tag, it)
        }
    }

    fun d(message: String?) {
        i(TAG, message)
    }

    fun d(tag: String, message: String?) {
        message?.let {
            Log.d(tag, it)
        }
    }

    fun e(message: String?) {
        i(TAG, message)
    }

    fun e(tag: String, message: String?) {
        message?.let {
            Log.e(tag, it)
        }
    }
}