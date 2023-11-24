package com.example.planify.database

import android.content.ContentValues
import android.content.Context
import com.example.planify.TaskModel

class TaskDatabase(context: Context) {

    private lateinit var databaseHandler: DatabaseHandler

    init {
        this.databaseHandler = DatabaseHandler(context)
    }

    fun addTask(task: TaskModel) : Int {
        val db = databaseHandler.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(DatabaseHandler.TASK_TITLE, task.title)

        val _id = db.insert(DatabaseHandler.TASK_TABLE,null, contentValues)
        db.close()

        return _id.toInt()
    }
    fun updateTask(task: TaskModel) {
        val db = databaseHandler.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(DatabaseHandler.TASK_TITLE, task.title)
        contentValues.put(DatabaseHandler.TASK_SUBJECT, task.subject)
        contentValues.put(DatabaseHandler.TASK_STATUS, task.status)
        contentValues.put(DatabaseHandler.TASK_DEADLINE, task.deadline)
        val whereArgs = arrayOf(task.id.toString())
        db.update(DatabaseHandler.TASK_TABLE, contentValues, "${DatabaseHandler.TASK_ID}=?", whereArgs)

        db.close()
    }
    fun deleteTask(task: TaskModel) {
        val db = databaseHandler.writableDatabase

        val whereArgs = arrayOf(task.id.toString())
        db.delete(DatabaseHandler.TASK_TABLE, "${DatabaseHandler.TASK_ID}=?", whereArgs)
        db.close()
    }

}