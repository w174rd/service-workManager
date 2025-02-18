package com.w174rd.serviceworkmanager.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.w174rd.serviceworkmanager.R
import com.w174rd.serviceworkmanager.utils.Attributes

class ForegroundService : Service() {

    override fun onCreate() {
        super.onCreate()
        startForeground(1, createNotification()) // Jalankan service dengan notifikasi
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Simulasi pekerjaan yang berjalan terus menerus
        Thread {
            while (true) {
                Log.d("ForegroundService", "Service berjalan...")
                Thread.sleep(3000) // Simulasi pekerjaan
            }
        }.start()

        return START_STICKY // Agar service tetap hidup jika sistem mematikannya
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MyForegroundService", "Service dihentikan!")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null // Tidak perlu bind ke activity
    }

    private fun createNotification(): Notification {
        val channelId = Attributes.pushNotif.channelForegroundService
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Foreground Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Foreground Service")
            .setContentText("Service berjalan di latar belakang")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true) // Tidak bisa di-swipe
            .build()
    }
}