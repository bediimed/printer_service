package com.codingdevs.thermal_printer.usb

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.util.Log

class UsbReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return
        Log.d("UsbReceiver", "Inside USB Broadcast action ${intent.action}")

        val action = intent.action
        if (UsbManager.ACTION_USB_DEVICE_ATTACHED == action) {
            val usbDevice: UsbDevice? =
                intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)
            if (usbDevice == null) return

            val permissionIntent = Intent("com.flutter_pos_printer.USB_PERMISSION").apply {
                setPackage(context.packageName)
            }

            val mPermissionIndent = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                PendingIntent.getBroadcast(
                    context, 0,
                    permissionIntent,
                    PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
            } else {
                PendingIntent.getBroadcast(
                    context, 0,
                    permissionIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            }
            val mUSBManager = context.getSystemService(Context.USB_SERVICE) as UsbManager?
            mUSBManager?.requestPermission(usbDevice, mPermissionIndent)
        }
    }
}
