package com.example.notekeeper.notes

import androidx.lifecycle.LiveData
import com.example.notekeeper.database.NoteInfo
import com.example.notekeeper.database.NoteInfoDao

class NoteInfoRepository(private val noteInfoDao: NoteInfoDao) {
    val allNotes: LiveData<List<NoteInfo>> = noteInfoDao.getNotes()
    val allNoteCourses: LiveData<List<NoteInfoDao.NoteCourse>> = noteInfoDao.getNoteCourses()

    suspend fun insert(noteInfo: NoteInfo) {
        noteInfoDao.insert(noteInfo)
    }
    suspend fun update(noteInfo: NoteInfo) {
        noteInfoDao.update(noteInfo)
    }

    fun getNote(id: Int): LiveData<NoteInfo> {
        return noteInfoDao.getNote(id)
    }

    fun getNotesNotLive() : List<NoteInfo> {
        return noteInfoDao.getNotesNotLive()
    }
}