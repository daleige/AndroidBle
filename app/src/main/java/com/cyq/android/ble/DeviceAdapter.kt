package com.cyq.android.ble

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.cyq.android.ble.bean.DeviceInfo

/**
 * @author：YangQi.Chen
 * @date：2024/1/17 09:50
 * @description：
 */
class DeviceAdapter : BaseQuickAdapter<DeviceInfo, QuickViewHolder>() {

    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: DeviceInfo?) {
        holder.getView<TextView>(R.id.tvDeviceInfo).text = item.toString()
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.item_device_layout, parent)
    }
}