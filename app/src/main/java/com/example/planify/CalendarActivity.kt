package com.example.planify

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planify.database.TaskDatabase
import com.example.planify.databinding.ActivityCalendarBinding
import com.example.planify.model.TaskModel

class CalendarActivity : AppCompatActivity() {

    // Tasks Recycler View vars
    private lateinit var tasksRecyclerView: RecyclerView
    private lateinit var tasksAdapter: TaskNotEditRecyclerView
    private var tasks = ArrayList<TaskModel>()

    // Binding
    private lateinit var binding: ActivityCalendarBinding
    private lateinit var taskDatabase: TaskDatabase
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
        taskDatabase = TaskDatabase(applicationContext)
        tasks = taskDatabase.getTaskList()
        tasksRecyclerView = findViewById(R.id.recycleTasksCalendar)
        tasksAdapter = TaskNotEditRecyclerView(tasks)
        tasksRecyclerView.adapter = tasksAdapter
        Log.d("AdapterData", tasksAdapter.itemCount.toString())
        tasksRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }
}