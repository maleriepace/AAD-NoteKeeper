package com.example.notekeeper

import org.junit.Test

import org.junit.Assert.*

class DataManagerTest {

    @Test
    fun createNewNote() {
        val instance = DataManager.instance
        var course = instance?.getCourse("android_async")
        var noteTitle = "Test note title"
        var noteText = "Test text"

        var noteIndex = instance?.createNewNote()
        var newNote = noteIndex?.let { instance?.notes?.get(it) }
        newNote?.course = course
        newNote?.title = noteTitle
        newNote?.text = noteText

        var compareNote = noteIndex?.let { instance?.notes?.get(it) }

        assertEquals(newNote?.course, compareNote?.course)
        assertEquals(newNote?.title, compareNote?.title)
        assertEquals(newNote?.text, compareNote?.text)

    }
}