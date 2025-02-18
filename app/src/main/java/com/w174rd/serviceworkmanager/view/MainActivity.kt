package com.w174rd.serviceworkmanager.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.w174rd.serviceworkmanager.databinding.ActivityMainBinding
import com.w174rd.serviceworkmanager.services.BackgroundWorkerService
import com.w174rd.serviceworkmanager.services.ForegroundService
import com.w174rd.serviceworkmanager.services.ForegroundWorkerService
import com.w174rd.serviceworkmanager.utils.Attributes
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var  binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationBGChannel(context = this)
        createNotificationFGChannel(context = this)
        onClick()
    }

    private fun onClick() {
        binding.apply {
            btnStartBgWorkerService.setOnClickListener {
                val periodicWorkRequest = PeriodicWorkRequestBuilder<BackgroundWorkerService>(
                    15, TimeUnit.MINUTES // Minimal 15 menit
                ).build()

                WorkManager.getInstance(this@MainActivity).enqueueUniquePeriodicWork(
                    Attributes.workerName.periodicWorker,
                    ExistingPeriodicWorkPolicy.KEEP,
                    periodicWorkRequest
                )
            }

            btnStopBgWorkerService.setOnClickListener {
                WorkManager.getInstance(this@MainActivity).cancelUniqueWork(Attributes.workerName.periodicWorker)
            }

            btnStartFgWorkerService.setOnClickListener {
                val workRequest = OneTimeWorkRequestBuilder<ForegroundWorkerService>().build()

                WorkManager.getInstance(this@MainActivity).enqueueUniqueWork(
                    Attributes.workerName.foregroundWorker,
                    ExistingWorkPolicy.REPLACE, // Hapus worker lama jika ada yang berjalan
                    workRequest
                )
            }

            btnStopFgWorkerService.setOnClickListener {
                WorkManager.getInstance(this@MainActivity).cancelUniqueWork(Attributes.workerName.foregroundWorker)
            }

            btnStartFgService.setOnClickListener {
                val serviceIntent = Intent(this@MainActivity, ForegroundService::class.java)
                startService(serviceIntent)
            }

            btnStopFgService.setOnClickListener {
                val serviceIntent = Intent(this@MainActivity, ForegroundService::class.java)
                stopService(serviceIntent)
            }
        }
    }

    fun createNotificationBGChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Attributes.pushNotif.channelBackgroundWorkManager,
                "Background Work Manager Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    fun createNotificationFGChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Attributes.pushNotif.channelForegroundWorkManager,
                "Foreground Work Manager Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}