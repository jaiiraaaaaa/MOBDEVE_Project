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
import com.example.planify.databinding.ActivityDashboardBinding
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class TaskNotEditViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(task: TaskModel) {
        val titleTextView = itemView.findViewById<TextView>(R.id.task_name)
        titleTextView.text = task.title
    }
}
class TaskNotEditAdapter(private val tasks: List<TaskModel>) : RecyclerView.Adapter<TaskNotEditViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskNotEditViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_tasks_row, parent, false)
        return TaskNotEditViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskNotEditViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task)
    }
    override fun getItemCount(): Int {
        return tasks.size
    }
}
class DashboardActivity : AppCompatActivity() {

    // Tasks Recycler View vars
    private lateinit var tasksRecyclerView: RecyclerView
    private lateinit var tasksAdapter: TaskNotEditRecyclerView
    private var tasks = mutableListOf<TaskModel>()

    // Binding
    private lateinit var binding: ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(this.binding.root)

        binding.logoutBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity:: class.java))
            finish()
        }

        binding.taskNav.setOnClickListener (View.OnClickListener {
            val intent = Intent(this, TasksNotesActivity::class.java)
            startActivity(intent)
            finish()
        })

        binding.calendarNav.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Tasks Dashboard Recycler View
        tasks = generateSampleTasks().toMutableList()
        tasksRecyclerView = findViewById(R.id.recycleTasksDashboard)
        tasksAdapter = TaskNotEditRecyclerView(tasks)
        tasksRecyclerView.adapter = tasksAdapter
        tasksRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun generateSampleTasks(): List<TaskModel> {
        return listOf(
            TaskModel(1, "Project3", "MOBDEVE", "In progress", "11/20/23"),
            TaskModel(2,"MCO2", "CSOPESY", "Todo", "11/20/23"),
            TaskModel(3,"Final Proj", "STINTSY", "Todo", "12/20/23"),
        )
    }
}