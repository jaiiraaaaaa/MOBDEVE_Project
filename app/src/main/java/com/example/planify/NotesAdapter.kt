package com.example.planify

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotesAdapter(private val notes: MutableList<DataNote>) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_notes_card, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.noteTitleTv.text = note.title
        holder.noteDescTv.text = note.content
        holder.noteDateTv.text = note.date
    }
    override fun getItemCount() = notes.size
    fun addNote(note: DataNote) {
        notes.add(note)
        notifyDataSetChanged()
    }
    class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val noteTitleTv: TextView = view.findViewById(R.id.note_title_tv)
        val noteDescTv: TextView = view.findViewById(R.id.note_desc_tv)
        val noteDateTv: TextView = view.findViewById(R.id.note_date_tv)
    }
}
