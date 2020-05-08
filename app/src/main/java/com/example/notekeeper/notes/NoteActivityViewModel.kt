package com.example.notekeeper.notes

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.notekeeper.database.NoteKeeperDatabase
import com.example.notekeeper.courses.CourseInfoRepository
import com.example.notekeeper.database.CourseInfo
import com.example.notekeeper.database.NoteInfo
import com.example.notekeeper.database.NoteInfoDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val courseInfoRepository : CourseInfoRepository
    private val noteInfoRepository : NoteInfoRepository
    val allCourses: LiveData<List<CourseInfo>>
    var allNotes: LiveData<List<NoteInfo>>
    var allNoteCourses: LiveData<List<NoteInfoDao.NoteCourse>>
    val ORIGINAL_NOTE_COURSE_ID = "com.example.notekeeper.ORIGINAL_NOTE_COURSE_ID"
    val ORIGINAL_NOTE_TITLE = "com.example.notekeeper.ORIGINAL_NOTE_TITLE"
    val ORIGINAL_NOTE_TEXT = "com.example.notekeeper.ORIGINAL_NOTE_TEXT"

    var originalNoteText: String? = null
    var originalNoteTitle: String? = null
    var originalNoteCourseId: String? = null
    var isNewlyCreated : Boolean = true

    init {
        var db = NoteKeeperDatabase.getDatabase(
            application,
            viewModelScope
        )
        val courseInfoDao = db.courseInfoDao()
        val noteInfoDao = db.noteInfoDao()
        courseInfoRepository =
            CourseInfoRepository(courseInfoDao)
        allCourses = courseInfoRepository.allCourses
        noteInfoRepository =
            NoteInfoRepository(noteInfoDao)
        allNotes = noteInfoRepository.allNotes
        allNoteCourses = noteInfoRepository.allNoteCourses
    }

    fun getNote(id:Int): LiveData<NoteInfo> {
        return noteInfoRepository.getNote(id)
    }

    fun insert(note: NoteInfo) = viewModelScope.launch(Dispatchers.IO) {
        noteInfoRepository.insert(note)
    }

    fun update(note: NoteInfo) = viewModelScope.launch(Dispatchers.IO) {
        noteInfoRepository.update(note)
    }

    fun saveState(outState: Bundle) {
        outState.putString(ORIGINAL_NOTE_COURSE_ID, originalNoteCourseId)
        outState.putString(ORIGINAL_NOTE_TITLE, originalNoteTitle)
        outState.putString(ORIGINAL_NOTE_TEXT, originalNoteText)
    }

    fun restoreState(inState: Bundle){
        originalNoteCourseId = inState.getString(ORIGINAL_NOTE_COURSE_ID)
        originalNoteTitle = inState.getString(ORIGINAL_NOTE_TITLE)
        originalNoteText = inState.getString(ORIGINAL_NOTE_TEXT)
    }
}