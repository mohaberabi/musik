package com.mohaberabi.musik

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.content.getSystemService


class MusikApplication : Application() {


    companion object {
        const val CHANNEL_ID = "MUSIK"
    }


    override fun onCreate() {
        super.onCreate()
        initNotifications()
    }


    private fun initNotifications() {
        val notificationManager = getSystemService<NotificationManager>()!!
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Music Playback",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for music playback"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }
}