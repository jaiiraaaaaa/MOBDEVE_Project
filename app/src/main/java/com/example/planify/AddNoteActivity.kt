package com.example.planify

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.example.planify.databinding.ActivityAddNoteBinding

class AddNoteActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var notesAdapter: NoteRecyclerView
    private lateinit var noteList: MutableList<NoteModel>
    private var isUpdate: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noteList = intent.getSerializableExtra("noteList") as? MutableList<NoteModel> ?: mutableListOf()
        notesAdapter = NoteRecyclerView(noteList)
        // Inside onCreate()
        val noteToUpdate = intent.getSerializableExtra("noteToUpdate") as? NoteModel
        if (noteToUpdate != null) {
            // This means we are updating an existing note
            // Populate the UI with existing note data
            binding.inputTitle.setText(noteToUpdate.title)
            binding.inputDate.setText(noteToUpdate.date)
            binding.inputContent.setText(noteToUpdate.description)
        }

// ... the rest of your code ...

        binding.logoutBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity:: class.java))
            finish()
        }

        binding.dashboardNav.setOnClickListener (View.OnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        })

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
        // Inside binding.addNoteBtn.setOnClickListener()
        binding.addNoteBtn.setOnClickListener {
            val returnIntent = Intent()
            val title = binding.inputTitle.text.toString()
            val date = binding.inputDate.text.toString()
            val content = binding.inputContent.text.toString()

            if (title.isNotEmpty() && date.isNotEmpty() && content.isNotEmpty()) {
                val note = NoteModel(notesAdapter.returnIdCount(), title, date, content)

                // Set result code and extras based on whether it's an update or add
                if (isUpdate) {
                    returnIntent.putExtra("isUpdate", true)
                    returnIntent.putExtra("note", note)
                } else {
                    returnIntent.putExtra("note", note)
                }

                setResult(Activity.RESULT_OK, returnIntent)
            } else {
                setResult(Activity.RESULT_CANCELED)
            }
            finish()
        }
    }
}