package com.example.notekeeper.notes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.notekeeper.database.NoteKeeperDatabase
import com.example.notekeeper.database.NoteInfo
import com.example.notekeeper.database.NoteInfoDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteInfoViewModel(application: Application) :AndroidViewModel(application) {
    private val repository : NoteInfoRepository

    val allNotes: LiveData<List<NoteInfo>>
    val allNoteCourses: LiveData<List<NoteInfoDao.NoteCourse>>

    init {
        val noteInfoDao = NoteKeeperDatabase.getDatabase(
            application,
            viewModelScope
        ).noteInfoDao()
        repository = NoteInfoRepository(noteInfoDao)
        allNotes = repository.allNotes
        allNoteCourses = repository.allNoteCourses
    }

    fun insert(noteInfo: NoteInfo) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(noteInfo)
    }
}