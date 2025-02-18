package com.w174rd.serviceworkmanager.services

import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.w174rd.serviceworkmanager.R
import com.w174rd.serviceworkmanager.utils.Attributes
import kotlinx.coroutines.delay

class ForegroundWorkerService(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        setForeground(createForegroundInfo())

        Log.d("ForegroundWorker", "Foreground task dimulai")

        // Simulasi pekerjaan yang berjalan di foreground
        for (i in 1..5) {
            Log.d("ForegroundWorker", "Proses $i")
            delay(1000)
        }

        Log.d("ForegroundWorker", "Foreground task selesai")
        return Result.success()
    }

    private fun createForegroundInfo(): ForegroundInfo {
        val notification = NotificationCompat.Builder(applicationContext, Attributes.pushNotif.channelForegroundWorkManager)
            .setContentTitle("Foreground Worker")
            .setContentText("Sedang berjalan...")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .build()

        return ForegroundInfo(2, notification)
    }
}