package com.example.notekeeper.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "course_info", indices = [Index(
    value = ["course_id"],
    unique = true
), Index(value = ["course_title"])])
data class CourseInfo(
    @ColumnInfo(name="course_id") val courseId: String,
    @ColumnInfo(name="course_title") val courseTitle: String)
{
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0

    override fun toString(): String {
        return this.courseTitle
    }
}