package com.example.notekeeper

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notekeeper.courses.CourseInfoViewModel
import com.example.notekeeper.courses.CourseRecyclerAdapter
import com.example.notekeeper.notes.NoteInfoViewModel
import com.example.notekeeper.notes.NoteRecyclerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var coursesLayoutManager: GridLayoutManager
    private lateinit var noteRecyclerAdapter : NoteRecyclerAdapter
    private lateinit var courseRecyclerAdapter : CourseRecyclerAdapter
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var recyclerItems: RecyclerView
    private lateinit var notesLayoutManager: LinearLayoutManager
    private lateinit var courseInfoViewModel : CourseInfoViewModel
    private lateinit var noteInfoViewModel : NoteInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            startActivity(Intent(this@MainActivity, NoteActivity::class.java))

        }

        //NoteKeeperDatabase.getDatabase(application, CoroutineScope(Dispatchers.Main)).courseInfoDao().getCourses()

        PreferenceManager.setDefaultValues(this, R.xml.general_preferences, false)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_notes, R.id.nav_courses, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_notes -> {
                    displayNotes()
                }
                R.id.nav_courses -> {
                    displayCourses()
                }
                R.id.nav_share -> {
                    handleShare()
                    //handleSelection(R.string.nav_share_message)
                }
                R.id.nav_send -> {
                    handleSelection(R.string.nav_send_message)
                }
            }

            val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
            drawer.closeDrawer(GravityCompat.START)
            true
        }

        initializeDisplayContent()
    }

    private fun handleShare() {
        var view = findViewById<View>(R.id.list_items)
        Snackbar.make(view, "Share to - " +
                PreferenceManager.getDefaultSharedPreferences(this).getString("user_favorite_social", "").toString(),
            Snackbar.LENGTH_LONG).show()
    }

    private fun handleSelection(messageId : Int) {
        var view = findViewById<View>(R.id.list_items)
        Snackbar.make(view, messageId, Snackbar.LENGTH_LONG).show()
    }

    private fun initializeDisplayContent() {
        recyclerItems = findViewById<RecyclerView>(R.id.list_items)
        notesLayoutManager = LinearLayoutManager(this)
        coursesLayoutManager = GridLayoutManager(this, getResources().getInteger(R.integer.course_grid_span))

        noteRecyclerAdapter =
            NoteRecyclerAdapter(this)

        courseRecyclerAdapter =
            CourseRecyclerAdapter(this)

        courseInfoViewModel = ViewModelProvider(this).get(CourseInfoViewModel::class.java)
        courseInfoViewModel.allCourses.observe(this, Observer { courses ->
            courses?.let { courseRecyclerAdapter.setCourses(it) }
        })

        noteInfoViewModel = ViewModelProvider(this).get(NoteInfoViewModel::class.java)
        noteInfoViewModel.allNoteCourses.observe(this, Observer { notes ->
            notes?.let { noteRecyclerAdapter.setNotes(it) }
        })
        displayNotes()
    }

    private fun displayNotes() {
        recyclerItems.layoutManager = notesLayoutManager
        recyclerItems.adapter = noteRecyclerAdapter

        selectNavigationMenuItem(R.id.nav_notes)
    }

    private fun displayCourses() {
        recyclerItems.layoutManager = coursesLayoutManager
        recyclerItems.adapter = courseRecyclerAdapter

        selectNavigationMenuItem(R.id.nav_courses)
    }

    private fun selectNavigationMenuItem(id: Int) {
        var navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.menu.findItem(id).isChecked = true
    }

    override fun onResume() {
        super.onResume()

        updateNavHeader()
    }

    private fun updateNavHeader() {
        var navigationView = findViewById<NavigationView>(R.id.nav_view)
        var headerView = navigationView.getHeaderView(0)
        var textUserName = headerView.findViewById<TextView>(R.id.text_user_name)
        var textEmailAddress = headerView.findViewById<TextView>(R.id.text_email_address)

        var pref = PreferenceManager.getDefaultSharedPreferences(this)
        var userName = pref.getString("user_display_name", "")
        var emailAddress = pref.getString("user_email_address", "")

        textUserName.text = userName
        textEmailAddress.text = emailAddress
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
