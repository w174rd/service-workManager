package com.w174rd.serviceworkmanager.view

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

        // Cek dan minta permission untuk Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission()
        }

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
                    Attributes.workerName.backgroundWorker,
                    ExistingPeriodicWorkPolicy.KEEP, // Menjaga schedule yang sudah ada
                    periodicWorkRequest
                )
            }

            btnStopBgWorkerService.setOnClickListener {
                WorkManager.getInstance(this@MainActivity).cancelUniqueWork(Attributes.workerName.backgroundWorker)
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

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d("Permission", "Izin notifikasi diberikan ✅")
        } else {
            Log.d("Permission", "Izin notifikasi ditolak ❌")
        }
    }

    private fun requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
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