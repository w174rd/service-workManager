package com.w174rd.serviceworkmanager.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.w174rd.serviceworkmanager.services.BackgroundWorkerService
import java.util.concurrent.TimeUnit

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {

            // Menjadwalkan ulang WorkManager
//            val constraints = Constraints.Builder()
//                .setRequiredNetworkType(NetworkType.CONNECTED)
//                .setRequiresCharging(true)
//                .build()
//
//            val workRequest = PeriodicWorkRequestBuilder<BackgroundWorkerService>(1, TimeUnit.DAYS)
//                .setConstraints(constraints)
//                .setInitialDelay(calculateDelayUntilMidnight(), TimeUnit.MILLISECONDS)
//                .build()

            val workRequest = PeriodicWorkRequestBuilder<BackgroundWorkerService>(
                15, TimeUnit.MINUTES // Minimal 15 menit
            ).build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                Attributes.workerName.backgroundWorker,
                ExistingPeriodicWorkPolicy.KEEP, // Menjaga schedule yang sudah ada
                workRequest
            )
        }
    }
}