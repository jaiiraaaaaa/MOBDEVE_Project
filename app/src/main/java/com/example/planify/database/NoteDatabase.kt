package com.example.planify.database

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import com.example.planify.model.NoteModel

class NoteDatabase(context: Context) {

    private lateinit var databaseHandler: DatabaseHandler

    init {
        this.databaseHandler = DatabaseHandler(context)
    }

    fun addNote(note: NoteModel) : Int {
        val db = databaseHandler.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(DatabaseHandler.NOTE_TITLE, note.title)
        contentValues.put(DatabaseHandler.NOTE_DESCRIPTION, note.description)
        contentValues.put(DatabaseHandler.NOTE_DATE, note.date)

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
        db.update(DatabaseHandler.NOTE_TABLE, contentValues, "${DatabaseHandler.NOTE_ID}=?", whereArgs)

        db.close()
    }
    fun deleteNote(note: NoteModel) {
        val db = databaseHandler.writableDatabase

        val whereArgs = arrayOf(note.id.toString())
        db.delete(DatabaseHandler.NOTE_TABLE, "${DatabaseHandler.NOTE_ID}=?", whereArgs)
        db.close()
    }
    fun getNoteList(): ArrayList<NoteModel>{
        val result = ArrayList<NoteModel>()
        val db = databaseHandler.readableDatabase
        val fields = arrayOf(
            DatabaseHandler.NOTE_ID,
            DatabaseHandler.NOTE_TITLE,
            DatabaseHandler.NOTE_DESCRIPTION,
            DatabaseHandler.NOTE_DATE,
        )
        val cursor = db.query("${DatabaseHandler.NOTE_TABLE}", fields, null, null, null, null, null)
        Log.d("Count", "${cursor.count}")
        if(cursor.count > 0){
            if(cursor.moveToFirst()){
                do{
                    val noteID= cursor.getIntOrNull(cursor.getColumnIndex("${DatabaseHandler.NOTE_ID}"))
                    val noteTitle= cursor.getStringOrNull(cursor.getColumnIndex("${DatabaseHandler.NOTE_TITLE}"))
                    val noteDescription= cursor.getStringOrNull(cursor.getColumnIndex("${DatabaseHandler.NOTE_DESCRIPTION}"))
                    val noteDate= cursor.getStringOrNull(cursor.getColumnIndex("${DatabaseHandler.NOTE_DATE}"))
                    result.add(NoteModel(noteID!!, noteTitle!!, noteDescription!!, noteDate!!))
                }while(cursor.moveToNext())
            }
        }
        return result
    }

}