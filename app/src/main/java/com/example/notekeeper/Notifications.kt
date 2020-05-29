package com.example.notekeeper

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class Notifications {
    fun createNotificationChannel(context: Context, importance: Int, showBadge: Boolean, name: String, description: String){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channelId = "${context.packageName}-$name"
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            channel.setShowBadge(showBadge)

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun createNotification(context: Context, title: String, message: String, autoCancel: Boolean, noteId: Int){
        val channelId = "${context.packageName}-${context.getString(R.string.app_name)}"

        var noteActivityIntent = Intent(context, NoteActivity::class.java)
        noteActivityIntent.putExtra(NoteActivity.NOTE_ID, noteId)

        val notificationBuilder = NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.drawable.ic_menu_camera)
            setStyle(NotificationCompat.BigTextStyle().bigText(message).setBigContentTitle(title).setSummaryText("Review Note"))
            setContentTitle(title)
            setContentText(message)
            priority = NotificationCompat.PRIORITY_DEFAULT
            setAutoCancel(autoCancel)
            setContentIntent(PendingIntent.getActivity(context, 0, noteActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT))
            addAction(0, "View all notes", PendingIntent.getActivity(context, 0, Intent(context, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT))
        }

        val notificationManager = NotificationManagerCompat.from(context)

        notificationManager.notify(1001, notificationBuilder.build())
    }
}