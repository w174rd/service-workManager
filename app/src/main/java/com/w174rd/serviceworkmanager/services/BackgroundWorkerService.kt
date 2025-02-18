package com.w174rd.serviceworkmanager.services

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.w174rd.serviceworkmanager.R
import com.w174rd.serviceworkmanager.utils.Attributes

class BackgroundWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        Log.d("BackgroundWorker", "Task dimulai di background")

        // Tampilkan notifikasi saat worker berjalan
        showNotification("Worker Berjalan", "Sinkronisasi data sedang berlangsung...")

        // Simulasi pekerjaan
        Thread.sleep(3000) // Jangan gunakan di produksi

        Log.d("BackgroundWorker", "Task selesai di background")

        // Tampilkan notifikasi selesai
        showNotification("Worker Selesai", "Sinkronisasi data selesai!")

        return Result.success()
    }

    private fun showNotification(title: String, message: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(applicationContext, Attributes.pushNotif.channelWorkManager)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
}