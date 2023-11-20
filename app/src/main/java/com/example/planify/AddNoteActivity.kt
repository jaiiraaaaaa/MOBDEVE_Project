package com.example.planify

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.planify.databinding.ActivityAddNoteBinding

class AddNoteActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var notesAdapter: NoteRecyclerView
    private lateinit var noteList: MutableList<NoteModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noteList = intent.getSerializableExtra("noteList") as? MutableList<NoteModel> ?: mutableListOf()
        notesAdapter = NoteRecyclerView(noteList)

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
            var bFlag = true

            if (title.isNotEmpty() && date.isNotEmpty() && content.isNotEmpty()) {
                val note = NoteModel(notesAdapter.returnIdCount(),title, date, content)
                returnIntent.putExtra("note", note)
                setResult(Activity.RESULT_OK, returnIntent)
            } else {
                bFlag = false
                setResult(Activity.RESULT_CANCELED)
            }
            if(!bFlag) {
                Snackbar.make(
                    binding.root,
                    "Please fill in all fields",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            finish()
        }
    }
}