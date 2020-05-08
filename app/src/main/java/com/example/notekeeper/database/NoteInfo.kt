package com.example.notekeeper.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "note_info", indices = [Index(
    value = ["note_title"]
)])
data class NoteInfo(
    @ColumnInfo(name="course_id") var courseId: String,
    @ColumnInfo(name="note_title") var noteTitle: String,
    @ColumnInfo(name="note_text") var noteText: String)
{
@PrimaryKey(autoGenerate = true)
var id : Int = 0

}