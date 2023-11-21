package com.example.planify

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planify.databinding.ActivityTasksNoteBinding

class TasksNotesActivity : AppCompatActivity() {

    //Tasks vars
    private lateinit var tasksRecyclerView: RecyclerView
    private lateinit var tasksAdapter: TaskEditableRecyclerView
    private lateinit var tasks: MutableList<TaskModel>

    // Notes vars
    private lateinit var notesRecyclerView: RecyclerView
    private lateinit var notesAdapter: NoteRecyclerView
    private lateinit var noteList: MutableList<NoteModel>
    private val AddNoteRequest = 1
    private val AddTaskRequest = 2
    private val UpdateNoteRequest = 3
    private val UpdateTaskRequest = 4

    private var isUpdatingNote: Boolean = false
    private lateinit var noteToUpdate: NoteModel

    // Binding
    private lateinit var binding: ActivityTasksNoteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTasksNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            startActivityForResult(intent, AddTaskRequest)
        }

        // Tasks Recycler View
        tasks = generateSampleTasks().toMutableList()
        tasksRecyclerView = findViewById(R.id.recycleTasks)
        tasksAdapter = TaskEditableRecyclerView(tasks)
        tasksRecyclerView.adapter = tasksAdapter
        tasksRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // Notes Recycler View
        noteList = generateSampleNotes().toMutableList()
        notesRecyclerView = findViewById(R.id.recycleNotes)
        notesAdapter = NoteRecyclerView(noteList)
        notesRecyclerView.adapter = notesAdapter
        notesRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("TasksNotesActivity", "onActivityResult - requestCode: $requestCode, resultCode: $resultCode")
        when (requestCode) {
            AddNoteRequest -> {
                if (resultCode == Activity.RESULT_OK) {
                    val note = data?.getSerializableExtra("note") as? NoteModel
                    if (note != null) {
                        notesAdapter.addNote(note)
                    }
                }
            }
            UpdateNoteRequest -> {
                if (resultCode == Activity.RESULT_OK) {
                    val updatedNote = data?.getSerializableExtra("note") as NoteModel
                    Log.d("TasksNotesActivity", "Updated note ID: ${updatedNote.id}")
                    val position = noteList.indexOfFirst { it.id == updatedNote.id }
                    if (position != -1) {
                        noteList[position] = updatedNote
                        notesAdapter.notifyItemChanged(position)
                    }
                }
            }
        }
    }
    private fun generateSampleNotes(): List<NoteModel> {
        return listOf(
            NoteModel(1, "Main Memory", "Sample Description of Note 1", "11/20/23" ),
            NoteModel(2,"MP Specs", "Sample Description of Note 2", "11/21/23"),
            NoteModel(3,"Deadlines", "Sample Description of Note 2", "11/21/23"),
        )
    }
    private fun generateSampleTasks(): List<TaskModel> {
        return listOf(
            TaskModel(1, "Project3", "MOBDEVE", "In progress", "11/20/23"),
            TaskModel(2,"MCO2", "CSOPESY", "Todo", "11/20/23"),
            TaskModel(3,"Final Proj", "STINTSY", "Todo", "12/20/23"),
            TaskModel(4,"Notebook", "STINTSY", "Completed", "11/20/23")
        )
    }
}