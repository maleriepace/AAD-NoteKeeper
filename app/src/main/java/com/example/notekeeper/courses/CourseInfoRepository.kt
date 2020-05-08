package com.example.notekeeper.courses

import androidx.lifecycle.LiveData
import com.example.notekeeper.database.CourseInfo
import com.example.notekeeper.database.CourseInfoDao

class CourseInfoRepository(private val courseInfoDao: CourseInfoDao) {
    val allCourses: LiveData<List<CourseInfo>> = courseInfoDao.getCourses()

    suspend fun insert(courseInfo: CourseInfo) {
        courseInfoDao.insert(courseInfo)
    }
}