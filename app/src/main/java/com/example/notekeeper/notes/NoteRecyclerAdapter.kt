package com.example.notekeeper.notes

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notekeeper.NoteActivity
import com.example.notekeeper.R
import com.example.notekeeper.database.NoteInfo
import com.example.notekeeper.database.NoteInfoDao

class NoteRecyclerAdapter(
    private var context: Context
) :
    RecyclerView.Adapter<NoteRecyclerAdapter.ViewHolder>() {
    private var layoutInflater : LayoutInflater = LayoutInflater.from(context)
    private var notes = emptyList<NoteInfoDao.NoteCourse>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textCourse : TextView = itemView.findViewById(R.id.text_course)
        val textTitle : TextView = itemView.findViewById(R.id.text_title)
        var id: Int = -1


        init {
            itemView.setOnClickListener {
                var intent = Intent(context, NoteActivity::class.java)
                intent.putExtra(NoteActivity.NOTE_ID, id)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView = layoutInflater.inflate(R.layout.item_note_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var current = notes[position]

        holder.textCourse.text = current.courseTitle
        holder.textTitle.text = current.noteTitle
        holder.id = current.id
    }

    fun setNotes(notes: List<NoteInfoDao.NoteCourse>) {
        this.notes = notes
        notifyDataSetChanged()
    }
}