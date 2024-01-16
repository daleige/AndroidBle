package com.cyq.android.ble

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.cyq.android.ble.bean.DeviceInfo
import com.cyq.android.ble.interfaces.IScanDeviceCallback

class MainActivity : AppCompatActivity() {
    private val btnStartScan: Button by lazy { findViewById(R.id.btnStartScan) }
    private val btnStopScan: Button by lazy { findViewById(R.id.btnStopScan) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initObserver()
    }

    private fun initView() {
        btnStartScan.setOnClickListener {
            BleManager.startScanDevice()
        }
        btnStopScan.setOnClickListener {
            BleManager.stopScanDevice()
        }
    }

    private fun initObserver() {
        BleManager.addScanDeviceListener(mScanDeviceListener)

    }

    private val mScanDeviceListener = object : IScanDeviceCallback {
        override fun onScanDevice(deviceInfo: MutableList<DeviceInfo>) {
            Log.d("scan_result", deviceInfo.map { it.bleName }.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        BleManager.removeScanDeviceListener(mScanDeviceListener)
    }
}