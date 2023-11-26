package com.example.planify
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planify.database.TaskDatabase
import com.example.planify.databinding.ActivityCalendarBinding
import com.example.planify.model.TaskModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CalendarActivity : AppCompatActivity() {

    // Tasks Recycler View vars
    private lateinit var tasksRecyclerView: RecyclerView
    private lateinit var tasksAdapter: TaskNotEditRecyclerView
    private var tasks = ArrayList<TaskModel>()

    // Binding
    private lateinit var binding: ActivityCalendarBinding
    private lateinit var taskDatabase: TaskDatabase
    private fun String.toDateFormat(format: String): String {
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        val date = sdf.parse(this) ?: Date()
        return SimpleDateFormat("MM/dd/yy", Locale.getDefault()).format(date)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logoutBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.taskNav.setOnClickListener(View.OnClickListener {
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
        Log.d("Tasks", tasks.joinToString("\n"))
        tasksRecyclerView = findViewById(R.id.recycleTasksCalendar)
        tasksAdapter = TaskNotEditRecyclerView(tasks)
        tasksRecyclerView.adapter = tasksAdapter
        Log.d("AdapterData", tasksAdapter.itemCount.toString())
        tasksRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // Initialize the options and the ArrayAdapter
        val options = arrayOf("View by Date", "View All")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, options)

        // Set the adapter for the AutoCompleteTextView
        binding.calendarSortBtn.setAdapter(adapter)

        // Set an item click listener for the AutoCompleteTextView
        binding.calendarSortBtn.setOnItemClickListener { parent, view, position, id ->
            when (parent.getItemAtPosition(position) as String) {
                "View by Date" -> {
                    // Set the OnDateChangeListener to filter tasks by the selected date
                    binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
                        val selectedDate = "${month + 1}/${dayOfMonth}/${year.toString().takeLast(2)}"
                        val filteredTasks = tasks.filter {
                            it.deadline.toDateFormat("MM/dd/yy") == selectedDate
                        }
                        tasksAdapter.updateTasks(filteredTasks)
                    }
                }
                "View All" -> {
                    // Remove the OnDateChangeListener and show all tasks
                    binding.calendarView.setOnDateChangeListener(null)
                    tasksAdapter.updateTasks(tasks)
                }
            }
        }

    }
}