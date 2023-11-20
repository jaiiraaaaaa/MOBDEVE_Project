package com.example.planify

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.planify.databinding.ActivityAddNoteBinding
class AddNoteActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityAddNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logoutBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.dashboardNav.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.calendarNav.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.saveNoteBtn.setOnClickListener {
            val returnIntent = Intent()
            val title = binding.inputTitle.text.toString()
            val date = binding.inputDate.text.toString()
            val content = binding.inputContent.text.toString()

            if (title.isNotEmpty() && date.isNotEmpty() && content.isNotEmpty()) {
                val note = DataNote(title, date, content)
                returnIntent.putExtra("note", note)
                setResult(Activity.RESULT_OK, returnIntent)
            } else {
                setResult(Activity.RESULT_CANCELED)
            }
            finish() // Make sure this line is always executed
        }

    }
}
