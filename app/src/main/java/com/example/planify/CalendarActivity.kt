package com.example.planify

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planify.databinding.ActivityCalendarBinding
import com.example.planify.model.TaskModel

class CalendarActivity : AppCompatActivity() {

    // Tasks Recycler View vars
    private lateinit var tasksRecyclerView: RecyclerView
    private lateinit var tasksAdapter: TaskNotEditRecyclerView
    private var tasks = mutableListOf<TaskModel>()

    // Binding
    private lateinit var binding: ActivityCalendarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logoutBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity:: class.java))
            finish()
        }

        binding.taskNav.setOnClickListener (View.OnClickListener {
            val intent = Intent(this, TasksNotesActivity::class.java)
            startActivity(intent)
            finish()
        })

        binding.dashboardNav.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Tasks Calendar Recycler View
        tasks = generateSampleTasks().toMutableList()
        tasksRecyclerView = findViewById(R.id.recycleTasksCalendar)
        tasksAdapter = TaskNotEditRecyclerView(tasks)
        tasksRecyclerView.adapter = tasksAdapter
        Log.d("AdapterData", tasksAdapter.itemCount.toString())
        tasksRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }
    private fun generateSampleTasks(): List<TaskModel> {
        val tasks = listOf(
            TaskModel(1, "Project3", "MOBDEVE", "In progress", "11/20/23"),
            TaskModel(2, "MCO2", "CSOPESY", "Todo", "11/20/23"),
            TaskModel(3, "Final Proj", "STINTSY", "Todo", "12/20/23"),
            TaskModel(4, "Notebook", "STINTSY", "Completed", "11/20/23"),
            TaskModel(5, "Proj", "ITISPRJ", "Completed", "11/20/23"),
            TaskModel(6, "Essay", "GEETHIC", "Todo", "11/20/23"),
            TaskModel(7, "Warmup", "GETEAMS", "In progress", "11/20/23"),
            TaskModel(8, "Record", "LCFILIB", "In progress", "11/20/23"),
        )

        Log.d("SampleTasks", tasks.toString())
        return tasks
    }
}