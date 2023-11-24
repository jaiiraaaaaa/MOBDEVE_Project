package com.example.planify

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planify.databinding.ActivityTasksNoteBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TasksNotesActivity : AppCompatActivity() {

    //Tasks vars
    private lateinit var tasksRecyclerView: RecyclerView
    private lateinit var tasksAdapter: TaskEditableRecyclerView
    private lateinit var taskList: MutableList<TaskModel>

    // Notes vars
    private lateinit var notesRecyclerView: RecyclerView
    private lateinit var notesAdapter: NoteRecyclerView
    private lateinit var noteList: MutableList<NoteModel>

    private val AddNoteRequest = 1
    private val AddTaskRequest = 2
    private val UpdateNoteRequest = 3
    private val UpdateTaskRequest = 4

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
        binding.tasksSortBtn.setOnClickListener {
            taskList.sortWith(compareBy { it.deadline.toDateFormat("MM/dd/yy") })
            tasksAdapter.notifyDataSetChanged()
        }

        // Tasks Recycler View
        taskList = generateSampleTasks().toMutableList()
        tasksRecyclerView = findViewById(R.id.recycleTasks)
        tasksAdapter = TaskEditableRecyclerView(taskList)
        tasksRecyclerView.adapter = tasksAdapter
        tasksRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        tasksAdapter.setOnTaskDeleteClickListener(object : TaskEditableRecyclerView.OnTaskDeleteClickListener {
            override fun onTaskDeleteClick(task: TaskModel, position: Int) {
                // Show a dialog or confirmation prompt before deleting the task
                showDeleteTaskDialog(task, position)
            }
        })

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
                if(resultCode == Activity.RESULT_OK) {
                    val note = data?.getSerializableExtra("note") as? NoteModel
                    if (note != null) {
                        notesAdapter.addNote(note)
                    }
                }
            }
            // Handles both updating and deleting when in EditNoteActivity
            UpdateNoteRequest -> {
                if(data?.getBooleanExtra("deleteNote", false) == true){
                    Log.d("TasksNotesActivity: ", "went in here del note")
                    val position = data.getIntExtra("notePosition", -1)
                    if (position >= 0) {
                        Log.d("TasksNotesActivity: ", "went in here del note if statement")
                        notesAdapter.removeNote(position)
                        notesAdapter.notifyItemRemoved(position)
                        notesAdapter.notifyItemRangeChanged(position, noteList.size)
                    } else {
                        Log.e("TasksNotesActivity", "Invalid note position received for deletion")
                    }
                }
                else if(resultCode == Activity.RESULT_OK) {
                    val updatedNote = data?.getSerializableExtra("note") as NoteModel
                    val position = noteList.indexOfFirst { it.id == updatedNote.id }
                    if (position >= 0) {
                        noteList[position] = updatedNote
                        notesAdapter.notifyItemChanged(position)
                    }
                }
            }
            AddTaskRequest -> {
                if (resultCode == Activity.RESULT_OK) {
                    val task = data?.getSerializableExtra("task") as? TaskModel
                    Log.d("TasksNotesActivity", "AddTaskRequest result received")
                    if (task != null) {
                        tasksAdapter.addTask(task)
                    }
                }
            }
            // Handles both updating and deleting when in EditTaskActivity
            UpdateTaskRequest -> {
                if (data?.getBooleanExtra("deleteTask", false) == true) {
                    // Handle delete task
                    val position = data.getIntExtra("taskPosition", -1)
                    if (position >= 0) {
                        tasksAdapter.removeTask(position) // Call the removeTask function
                        tasksAdapter.notifyItemRemoved(position)
                        tasksAdapter.notifyItemRangeChanged(position, taskList.size)
                    } else {
                        Log.e("TasksNotesActivity", "Invalid task position received for deletion")
                    }
                } else if(resultCode == Activity.RESULT_OK){
                    // Handle update task
                    val updatedTask = data?.getSerializableExtra("task") as? TaskModel
                    if (updatedTask != null) {
                        Log.d("TasksNotesActivity", "UpdateTaskRequest result received")
                        tasksAdapter.updateTask(updatedTask)
                    }
                }
            }
        }
    }
    // Handles deleting from TaskEditableRecyclerView
    private fun showDeleteTaskDialog(task: TaskModel, position: Int) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Delete Task")
        alertDialogBuilder.setMessage("Are you sure you want to delete this task?")
        alertDialogBuilder.setPositiveButton("Delete") { _, _ ->
            // Delete the task and update the UI
            taskList.removeAt(position)
            tasksAdapter.notifyItemRemoved(position)
        }
        alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialogBuilder.show()
    }
    fun String.toDateFormat(format: String): Date {
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        return sdf.parse(this) ?: Date()
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