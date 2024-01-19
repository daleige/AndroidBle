package com.cyq.android.ble.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.chad.library.adapter4.BuildConfig
import com.cyq.android.ble.R
import no.nordicsemi.android.ble.BleManager
import no.nordicsemi.android.ble.BleServerManager
import no.nordicsemi.android.ble.observer.ServerObserver
import java.nio.charset.StandardCharsets
import java.util.Collections
import java.util.UUID

/**
 * @author：YangQi.Chen
 * @date：2024/1/19 10:23
 * @description：蓝牙GATT服务
 */
class GattService(val context: Context) {

    companion object {
        private const val TAG = "GattService"
    }

    private var serverManager: ServerManager? = null
    private lateinit var bluetoothObserver: BroadcastReceiver
    private var bleAdvertiseCallback: BleAdvertiser.Callback? = null

//    override fun onCreate() {
//        super.onCreate()
//        Log.d(TAG,"onCreate----->>>")
//        val notificationChannel = NotificationChannel(
//            GattService::class.java.simpleName,
//           "gatt_service_1",
//            NotificationManager.IMPORTANCE_DEFAULT
//        )
//        val notificationService =
//            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationService.createNotificationChannel(notificationChannel)
//
//        val notification = NotificationCompat.Builder(this, GattService::class.java.simpleName)
//            .setSmallIcon(R.mipmap.ic_launcher)
//            .setContentTitle("gatt service")
//            .setContentText("gatt_service_running_notification")
//            .setAutoCancel(true)
//
//        startForeground(1, notification.build())
//
//        bluetoothObserver = object : BroadcastReceiver() {
//            override fun onReceive(context: Context?, intent: Intent?) {
//                when (intent?.action) {
//                    BluetoothAdapter.ACTION_STATE_CHANGED -> {
//                        val bluetoothState = intent.getIntExtra(
//                            BluetoothAdapter.EXTRA_STATE, -1
//                        )
//                        when (bluetoothState) {
//                            BluetoothAdapter.STATE_ON -> enableBleServices()
//                            BluetoothAdapter.STATE_OFF -> disableBleServices()
//                        }
//                    }
//                }
//            }
//        }
//        registerReceiver(bluetoothObserver, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))
//        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
//        if (bluetoothManager.adapter?.isEnabled == true) {
//            enableBleServices()
//        }
//    }

//    override fun onDestroy() {
//        super.onDestroy()
//        disableBleServices()
//    }

    /**
     * 开启服务
     */
    @SuppressLint("MissingPermission")
    fun enableBleServices(context: Context) {
        serverManager = ServerManager(context)
        serverManager?.open()

        bleAdvertiseCallback = BleAdvertiser.Callback()

        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter.bluetoothLeAdvertiser?.startAdvertising(
            BleAdvertiser.settings(),
            BleAdvertiser.advertiseData(),
            bleAdvertiseCallback!!
        )
    }

    /**
     * 关闭服务
     */
    @SuppressLint("MissingPermission")
    fun disableBleServices(context: Context) {
        bleAdvertiseCallback?.let {
            val bluetoothManager =
                context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            bluetoothManager.adapter.bluetoothLeAdvertiser?.stopAdvertising(it)
            bleAdvertiseCallback = null
        }
        serverManager?.close()
        serverManager = null
    }

//    override fun onBind(intent: Intent?): IBinder? {
//        return DataPlane()
//    }

//    private inner class DataPlane : Binder(), DeviceAPI {
//
//        override fun setMyCharacteristicValue(value: String) {
//            serverManager?.setMyCharacteristicValue(value)
//        }
//    }

    private class ServerManager(val context: Context) : BleServerManager(context), ServerObserver,
        DeviceAPI {

        companion object {
            private val CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID =
                UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
        }

        private val mGattCharacteristic = sharedCharacteristic(
            ServiceConstant.MY_CHARACTERISTIC_UUID,
            BluetoothGattCharacteristic.PROPERTY_READ or BluetoothGattCharacteristic.PROPERTY_NOTIFY,
            BluetoothGattCharacteristic.PERMISSION_READ_ENCRYPTED_MITM,
            descriptor(
                CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID,
                BluetoothGattDescriptor.PERMISSION_READ_ENCRYPTED_MITM or BluetoothGattDescriptor.PERMISSION_WRITE_ENCRYPTED_MITM,
                byteArrayOf(0, 0)
            ),
            description("A characteristic to be read", false) // descriptors
        )

        private val mGattService = service(
            ServiceConstant.MY_SERVICE_UUID,
            mGattCharacteristic
        )

        private val mGattServices = Collections.singletonList(mGattService)

        private val serverConnections = mutableMapOf<String, ServerConnection>()

        override fun log(priority: Int, message: String) {
            if (BuildConfig.DEBUG || priority == Log.ERROR) {
                Log.println(priority, TAG, message)
            }
        }

        override fun initializeServer(): MutableList<BluetoothGattService> {
            setServerObserver(this)
            return mGattServices
        }

        override fun onServerReady() {
            log(Log.INFO, "Gatt server ready")
        }

        override fun onDeviceConnectedToServer(device: BluetoothDevice) {
            log(Log.DEBUG, "Device connected ${device.address}")
            serverConnections[device.address] = ServerConnection().apply {
                useServer(this@ServerManager)
                connect(device).enqueue()
            }
        }

        override fun onDeviceDisconnectedFromServer(device: BluetoothDevice) {
            log(Log.DEBUG, "Device disconnected ${device.address}")
            serverConnections.remove(device.address)
        }

        override fun setMyCharacteristicValue(value: String) {
            val bytes = value.toByteArray(StandardCharsets.UTF_8)
            serverConnections.values.forEach { serverConnection ->
                // 发送数据
                serverConnection.sendNotificationForMyGattCharacteristic(bytes)
            }
        }

        inner class ServerConnection : BleManager(context) {
            fun sendNotificationForMyGattCharacteristic(value: ByteArray) {
                sendNotification(mGattCharacteristic, value).enqueue()
            }

            override fun log(priority: Int, message: String) {
                this@ServerManager.log(priority, message)
            }

            override fun isRequiredServiceSupported(gatt: BluetoothGatt): Boolean {
                return true
            }

            override fun onServicesInvalidated() {

            }
        }
    }
}