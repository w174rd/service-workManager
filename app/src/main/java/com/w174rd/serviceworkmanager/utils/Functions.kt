package com.w174rd.serviceworkmanager.utils

import java.util.Calendar

object Functions {

    fun calculateDelayUntilMidnight(): Long {
        val now = Calendar.getInstance()
        val midnight = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            add(Calendar.DAY_OF_YEAR, 1) // Set untuk hari berikutnya
        }
        return midnight.timeInMillis - now.timeInMillis
    }
}