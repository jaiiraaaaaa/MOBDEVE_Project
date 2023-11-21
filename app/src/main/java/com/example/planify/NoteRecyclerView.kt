package com.example.planify

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
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
        val expandButton = holder.itemView.findViewById<ImageButton>(R.id.note_expand_btn)

        titleTextView.text = note.title
        descriptionTextView.text = note.description
        dateTextView.text = note.date

        expandButton.setOnClickListener {
            val intent = Intent(it.context, EditNoteActivity::class.java)
            intent.putExtra("note", note)
            (it.context as Activity).startActivityForResult(intent, position)
        }
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
    fun updateNote(updatedNote: NoteModel) {
        val index = notesList.indexOfFirst { it.id == updatedNote.id }
        if (index != -1) {
            notesList[index] = updatedNote
            Log.d("NoteUpdate", "Updated note at index $index: $updatedNote")
            notifyItemChanged(index)
            notifyDataSetChanged() // Make sure to call notifyDataSetChanged
        } else {
            Log.e("NoteUpdate", "Note not found for update: $updatedNote")
        }
    }

}