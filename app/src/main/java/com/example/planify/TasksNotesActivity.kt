package com.example.planify

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.gesture.Gesture
import android.gesture.GestureLibraries
import android.gesture.GestureLibrary
import android.gesture.GestureOverlayView
import android.gesture.Prediction
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planify.database.NoteDatabase
import com.example.planify.database.TaskDatabase
import com.example.planify.databinding.ActivityTasksNoteBinding
import com.example.planify.model.NoteModel
import com.example.planify.model.TaskModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TasksNotesActivity : AppCompatActivity() {

    enum class SortingOrder {
        ASCENDING,
        DESCENDING
    }
    //Tasks vars
    private lateinit var tasksRecyclerView: RecyclerView
    private lateinit var tasksAdapter: TaskEditableRecyclerView
    private lateinit var taskList: ArrayList<TaskModel>
    private lateinit var taskDatabase: TaskDatabase
    private var taskCurrentSortingOrder = SortingOrder.ASCENDING
    // Notes vars
    private lateinit var notesRecyclerView: RecyclerView
    private lateinit var notesAdapter: NoteRecyclerView
    private lateinit var noteList: ArrayList<NoteModel>
    private lateinit var noteDatabase: NoteDatabase
    private var noteCurrentSortingOrder = SortingOrder.ASCENDING

    // Gesture vars
    private lateinit var gLibrary: GestureLibrary

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
            when (taskCurrentSortingOrder) {
                SortingOrder.ASCENDING -> {
                    taskList.sortWith(compareBy { it.deadline.toDateFormat("MM/dd/yy") })
                    taskCurrentSortingOrder = SortingOrder.DESCENDING
                }
                SortingOrder.DESCENDING -> {
                    taskList.sortWith(compareByDescending { it.deadline.toDateFormat("MM/dd/yy") })
                    taskCurrentSortingOrder = SortingOrder.ASCENDING
                }
            }
            tasksAdapter.notifyDataSetChanged()
        }
        binding.notesSortBtn.setOnClickListener {
            when (noteCurrentSortingOrder) {
                SortingOrder.ASCENDING -> {
                    noteList.sortWith(compareBy { it.date.toDateFormat("MM/dd/yy") })
                    noteCurrentSortingOrder = SortingOrder.DESCENDING
                }
                SortingOrder.DESCENDING -> {
                    noteList.sortWith(compareByDescending { it.date.toDateFormat("MM/dd/yy") })
                    noteCurrentSortingOrder = SortingOrder.ASCENDING
                }
            }
            notesAdapter.notifyDataSetChanged()
        }

        // Gestures
        gLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures)
        if (!gLibrary.load()) {
            finish()
        }
        val gestureOverlayView = findViewById<GestureOverlayView>(R.id.gestureOverlayView)
        gestureOverlayView.addOnGesturePerformedListener { _, gesture ->
            handleGesture(gesture)
        }


        // Tasks Recycler View
        taskDatabase = TaskDatabase(applicationContext)
        taskList = taskDatabase.getTaskList()
        Log.d("TasksNotesActivity", "taskList.size ${taskList.size}")
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
        noteDatabase = NoteDatabase(applicationContext)
        noteList = noteDatabase.getNoteList()
        notesRecyclerView = findViewById(R.id.recycleNotes)
        notesAdapter = NoteRecyclerView(noteList)
        notesRecyclerView.adapter = notesAdapter
        notesRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

    }
    private fun handleGesture(gesture: Gesture) {
        val predictions: ArrayList<Prediction> = gLibrary.recognize(gesture)
        if (predictions.isNotEmpty()) {
            val prediction = predictions[0]

            if (prediction.score > 1) {
                Log.d("TasksNotesActivity", "name: ${prediction.score}")
                Log.d("TasksNotesActivity", "name: ${prediction.name}")
                when (prediction.name) {
                    "T" -> createNewTask() // vertical line // scroll down
                    "N" -> createNewNote() // circle
                    "N2" -> createNewNote() // circle
                }
            }
        }
        else{
            Log.d("TasksNotesActivity", "invalid prediction")
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("TasksNotesActivity", "onActivityResult - requestCode: $requestCode, resultCode: $resultCode")
        when (requestCode) {
            AddNoteRequest -> {
                if(resultCode == Activity.RESULT_OK) {
                    val note = data?.getSerializableExtra("note") as? NoteModel
                    if (note != null) {
                        val noteID = noteDatabase.addNote(note)
                        note.id = noteID
                        notesAdapter.addNote(note)
                        notesAdapter.notifyItemInserted(noteList.size - 1)
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
                        val note = noteList[position]
                        noteList.removeAt(position)
                        noteDatabase.deleteNote(note)
                        notesAdapter.notifyItemRemoved(position)
//                        notesAdapter.notifyItemRangeChanged(position, noteList.size)
                    } else {
                        Log.e("TasksNotesActivity", "Invalid note position received for deletion")
                    }
                }
                else if(resultCode == Activity.RESULT_OK) {
                    val updatedNote = data?.getSerializableExtra("note") as NoteModel
                    val position = noteList.indexOfFirst { it.id == updatedNote.id }
                    if (position >= 0) {
                        noteList[position] = updatedNote
                        noteDatabase.updateTask(updatedNote)
                        notesAdapter.notifyItemChanged(position)
                    }
                }
            }
            AddTaskRequest -> {
                if (resultCode == Activity.RESULT_OK) {
                    val task = data?.getSerializableExtra("task") as? TaskModel
                    Log.d("TasksNotesActivity", "AddTaskRequest result received")
                    if (task != null) {
                        val taskId = taskDatabase.addTask(task)
                        task.id = taskId
                        tasksAdapter.addTask(task)
                        tasksAdapter.notifyItemInserted(taskList.size - 1)
                    }
                }
            }
            // Handles both updating and deleting when in EditTaskActivity
            UpdateTaskRequest -> {
                if (data?.getBooleanExtra("deleteTask", false) == true) {
                    // Handle delete task
                    val position = data.getIntExtra("taskPosition", -1)
                    if (position >= 0) {
                        val task = taskList[position]
                        taskList.removeAt(position) // Call the removeTask function
                        taskDatabase.deleteTask(task)
                        tasksAdapter.notifyItemRemoved(position)
//                        tasksAdapter.notifyItemRangeChanged(position, taskList.size)
                    } else {
                        Log.e("TasksNotesActivity", "Invalid task position received for deletion")
                    }
                } else if(resultCode == Activity.RESULT_OK){
                    // Handle update task
                    val updatedTask = data?.getSerializableExtra("task") as? TaskModel
                    if (updatedTask != null) {
                        Log.d("TasksNotesActivity", "UpdateTaskRequest result received")
                        taskDatabase.updateTask(updatedTask)
                        tasksAdapter.updateTask(updatedTask)
                    }
                }
            }
        }
    }
    private fun createNewTask() {
        // Navigate to AddTaskActivity
        val intent = Intent(this, AddTaskActivity::class.java)
        startActivityForResult(intent, AddTaskRequest)
    }
    private fun createNewNote() {
        // Navigate to AddNoteActivity
        val intent = Intent(this, AddNoteActivity::class.java)
        startActivityForResult(intent, AddNoteRequest)
    }
    // Handles deleting from TaskEditableRecyclerView
    private fun showDeleteTaskDialog(task: TaskModel, position: Int) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Delete Task")
        alertDialogBuilder.setMessage("Are you sure you want to delete this task?")
        alertDialogBuilder.setPositiveButton("Delete") { _, _ ->
            // Delete the task and update the UI
            taskList.removeAt(position)
            taskDatabase.deleteTask(task)
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
}