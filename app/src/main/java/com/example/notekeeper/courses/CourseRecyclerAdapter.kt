package com.example.notekeeper.courses

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notekeeper.R
import com.example.notekeeper.database.CourseInfo
import com.google.android.material.snackbar.Snackbar

class CourseRecyclerAdapter(
    private var context: Context
) :
    RecyclerView.Adapter<CourseRecyclerAdapter.ViewHolder>() {
    private var layoutInflater : LayoutInflater = LayoutInflater.from(context)
    private var courses = emptyList<CourseInfo>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textCourse : TextView = itemView.findViewById(R.id.text_course)

        init {
            itemView.setOnClickListener {
                courses[this.layoutPosition]?.courseTitle?.let { it1 ->
                    Snackbar.make(it,
                        it1, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView = layoutInflater.inflate(R.layout.item_course_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return courses.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var course = courses[position]
        holder.textCourse.text = course?.courseTitle
    }

    internal fun setCourses(cours: List<CourseInfo>){
        this.courses = cours
        notifyDataSetChanged()
    }
}