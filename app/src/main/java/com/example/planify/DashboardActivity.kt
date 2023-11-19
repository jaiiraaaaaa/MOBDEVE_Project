package com.example.planify

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.planify.databinding.ActivityDashboardBinding
import com.example.planify.databinding.ActivityMainBinding

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(this.binding.root)


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

    }
}