package com.example.planify.database

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import com.example.planify.model.TaskModel

class TaskDatabase(context: Context) {

    private lateinit var databaseHandler: DatabaseHandler

    init {
        this.databaseHandler = DatabaseHandler(context)
    }

    fun addTask(task: TaskModel) : Int {
        val db = databaseHandler.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(DatabaseHandler.TASK_TITLE, task.title)
        contentValues.put(DatabaseHandler.TASK_SUBJECT, task.subject)
        contentValues.put(DatabaseHandler.TASK_STATUS, task.status)
        contentValues.put(DatabaseHandler.TASK_DEADLINE, task.deadline)

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
    fun getTaskList(): ArrayList<TaskModel>{
        val result = ArrayList<TaskModel>()
        val db = databaseHandler.readableDatabase
        val fields = arrayOf(
            DatabaseHandler.TASK_ID,
            DatabaseHandler.TASK_TITLE,
            DatabaseHandler.TASK_SUBJECT,
            DatabaseHandler.TASK_STATUS,
            DatabaseHandler.TASK_DEADLINE
        )
        val cursor = db.query("${DatabaseHandler.TASK_TABLE}", fields, null, null, null, null, null)
        Log.d("Count", "${cursor.count}")
        if(cursor.count > 0){
            if(cursor.moveToFirst()){
                do{
                    val taskID= cursor.getIntOrNull(cursor.getColumnIndex("${DatabaseHandler.TASK_ID}"))
                    val taskTitle= cursor.getStringOrNull(cursor.getColumnIndex("${DatabaseHandler.TASK_TITLE}"))
                    val taskSubject= cursor.getStringOrNull(cursor.getColumnIndex("${DatabaseHandler.TASK_SUBJECT}"))
                    val taskStatus= cursor.getStringOrNull(cursor.getColumnIndex("${DatabaseHandler.TASK_STATUS}"))
                    val taskDeadline= cursor.getStringOrNull(cursor.getColumnIndex("${DatabaseHandler.TASK_DEADLINE}"))
                    if (taskID != null && taskTitle != null && taskSubject != null && taskStatus != null && taskDeadline != null) {
                        result.add(TaskModel(taskID, taskTitle, taskSubject, taskStatus, taskDeadline))
                    } else {
                        // Handle the case where one of the values is null
                        Log.e("TaskDatabase", "One or more values retrieved from the database is null.")
                    }
                }while(cursor.moveToNext())
            }
        }
        cursor.close()
        return result
    }
}