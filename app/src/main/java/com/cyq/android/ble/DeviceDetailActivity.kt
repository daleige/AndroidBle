package com.cyq.android.ble

import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.cyq.android.ble.bean.DeviceInfo
import com.cyq.android.ble.interfaces.IConnectBleCallback

class DeviceDetailActivity : AppCompatActivity() {
    private var mDeviceInfo: DeviceInfo? = null
    private val tvConnectState:TextView by lazy { findViewById(R.id.tvConnectState) }
    private val btnConnectDevice:TextView by lazy { findViewById(R.id.btnConnectDevice) }

    companion object {
        private const val DEVICE_INFO_KEY = "device_Info_key"

        fun start(activity: Activity, deviceInfo: DeviceInfo) {
            val bundle = Bundle()
            bundle.putSerializable(DEVICE_INFO_KEY, deviceInfo)
            val intent = Intent(activity, DeviceDetailActivity::class.java)
            intent.putExtras(bundle)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_detail)
        initView()
        initData()
    }

    private fun initView() {

    }

    private fun initData() {
        intent?.extras?.let {
            mDeviceInfo = it.getSerializable(DEVICE_INFO_KEY) as DeviceInfo
        }
        if (mDeviceInfo == null) {
            Toast.makeText(this, "deviceInfo is null!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        initDeviceConnect()
    }

    private val mConnectBleCallback = object : IConnectBleCallback {
        override fun onStartConnect(device: DeviceInfo) {
            TODO("Not yet implemented")
        }

        override fun connectSuccess(device: DeviceInfo) {
            TODO("Not yet implemented")
        }

        override fun connectFail(errorCode: Int, errorMessage: String?) {
            TODO("Not yet implemented")
        }

    }

    private fun initDeviceConnect() {
        mDeviceInfo?.let { deviceInfo ->
            BleManager.connect(
                bleAddress = deviceInfo.bleAddress,
                connectCallback = mConnectBleCallback
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        BleManager.removeConnectBleCallback(mDeviceInfo?.bleAddress, mConnectBleCallback)
    }
}