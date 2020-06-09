package com.example.notekeeper

import android.app.IntentService
import android.content.Intent

class NoteBackupService : IntentService("NoteBackupService") {
    companion object {
        const val EXTRA_COURSE_ID = "com.example.notekeeper.extra.COURSE_ID"
    }

    override fun onHandleIntent(intent: Intent?) {
        if(intent != null){
            val backupCourseId = intent.getStringExtra(EXTRA_COURSE_ID)
            NoteBackup.doBackup(this, backupCourseId);
        }
    }
}
