package com.example.planify

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.example.planify.database.NoteDatabase
import com.example.planify.databinding.ActivityAddNoteBinding
import com.example.planify.model.NoteModel

class AddNoteActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var notesAdapter: NoteRecyclerView
    private lateinit var noteList: MutableList<NoteModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val noteDatabase = NoteDatabase(applicationContext)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noteList = intent.getSerializableExtra("noteList") as? MutableList<NoteModel> ?: mutableListOf()
        notesAdapter = NoteRecyclerView(noteDatabase.getNoteList())

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
        binding.addNoteBtn.setOnClickListener() {
            val returnIntent = Intent()
            val title = binding.inputTitle.text.toString()
            val date = binding.inputDate.text.toString()
            val content = binding.inputContent.text.toString()

            val note = NoteModel(title, content, date)
            returnIntent.putExtra("note", note)
            setResult(Activity.RESULT_OK, returnIntent)

            finish()
        }
    }
}