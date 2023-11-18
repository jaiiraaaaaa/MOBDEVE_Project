package com.example.planify

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planify.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var notesAdapter: NotesAdapter

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HorizontalRecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.mainBtnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            this.startActivity(intent)
        }

        viewBinding.mainBtnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            this.startActivity(intent)
        }
//        val sampleNotes = generateSampleNotes()
//        notesAdapter = NotesAdapter(sampleNotes)
//        val recyclerView = findViewById<RecyclerView>(R.id.notesRecyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        recyclerView.adapter = notesAdapter
        recyclerView = findViewById(R.id.notesRecyclerView)
        adapter = HorizontalRecyclerView()

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter
    }
    private fun generateSampleNotes(): List<Note> {
        return listOf(
            Note(1, "Note 1", "Description for Note 1", "2023-11-18"),
            Note(2, "Note 2", "Description for Note 2", "2023-11-19"),
        )
    }
}