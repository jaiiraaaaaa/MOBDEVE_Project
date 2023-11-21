package com.example.planify

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import android.widget.RadioGroup
import com.example.planify.databinding.ActivityEditTaskBinding

class EditTaskActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var categoryEditText: EditText
    private lateinit var deadlineRadioGroup: RadioGroup
    private lateinit var task: TaskModel
    private lateinit var binding: ActivityEditTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityEditTaskBinding.inflate(layoutInflater)
        setContentView(this.binding.root)

        binding.dashboardNav.setOnClickListener {
            startActivity(Intent(this, DashboardActivity:: class.java))
            finish()
        }

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

        titleEditText = findViewById(R.id.inputEditTitle)
        categoryEditText = findViewById(R.id.inputEditCategory)
        deadlineRadioGroup = findViewById(R.id.statusRadioGroup)

        // Get the task that was passed from TaskRecyclerView
        task = intent.getSerializableExtra("task") as TaskModel

        // Populate the EditTexts with the task data
        titleEditText.setText(task.title)
        categoryEditText.setText(task.subject)

        // Check the appropriate radio button based on the task deadline
        when (task.deadline) {
            "Todo" -> deadlineRadioGroup.check(R.id.radioButtonTodo)
            "In Progress" -> deadlineRadioGroup.check(R.id.radioButtonInProgress)
            "Completed" -> deadlineRadioGroup.check(R.id.radioButtonCompleted)
        }

        val saveButton: Button = findViewById(R.id.edit_task_btn)
        saveButton.setOnClickListener {
            // Get the updated task content
            val updatedTitle = titleEditText.text.toString()
            val updatedCategory = categoryEditText.text.toString()

            // Get the selected radio button from the RadioGroup
            val selectedId = deadlineRadioGroup.checkedRadioButtonId
            val radioButton = findViewById<RadioButton>(selectedId)
            val updatedDeadline = radioButton.text.toString()

            // Update the task
            task.title = updatedTitle
            task.subject = updatedCategory
            task.deadline = updatedDeadline

            val returnIntent = Intent()
            returnIntent.putExtra("task", task)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }
}
