package com.example.planify

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoteRecyclerView(private val notesList: MutableList<NoteModel>) : RecyclerView.Adapter<NoteRecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_notes_card, parent, false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return notesList.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notesList[position]
        val titleTextView = holder.itemView.findViewById<TextView>(R.id.note_title_tv)
        val descriptionTextView = holder.itemView.findViewById<TextView>(R.id.note_desc_tv)
        val dateTextView = holder.itemView.findViewById<TextView>(R.id.note_date_tv)

        titleTextView.text = note.title
        descriptionTextView.text = note.description
        dateTextView.text = note.date
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.note_title_tv)
        val descriptionTextView: TextView = view.findViewById(R.id.note_desc_tv)
        val dateTextView: TextView = view.findViewById(R.id.note_date_tv)
    }
    fun addNote(note: NoteModel) {
        notesList.add(note)
        notifyDataSetChanged()
    }
    fun returnIdCount(): Int {
        return notesList.size + 1
    }
}