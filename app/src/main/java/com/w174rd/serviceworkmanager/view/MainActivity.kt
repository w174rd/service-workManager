package com.w174rd.serviceworkmanager.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.w174rd.serviceworkmanager.databinding.ActivityMainBinding
import com.w174rd.serviceworkmanager.services.BackgroundWorker
import com.w174rd.serviceworkmanager.utils.Attributes
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var  binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationChannel(context = this)
        onClick()
    }

    private fun onClick() {
        binding.apply {
            btnStartBgService.setOnClickListener {
                val periodicWorkRequest = PeriodicWorkRequestBuilder<BackgroundWorker>(
                    15, TimeUnit.MINUTES // Minimal 15 menit
                ).build()

                WorkManager.getInstance(this@MainActivity).enqueueUniquePeriodicWork(
                    Attributes.workerName.periodicWorker,
                    ExistingPeriodicWorkPolicy.KEEP,
                    periodicWorkRequest
                )
            }

            btnStopBgService.setOnClickListener {
                WorkManager.getInstance(this@MainActivity).cancelUniqueWork(Attributes.workerName.periodicWorker)
            }

            btnStartFgService.setOnClickListener {

            }

            btnStopFgService.setOnClickListener {

            }
        }
    }

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Attributes.pushNotif.channelWorkManager,
                "Work Manager Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}