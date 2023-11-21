package com.example.planify

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.planify.NoteModel
import com.example.planify.databinding.ActivityDashboardBinding
import com.example.planify.databinding.ActivityEditNoteBinding

class EditNoteActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var note: NoteModel
    private lateinit var noteList: MutableList<NoteModel>
    private lateinit var noteAdapter: NoteRecyclerView

    // Binding
    private lateinit var binding: ActivityEditNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityEditNoteBinding.inflate(layoutInflater)
        setContentView(this.binding.root)

        noteList = intent.getSerializableExtra("noteList") as? MutableList<NoteModel> ?: mutableListOf()
        noteAdapter = NoteRecyclerView(noteList)

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
        dateEditText = findViewById(R.id.inputEditDate)
        contentEditText = findViewById(R.id.inputEditContent)

        // Get the note that was passed from NoteRecyclerView
        note = intent.getSerializableExtra("note") as NoteModel

        // Populate the EditTexts with the note data
        titleEditText.setText(note.title)
        dateEditText.setText(note.date)
        contentEditText.setText(note.description)

        val saveButton: Button = findViewById(R.id.edit_note_btn)
        saveButton.setOnClickListener {
            // Get the updated note content
            val updatedTitle = titleEditText.text.toString()
            val updatedDate = dateEditText.text.toString()
            val updatedContent = contentEditText.text.toString()

            // Update the note
            note.title = updatedTitle
            note.date = updatedDate
            note.description = updatedContent

            val returnIntent = Intent()
            returnIntent.putExtra("note", note)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }
}