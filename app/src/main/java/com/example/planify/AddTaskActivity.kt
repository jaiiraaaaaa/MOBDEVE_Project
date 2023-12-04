package com.example.planify

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.planify.database.TaskDatabase
import com.example.planify.databinding.ActivityAddTaskBinding
import com.example.planify.model.NoteModel
import com.example.planify.model.TaskModel

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding
    private lateinit var taskList: MutableList<TaskModel>
    private lateinit var taskAdapter: TaskEditableRecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val taskDatabase = TaskDatabase(applicationContext)

        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        taskList = (intent.getSerializableExtra("taskList") as? MutableList<NoteModel> ?: mutableListOf()) as MutableList<TaskModel>
        taskAdapter = TaskEditableRecyclerView(taskDatabase.getTaskList())

        binding.dashboardNav.setOnClickListener {
            startActivity(Intent(this, DashboardActivity:: class.java))
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

        binding.saveTaskBtn.setOnClickListener {
            val returnIntent = Intent()

            // Retrieve input values
            val title = binding.inputTaskTitle.text.toString()
            val category = binding.inputTaskCategory.text.toString()
            val selectedId = binding.statusRadioGroup.checkedRadioButtonId
            val radioButton = findViewById<RadioButton>(selectedId)
            val status = radioButton.text.toString()
            val deadline = binding.inputTaskDeadline.text.toString()

            // Perform error checking
            if (title.isBlank() || category.isBlank() || status.isBlank() || deadline.isBlank()) {
                // Show a toast warning for empty fields
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            } else if (!isValidDateFormat(deadline)) {
                // Show a toast warning for invalid date format
                Toast.makeText(this, "Invalid deadline format. Please use MM/DD/YY", Toast.LENGTH_SHORT).show()
            } else {
                // Continue with creating TaskModel and returning the result
                Toast.makeText(this, "Task added successfully", Toast.LENGTH_SHORT).show()

                val task = TaskModel(title, category, status, deadline)
                returnIntent.putExtra("task", task)
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
        }
    }

    // Function to check if the date has a valid MM/DD/YY format
    private fun isValidDateFormat(date: String): Boolean {
        val regex = Regex("""^(0[1-9]|1[0-2])/(0[1-9]|[1-2][0-9]|3[0-1])/\d{2}$""")
        return regex.matches(date)
    }
}
