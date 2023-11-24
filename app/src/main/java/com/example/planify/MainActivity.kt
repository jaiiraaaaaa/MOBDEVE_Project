package com.example.planify

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.planify.database.TaskDatabase
import com.example.planify.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var taskAdapter : TaskEditableRecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.mainBtnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            this.startActivity(intent)
        }

        viewBinding.mainBtnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            this.startActivity(intent)
        }

        val tasksDatabase = TaskDatabase(applicationContext)
        taskAdapter = TaskEditableRecyclerView(tasksDatabase.getTaskList())
        Log.d("MainActivity", "taskList.size ${taskAdapter.itemCount}")
    }
}