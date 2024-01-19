package com.cyq.android.ble.service

import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.os.ParcelUuid
import android.util.Log

/**
 * @author：YangQi.Chen
 * @date：2024/1/19 10:07
 * @description：
 */
object BleAdvertiser {

    private const val TAG = "BleAdvertiser"

    class Callback : AdvertiseCallback() {
        override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
            super.onStartSuccess(settingsInEffect)
            Log.i(TAG, "ble advertiser start.")
        }

        override fun onStartFailure(errorCode: Int) {
            super.onStartFailure(errorCode)
            Log.i(TAG, "ble advertiser Fail:$errorCode")
        }
    }

    fun settings():AdvertiseSettings{
        return AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_POWER)
            .setConnectable(true)
            .setTimeout(0)
            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_LOW)
            .build()
    }

    fun advertiseData():AdvertiseData{
        return AdvertiseData.Builder()
            .setIncludeDeviceName(true) // Including it will blow the length
            .setIncludeTxPowerLevel(false)
            .addServiceUuid(ParcelUuid.fromString("80323644-3537-4F0B-A53B-CF494ECEAAB3"))
            .build()
    }
}