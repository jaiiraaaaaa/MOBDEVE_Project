package com.example.planify

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planify.databinding.ActivityTasksNoteBinding

class TaskEditViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(task: TaskModel) {
        val titleTextView = itemView.findViewById<TextView>(R.id.task_name)
        titleTextView.text = task.title
    }
}
class TaskEditAdapter(private val tasks: List<TaskModel>) : RecyclerView.Adapter<TaskEditViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskEditViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_tasks_row, parent, false)
        return TaskEditViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskEditViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task)
    }
    override fun getItemCount(): Int {
        return tasks.size
    }
}
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
            if (isUpdatingNote) {
                intent.putExtra("noteToUpdate", noteToUpdate)
            }
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
        when (requestCode) {
            AddNoteRequest, UpdateNoteRequest -> {
                handleAddOrUpdateNoteResult(requestCode,resultCode, data)
            }
            AddTaskRequest, UpdateTaskRequest -> {
                handleAddOrUpdateTaskResult(requestCode,resultCode, data)
            }
        }
    }
    private fun handleAddOrUpdateNoteResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val isUpdate = data?.getBooleanExtra("isUpdate", false) ?: false
            if (requestCode == UpdateNoteRequest) {
                // Handle update logic for notes
                val updatedNote = data?.getSerializableExtra("note") as? NoteModel
                if (updatedNote != null) {
                    Log.d("NoteUpdate", "Updating note: $updatedNote")
                    notesAdapter.updateNote(updatedNote)
                    noteList[requestCode] = updatedNote
                    notesRecyclerView.adapter?.notifyItemChanged(requestCode)
                } else {
                    Log.e("NoteUpdate", "Updated note is null.")
                }
            } else {
                // Handle add logic for notes
                val note = data?.getSerializableExtra("note") as? NoteModel
                if (note != null) {
                    Log.d("NoteAdd", "Adding note: $note")
                    notesAdapter.addNote(note)
                } else {
                    Log.e("NoteAdd", "Added note is null.")
                }
            }
        }
    }
    private fun handleAddOrUpdateTaskResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val isUpdate = data?.getBooleanExtra("isUpdate", false) ?: false
            if (requestCode == UpdateTaskRequest) {
                // Handle update logic for tasks
                val updatedTask = data?.getSerializableExtra("task") as? TaskModel
                if (updatedTask != null) {
                    Log.d("TaskUpdate", "Updating task: $updatedTask")
                    tasksAdapter.updateTask(updatedTask)
                } else {
                    Log.e("TaskUpdate", "Updated task is null.")
                }
            } else {
                // Handle add logic for tasks
                val task = data?.getSerializableExtra("task") as? TaskModel
                if (task != null) {
                    Log.d("TaskAdd", "Adding task: $task")
                    tasksAdapter.addTask(task)
                } else {
                    Log.e("TaskAdd", "Added task is null.")
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