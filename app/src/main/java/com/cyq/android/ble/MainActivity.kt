package com.cyq.android.ble

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.cyq.android.ble.bean.DeviceInfo
import com.cyq.android.ble.interfaces.IScanDeviceCallback

class MainActivity : AppCompatActivity() {
    private val btnStartScan: Button by lazy { findViewById(R.id.btnStartScan) }
    private val btnStopScan: Button by lazy { findViewById(R.id.btnStopScan) }
    private val mDeviceRecyclerView: RecyclerView by lazy { findViewById(R.id.recyclerView) }
    private val mDeviceAdapter: DeviceAdapter by lazy { DeviceAdapter() }

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
        mDeviceRecyclerView.adapter = mDeviceAdapter
        mDeviceRecyclerView.layoutManager = LinearLayoutManager(this)
        mDeviceAdapter.setOnItemClickListener{ adapter, view, position ->

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