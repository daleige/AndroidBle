package com.cyq.android.ble

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.cyq.android.ble.bean.DeviceInfo
import com.cyq.android.ble.interfaces.IScanDeviceCallback
import com.cyq.android.ble.service.BleServiceActivity

class MainActivity : AppCompatActivity() {
    private val btnStartScan: Button by lazy { findViewById(R.id.btnStartScan) }
    private val btnStopScan: Button by lazy { findViewById(R.id.btnStopScan) }
    private val mDeviceRecyclerView: RecyclerView by lazy { findViewById(R.id.recyclerView) }
    private val mDeviceAdapter: DeviceAdapter by lazy { DeviceAdapter() }
    private val btnToService: Button by lazy { findViewById(R.id.btnToService) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initObserver()
        initPermission()
    }

    private fun initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (checkSelfPermission(Manifest.permission.BLUETOOTH_ADVERTISE) == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "已有蓝牙权限", Toast.LENGTH_SHORT).show()
            } else {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.BLUETOOTH_ADVERTISE,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ), 0
                )
            }
        }
    }

    private fun initView() {
        btnStartScan.setOnClickListener {
            BleManager.startScanDevice()
        }
        btnStopScan.setOnClickListener {
            BleManager.stopScanDevice()
        }
        mDeviceRecyclerView.adapter = mDeviceAdapter
        mDeviceRecyclerView.layoutManager = LinearLayoutManager(this)
        mDeviceAdapter.setOnItemClickListener { adapter, view, position ->

        }
        btnToService.setOnClickListener {
            startActivity(Intent(this, BleServiceActivity::class.java))
        }
    }

    private fun initObserver() {
        BleManager.addScanDeviceListener(mScanDeviceListener)

    }

    private val mScanDeviceListener = object : IScanDeviceCallback {
        override fun onScanDevice(deviceInfoList: MutableList<DeviceInfo>) {
            Log.d("scan_result", deviceInfoList.map { it.bleName }.toString())
            mDeviceAdapter.submitList(deviceInfoList)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        BleManager.removeScanDeviceListener(mScanDeviceListener)
    }
}