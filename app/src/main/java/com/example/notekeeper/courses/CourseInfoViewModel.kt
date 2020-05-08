package com.example.notekeeper.courses

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.notekeeper.database.NoteKeeperDatabase
import com.example.notekeeper.database.CourseInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CourseInfoViewModel(application: Application) :AndroidViewModel(application) {
    private val repository : CourseInfoRepository

    val allCourses: LiveData<List<CourseInfo>>

    init {
        val courseInfoDao = NoteKeeperDatabase.getDatabase(
            application,
            viewModelScope
        ).courseInfoDao()
        repository =
            CourseInfoRepository(courseInfoDao)
        allCourses = repository.allCourses
    }

    fun insert(courseInfo: CourseInfo) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(courseInfo)
    }
}