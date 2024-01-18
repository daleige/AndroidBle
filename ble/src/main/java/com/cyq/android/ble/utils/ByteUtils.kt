package com.cyq.android.ble.utils

/**
 * @author：YangQi.Chen
 * @date：2024/1/17 10:52
 * @description：
 */
object ByteUtils {

    fun bytesToHexString(src: ByteArray?): String? {
        val stringBuilder = StringBuilder()
        if (src == null || src.isEmpty()) {
            return null
        }
        for (aSrc in src) {
            val v: Int = (aSrc.toInt() and 0xFF)
            stringBuilder.append("0x")
            val hv = Integer.toHexString(v)
            if (hv.length < 2) {
                stringBuilder.append(0)
            }
            stringBuilder.append(hv)
            stringBuilder.append(" ")
        }
        return stringBuilder.toString()
    }
}