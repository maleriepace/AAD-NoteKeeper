package com.example.notekeeper

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.rule.ActivityTestRule
import com.example.notekeeper.notes.NoteRecyclerAdapter
import org.junit.Rule
import org.hamcrest.Matchers.*;
import org.junit.Test

class NextThroughNotesTest{
    @get:Rule
    var activityTestRule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @Test
    fun nextThroughNotes(){
        onView(withId(R.id.drawer_layout)).perform((DrawerActions.open()))
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_notes))

        onView(withId(R.id.list_items)).perform(RecyclerViewActions.actionOnItemAtPosition<NoteRecyclerAdapter.ViewHolder>(0,click()))

        var notes = DataManager.instance?.notes!!
        for (i in notes.indices) {
            var note = notes[i]

            onView(withId(R.id.spinner_courses)).check(matches(
                withSpinnerText(note.course?.title!!)))
            onView(withId(R.id.text_note_title)).check(matches(
                withText(note.title!!)
            ))
            onView(withId(R.id.text_note_text)).check(matches(
                withText(note.text!!)))

            if(i != notes.size -1) {
                onView(allOf(withId(R.id.action_next), isEnabled())).perform(click())
            }
        }
        onView(withId(R.id.action_next)).check(doesNotExist())
    }
}