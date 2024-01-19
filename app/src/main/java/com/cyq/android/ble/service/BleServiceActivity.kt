package com.cyq.android.ble.service

import android.Manifest
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Button
import androidx.annotation.RequiresApi
import com.cyq.android.ble.R

class BleServiceActivity : AppCompatActivity() {

    private var gattServiceConn: GattServiceConn? = null
    private val btnSendMessage: Button by lazy { findViewById(R.id.btnSendMessage) }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ble_service)

        btnSendMessage.setOnClickListener {
            gattServiceConn?.binding?.setMyCharacteristicValue("AAABBBCCC")
        }
        requestPermissions(arrayOf(Manifest.permission.BLUETOOTH_ADVERTISE),0)

        Handler(Looper.getMainLooper()).postDelayed({
            val gattService = GattService(this)
            gattService.enableBleServices(this)
        }, 3000)
    }

//    override fun onStart() {
//        super.onStart()
//        Log.d("BleServiceActivity","onStart--->>")
//        val latestGattServiceConn = GattServiceConn()
//        if (bindService(Intent(this, GattService::class.java), latestGattServiceConn, 0)) {
//            gattServiceConn = latestGattServiceConn
//        }
//    }
//
//    override fun onStop() {
//        super.onStop()
//        if (gattServiceConn != null) {
//            unbindService(gattServiceConn!!)
//            gattServiceConn = null
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        stopService(Intent(this, GattService::class.java))
//    }

    private class GattServiceConn : ServiceConnection {
        var binding: DeviceAPI? = null

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d("BleServiceActivity", "onServiceDisconnected--->>")
            binding = null
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d("BleServiceActivity", "onServiceConnected--->>")
            binding = service as? DeviceAPI
        }
    }
}