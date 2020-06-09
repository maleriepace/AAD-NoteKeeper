package com.example.notekeeper

import android.content.Context
import android.util.Log
import com.example.notekeeper.database.NoteKeeperDatabase
import com.example.notekeeper.notes.NoteInfoRepository


object NoteBackup {
    private lateinit var db : NoteKeeperDatabase

    private val TAG = NoteBackup::class.java.simpleName

    fun doBackup(
        context: Context,
        backupCourseId: String
    ) {
        val repository : NoteInfoRepository

        val noteInfoDao = NoteKeeperDatabase.getDatabase(
            context,
            null
        ).noteInfoDao()
        repository =
            NoteInfoRepository(noteInfoDao)

        Log.i(
            TAG,
            ">>>***   BACKUP START - Thread: " + Thread.currentThread()
                .id + "   ***<<<"
        )

        var notes = repository.getNotesNotLive()
        for(note in notes) {
            Log.i(
                TAG,
                ">>>Backing Up Note<<< ${note.courseId}|${note.noteTitle}|${note.noteText}"
            )
            simulateLongRunningWork()
        }
        Log.i(TAG, ">>>***   BACKUP COMPLETE   ***<<<")
    }


    private fun simulateLongRunningWork() {
        try {
            Thread.sleep(1000)
        } catch (ex: Exception) {
        }
    }
}