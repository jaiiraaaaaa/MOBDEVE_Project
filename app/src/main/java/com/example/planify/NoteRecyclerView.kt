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
import com.example.planify.model.NoteModel

class NoteRecyclerView(private val notesList: ArrayList<NoteModel>) : RecyclerView.Adapter<NoteRecyclerView.ViewHolder>() {
    private val UpdateNoteRequest = 3 // or any other unique integer value
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
            Log.d("NoteRecyclerView", "Starting EditNoteActivity for note ID: ${note.id}")
            val intent = Intent(it.context, EditNoteActivity::class.java)
            if(position >= 0) {
                intent.putExtra("notePosition", position)
                intent.putExtra("note", note)
                (it.context as Activity).startActivityForResult(intent, UpdateNoteRequest)
            }
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
    fun removeNote(position: Int) {
        notesList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }
}