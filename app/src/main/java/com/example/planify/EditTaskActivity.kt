package com.example.planify

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import android.widget.RadioGroup
import android.widget.Toast
import com.example.planify.databinding.ActivityEditTaskBinding
import com.example.planify.model.TaskModel

class EditTaskActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var categoryEditText: EditText
    private lateinit var radioGroup: RadioGroup
    private lateinit var deadline: EditText
    private lateinit var task: TaskModel
    private lateinit var binding: ActivityEditTaskBinding
    private var taskPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityEditTaskBinding.inflate(layoutInflater)
        setContentView(this.binding.root)
        taskPosition = intent.getIntExtra("taskPosition", -1)

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

        titleEditText = findViewById(R.id.inputEditTitle)
        categoryEditText = findViewById(R.id.inputEditCategory)
        radioGroup = findViewById(R.id.statusRadioGroup)
        deadline = findViewById(R.id.inputEditDeadline)

        // Get the task that was passed from TaskRecyclerView
        task = intent.getSerializableExtra("task") as TaskModel

        // Populate the EditTexts with the task data
        titleEditText.setText(task.title)
        categoryEditText.setText(task.subject)
        deadline.setText(task.deadline)

        // Check the appropriate radio button based on the task deadline
        when (task.status) {
            "Todo" -> radioGroup.check(R.id.radioButtonTodo)
            "In Progress" -> radioGroup.check(R.id.radioButtonInProgress)
            "Completed" -> radioGroup.check(R.id.radioButtonCompleted)
        }

        // Save functionality

        val saveButton: Button = findViewById(R.id.edit_task_btn)
        saveButton.setOnClickListener {
            // Get the updated task content
            val updatedTitle = titleEditText.text.toString()
            val updatedCategory = categoryEditText.text.toString()
            val updatedDeadline = deadline.text.toString()

            // Get the selected radio button from the RadioGroup
            val selectedId = radioGroup.checkedRadioButtonId
            val radioButton = findViewById<RadioButton>(selectedId)
            val updatedStatus = radioButton.text.toString()

            // Check for empty fields
            if (updatedTitle.isBlank() || updatedCategory.isBlank() || updatedDeadline.isBlank()) {
                // Show a toast warning for empty fields
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            } else {
                // Check for the MM/DD/YY format for the deadline
                if (!isValidDateFormat(updatedDeadline)) {
                    // Show a toast warning for invalid date format
                    Toast.makeText(this, "Deadline must be in MM/DD/YY format", Toast.LENGTH_SHORT).show()
                } else {
                    // Update the task
                    task.title = updatedTitle
                    task.subject = updatedCategory
                    task.deadline = updatedDeadline
                    task.status = updatedStatus

                    // Notify the user that the task has been updated
                    Toast.makeText(this, "Task updated successfully", Toast.LENGTH_SHORT).show()

                    val returnIntent = Intent()
                    returnIntent.putExtra("task", task)

                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                }
            }
        }

        // Delete functionality

        val deleteButton: Button = findViewById(R.id.delete_task_btn)
        deleteButton.setOnClickListener {
            showDeleteTaskDialog()
        }
    }

    // Dialog for deleting the currently edited task
    private fun showDeleteTaskDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Delete Task")
        alertDialogBuilder.setMessage("Are you sure you want to delete this task?")
        alertDialogBuilder.setPositiveButton("Delete") { _, _ ->

            // Notify the user that the note has been deleted
            Toast.makeText(this, "Task deleted successfully", Toast.LENGTH_SHORT).show()

            // Notify the calling activity that the task should be deleted
            val returnIntent = Intent()
            returnIntent.putExtra("deleteTask", true)
            returnIntent.putExtra("taskPosition", taskPosition)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
        alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialogBuilder.show()
    }

    // Function to check if the date has a valid MM/DD/YY format
    private fun isValidDateFormat(date: String): Boolean {
        val regex = Regex("""^(0[1-9]|1[0-2])/(0[1-9]|[1-2][0-9]|3[0-1])/\d{2}$""")
        return regex.matches(date)
    }
}
