package com.example.notekeeper.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CourseInfoDao {
    @Query("SELECT * from course_info ORDER BY course_title ASC")
    fun getCourses(): LiveData<List<CourseInfo>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(courseInfo : CourseInfo)
}