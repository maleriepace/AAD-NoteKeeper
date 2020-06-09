package com.example.notekeeper.database

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteInfoDao {
    @Query("SELECT note_info.*, course_info.course_title from note_info JOIN course_info ON course_info.course_id = note_info.course_id ORDER BY course_id ASC, note_title ASC")
    fun getNoteCourses(): LiveData<List<NoteCourse>>

    @Query("SELECT note_info.*, course_info.course_title from note_info JOIN course_info ON course_info.course_id = note_info.course_id ORDER BY course_id ASC, note_title ASC")
    fun getNoteCoursesCursor(): Cursor

    @Query("SELECT * from note_info ORDER BY course_id ASC, note_title ASC")
    fun getNotes(): LiveData<List<NoteInfo>>

    @Query("SELECT * from note_info ORDER BY course_id ASC, note_title ASC")
    fun getNotesNotLive(): List<NoteInfo>

    data class NoteCourse(
        @ColumnInfo(name="id") var id: Int,
        @ColumnInfo(name="course_id") var courseId: String,
        @ColumnInfo(name="note_title") var noteTitle: String,
        @ColumnInfo(name="note_text") var noteText: String,
        @ColumnInfo(name="course_title") val courseTitle: String)


    @Query("SELECT * from note_info WHERE id = :id")
    fun getNote(id:Int): LiveData<NoteInfo>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(noteInfo : NoteInfo)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(noteInfo : NoteInfo)
}