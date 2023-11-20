package com.example.planify

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.planify.NoteModel

class EditNoteActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var note: NoteModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)

        titleEditText = findViewById(R.id.inputEditTitle)
        dateEditText = findViewById(R.id.inputEditDate)
        contentEditText = findViewById(R.id.inputEditContent)

        // Get the note that was passed from NoteRecyclerView
        note = intent.getSerializableExtra("note") as NoteModel

        // Populate the EditTexts with the note data
        titleEditText.setText(note.title)
        dateEditText.setText(note.date)
        contentEditText.setText(note.description)

        val saveButton: Button = findViewById(R.id.save_edit_btn)
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
            returnIntent.putExtra("isUpdate", true)
            returnIntent.putExtra("note", note)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }

    }
}
