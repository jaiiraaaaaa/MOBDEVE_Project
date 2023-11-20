package com.example.planify

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.planify.databinding.ActivityTasksNoteBinding

class TasksNotesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTasksNoteBinding
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var noteList: MutableList<DataNote>
    private val AddNoteRequest = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTasksNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noteList = generateSampleData()
        binding.recycleNotes.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        notesAdapter = NotesAdapter(noteList)
        binding.recycleNotes.adapter = notesAdapter

        binding.logoutBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity:: class.java))
            finish()
        }

        binding.dashboardNav.setOnClickListener (View.OnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        })

        binding.calendarNav.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.addNoteBtn.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivityForResult(intent, AddNoteRequest)
        }


        binding.addTaskBtn.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AddNoteRequest && resultCode == Activity.RESULT_OK) {
            val note = data?.getSerializableExtra("note") as? DataNote
            if (note != null) {
                notesAdapter.addNote(note)
                Log.d("TasksNotesActivity", "Note added: $note")
            }
        }
    }
    private fun generateSampleData(): MutableList<DataNote> {
        return mutableListOf(
            DataNote("Note 1", "11-11-2023", "This is the content of Note 1"),
            DataNote("Note 2", "12-25-2023", "This is the content of Note 2"),
            DataNote("Note 3", "01-01-2024", "This is the content of Note 3")
        )
    }
}