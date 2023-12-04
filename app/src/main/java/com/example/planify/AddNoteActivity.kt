package com.example.planify

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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

    // Format for MM/DD/YY
    private val dateFormat = SimpleDateFormat("MM/dd/yy", Locale.US)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val noteDatabase = NoteDatabase(applicationContext)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noteList = intent.getSerializableExtra("noteList") as? MutableList<NoteModel> ?: mutableListOf()
        notesAdapter = NoteRecyclerView(noteDatabase.getNoteList())

        // Autofill the current date
        val currentDate = dateFormat.format(Date())
        binding.inputDate.setText(currentDate)

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

            // Perform error checking
            if (title.isBlank()) {
                // Show a toast warning for blank title
                Toast.makeText(this, "Title is required", Toast.LENGTH_SHORT).show()
            } else if (date.isBlank() || !isValidDateFormat(date)) {
                // Show a toast warning for invalid or blank date format
                Toast.makeText(this, "Invalid date format. Please use MM/DD/YY", Toast.LENGTH_SHORT).show()
            } else {
                // Continue with creating NoteModel and returning the result
                val note = NoteModel(title, content, currentDate)
                returnIntent.putExtra("note", note)
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