package com.example.notekeeper

import android.app.Service
import android.content.Intent
import android.os.IBinder

class NoteUploaderJobService : Service() {

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}
