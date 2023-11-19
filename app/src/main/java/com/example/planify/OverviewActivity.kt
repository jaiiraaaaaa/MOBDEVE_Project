package com.example.planify
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planify.databinding.ActivityOverviewBinding

class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(note: Note) {
        val titleTextView = itemView.findViewById<TextView>(R.id.note_name)
        titleTextView.text = note.title
    }
}
class ItemAdapter(private val notes: List<Note>) : RecyclerView.Adapter<ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note)
    }
    override fun getItemCount(): Int {
        return notes.size
    }
}
class OverviewActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HorizontalRecyclerView
    private var notes = mutableListOf<Note>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding: ActivityOverviewBinding = ActivityOverviewBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        notes = generateSampleNotes().toMutableList()
        recyclerView = findViewById(R.id.notesRecyclerView)
        adapter = HorizontalRecyclerView(notes)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val addButton: ImageButton = findViewById(R.id.add_note_btn)
        addButton.setOnClickListener {
            notes.add(Note("New Note"))
            adapter.notifyDataSetChanged()
        }
    }
    private fun generateSampleNotes(): List<Note> {
        return listOf(
            Note("Note 1"),
            Note("Note 2"),
        )
    }
}