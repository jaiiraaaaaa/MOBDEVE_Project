package com.example.planify

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planify.databinding.ActivityTasksNoteBinding

class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(note: NoteModel) {
        val titleTextView = itemView.findViewById<TextView>(R.id.note_name)
        titleTextView.text = note.title
    }
}
class NoteAdapter(private val notes: List<NoteModel>) : RecyclerView.Adapter<NoteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_notes_row, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note)
    }
    override fun getItemCount(): Int {
        return notes.size
    }
}

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
    private var tasks = mutableListOf<TaskModel>()

    // Notes vars
    private lateinit var notesRecyclerView: RecyclerView
    private lateinit var notesAdapter: NoteRecyclerView
    private var notes = mutableListOf<NoteModel>()

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
            startActivity(intent)
            finish()
        }

        binding.addTaskBtn.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Tasks Recycler View
        tasks = generateSampleTasks().toMutableList()
        tasksRecyclerView = findViewById(R.id.recycleTasks)
        tasksAdapter = TaskEditableRecyclerView(tasks)
        tasksRecyclerView.adapter = tasksAdapter
        tasksRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // Notes Recycler View
        notes = generateSampleNotes().toMutableList()
        notesRecyclerView = findViewById(R.id.recycleNotes)
        notesAdapter = NoteRecyclerView(notes)
        notesRecyclerView.adapter = notesAdapter
        notesRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

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