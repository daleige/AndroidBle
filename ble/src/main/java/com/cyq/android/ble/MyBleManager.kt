package com.cyq.android.ble

import android.bluetooth.BluetoothGatt
import android.content.Context
import no.nordicsemi.android.ble.BleManager

/**
 * @author：YangQi.Chen
 * @date：2024/1/17 11:50
 * @description：
 */
class MyBleManager(context: Context):BleManager(context) {

    override fun getGattCallback(): BleManagerGattCallback {
        return MyBleManagerGattCallback()
    }

    private class MyBleManagerGattCallback :BleManagerGattCallback(){

        override fun initialize() {

        }

        override fun isRequiredServiceSupported(gatt: BluetoothGatt): Boolean {
            return true
        }

        override fun onServicesInvalidated() {

        }
    }
}