package com.example.planify

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.planify.databinding.ActivityEditNoteBinding
import com.example.planify.databinding.ActivityEditTaskBinding
import com.example.planify.model.NoteModel

class EditNoteActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var note: NoteModel
    private lateinit var binding: ActivityEditNoteBinding
    private val UpdateNoteRequest = 3
    private var notePosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityEditNoteBinding.inflate(layoutInflater)
        setContentView(this.binding.root)

        notePosition = intent.getIntExtra("notePosition", -1)

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
        dateEditText = findViewById(R.id.inputEditDate)
        contentEditText = findViewById(R.id.inputEditContent)

        note = intent.getSerializableExtra("note") as NoteModel

        titleEditText.setText(note.title)
        dateEditText.setText(note.date)
        contentEditText.setText(note.description)

        val saveButton: Button = findViewById(R.id.edit_note_btn)
        saveButton.setOnClickListener {
            // Get the updated note content
            val updatedTitle = titleEditText.text.toString()
            val updatedContent = contentEditText.text.toString()

            if (updatedTitle.isBlank()) {
                // Show a toast warning for blank title
                Toast.makeText(this, "Title is required", Toast.LENGTH_SHORT).show()
            } else {
                // Update the note
                note.title = updatedTitle
                note.description = updatedContent

                // Notify the user that the note has been edited
                Toast.makeText(this, "Note edited successfully", Toast.LENGTH_SHORT).show()

                val returnIntent = Intent()
                returnIntent.putExtra("note", note)

                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }

        }
        val deleteButton: Button = findViewById(R.id.delete_note_btn)
        deleteButton.setOnClickListener {
            showDeleteNoteDialog()
        }
    }
    // Dialog for deleting the currently edited note
    private fun showDeleteNoteDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Delete Note")
        alertDialogBuilder.setMessage("Are you sure you want to delete this note?")
        alertDialogBuilder.setPositiveButton("Delete") { _, _ ->

            // Notify the user that the note has been deleted
            Toast.makeText(this, "Note deleted successfully", Toast.LENGTH_SHORT).show()

            // Notify the calling activity that the task should be deleted
            val returnIntent = Intent()
            returnIntent.putExtra("deleteNote", true)
            returnIntent.putExtra("notePosition", notePosition)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
        alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialogBuilder.show()
    }
}