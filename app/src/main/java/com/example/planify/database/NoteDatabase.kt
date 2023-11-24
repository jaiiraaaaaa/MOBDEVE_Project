package com.example.planify.database

import android.content.ContentValues
import android.content.Context
import com.example.planify.NoteModel

class NoteDatabase(context: Context) {

    private lateinit var databaseHandler: DatabaseHandler

    init {
        this.databaseHandler = DatabaseHandler(context)
    }

    fun addTask(note: NoteModel) : Int {
        val db = databaseHandler.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(DatabaseHandler.NOTE_TABLE, note.title)

        val _id = db.insert(DatabaseHandler.NOTE_TABLE,null, contentValues)
        db.close()

        return _id.toInt()
    }
    fun updateTask(note: NoteModel) {
        val db = databaseHandler.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(DatabaseHandler.NOTE_TITLE, note.title)
        contentValues.put(DatabaseHandler.NOTE_DESCRIPTION, note.description)
        contentValues.put(DatabaseHandler.NOTE_DATE, note.date)
        val whereArgs = arrayOf(note.id.toString())
        db.update(DatabaseHandler.TASK_TABLE, contentValues, "${DatabaseHandler.NOTE_ID}=?", whereArgs)

        db.close()
    }
    fun deleteTask(note: NoteModel) {
        val db = databaseHandler.writableDatabase

        val whereArgs = arrayOf(note.id.toString())
        db.delete(DatabaseHandler.TASK_TABLE, "${DatabaseHandler.NOTE_ID}=?", whereArgs)
        db.close()
    }

}