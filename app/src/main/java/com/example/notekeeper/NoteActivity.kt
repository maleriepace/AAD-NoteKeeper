package com.example.notekeeper

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.notekeeper.database.CourseInfo
import com.example.notekeeper.database.NoteInfo
import com.example.notekeeper.notes.NoteActivityViewModel

import kotlinx.android.synthetic.main.activity_note.*
import kotlinx.android.synthetic.main.content_note.*

class NoteActivity : AppCompatActivity() {
    private val viewModel: NoteActivityViewModel by viewModels()
    private var note : NoteInfo? = null
    var isNewNote : Boolean = false
    var noteId: Int = -1
    var isCanceling : Boolean = false
    val ID_NOT_SET = -1
    var courses = emptyList<CourseInfo>()
    private lateinit var adapterCourses: ArrayAdapter<Any>


    companion object {
        val NOTE_ID = "com.example.notekeeper.NOTE_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        setSupportActionBar(toolbar)

        if(viewModel.isNewlyCreated && savedInstanceState != null)
        {
            viewModel.restoreState(savedInstanceState)
        }
        viewModel.isNewlyCreated = false

        adapterCourses = ArrayAdapter<Any>(this, android.R.layout.simple_spinner_item)
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        viewModel.allCourses.observe(this, Observer { courses ->
            this.courses = courses
            courses?.forEach {
                adapterCourses.add(it)
            }
        })
        spinner_courses.adapter = adapterCourses

        readDisplayStateValues()
        saveOriginalNoteValues()

        if(!isNewNote)
        {
            loadNoteData()
        }
    }

    private fun loadNoteData() {
        viewModel.getNote(noteId).observe(this, Observer { note ->
            this.note = note
            displayNote()
        })
    }

    private fun saveOriginalNoteValues() {
        if(isNewNote){
            return
        }
        viewModel.originalNoteCourseId = note?.courseId
        viewModel.originalNoteTitle = note?.noteTitle
        viewModel.originalNoteText = note?.noteText
    }

    private fun displayNote() {
        var courseIndex = getIndexOfCourseId(note?.courseId)
        if (courseIndex != null) {
            spinner_courses.setSelection(courseIndex)
        }
        text_note_title.setText(note?.noteTitle)
        text_note_text.setText(note?.noteText)
    }

    private fun getIndexOfCourseId(courseId: String?): Int {
        if(courseId == null)
            return 0
        var course = courses?.find { course -> course.courseId == courseId }
        return courses?.indexOf(course)!!
    }

    private fun readDisplayStateValues() {
        noteId = intent.getIntExtra(NOTE_ID, ID_NOT_SET)
        isNewNote = noteId == ID_NOT_SET
        if(!isNewNote){
            viewModel.getNote(noteId).observe(this, Observer { note ->
                this.note = note
            })
        }
    }

    override fun onPause() {
        super.onPause()
        if(isCanceling) {
            if(!isNewNote){
                storePreviousNoteValues()
            }
        }else{
            saveNote()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(outState != null){
            viewModel.saveState(outState)
        }
    }

    private fun storePreviousNoteValues() {
        //var course = viewModel.originalNoteCourseId?.let { viewModel.allCourses.value?.find { course -> course.courseId == it } }
//        note?.courseId = course
//        note?.noteTitle = viewModel.originalNoteTitle
//        note?.text = viewModel.originalNoteText
    }

    private fun saveNote() {
        var course = spinner_courses.selectedItem as CourseInfo
        if(note != null) {
            note?.courseId = course?.courseId
            note?.noteTitle = text_note_title.text.toString()
            note?.noteText = text_note_text.text.toString()
            viewModel.update(note!!)
        }else {
            viewModel.insert(
                NoteInfo(
                    course?.courseId,
                    text_note_title.text.toString(),
                    text_note_text.text.toString()
                )
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_note, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        var item = menu?.findItem(R.id.action_next)
        var isLast = viewModel.allNotes.value?.last()?.id == note?.id
        item?.isEnabled = !isLast
        item?.isVisible = !isLast
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        var thing = 0;
        when (item.itemId) {
            R.id.action_send_mail -> {
                sendEmail()
                return true
            }
            R.id.action_next -> {
                moveNext()
            }
            R.id.action_cancel -> {
                isCanceling = true
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun moveNext() {
        saveNote()

        var index = viewModel.allNotes.value?.indexOf(note)
        if (index != null) {
            note = viewModel.allNotes.value?.get(index.plus(1))
        }
        saveOriginalNoteValues()
        displayNote()
        invalidateOptionsMenu()
    }

    private fun sendEmail() {
        var course = spinner_courses.selectedItem as CourseInfo
        var subject = "${text_note_title.text}"
        var text = "Checkout what I learned in the Pluralsight course \"${course.courseTitle}\"\n${text_note_text.text}"
        var intent = Intent(Intent.ACTION_SENDTO)
        intent.type = "message/rfc822"
        val uriText = "mailto:?subject=${subject}&body=${text}"
        intent.data = Uri.parse(uriText)
        startActivity(intent)
    }
}
