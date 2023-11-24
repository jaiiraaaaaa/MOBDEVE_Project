package com.example.planify.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "TasksNotesDatabase"

        // Tasks Table
        const val TASK_TABLE = "tasks_table"

        const val TASK_ID = "id"
        const val TASK_TITLE = "task_title"
        const val TASK_SUBJECT = "task_subject"
        const val TASK_STATUS = "task_status"
        const val TASK_DEADLINE ="task_deadline"

        // Notes Table
        const val NOTE_TABLE = "tasks_table"

        const val NOTE_ID = "id"
        const val NOTE_TITLE = "note_title"
        const val NOTE_DESCRIPTION = "note_description"
        const val NOTE_DATE ="note_date"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TASK_TABLE = "CREATE TABLE $TASK_TABLE " +
                "(" +
                "	$TASK_ID INTEGER PRIMARY KEY AUTOINCREMENT" +
                "	,$TASK_TITLE TEXT" +
                "	,$TASK_SUBJECT TEXT" +
                "	,$TASK_STATUS TEXT" +
                "   ,$TASK_DEADLINE TEXT" +
                ")"
        db?.execSQL(CREATE_TASK_TABLE)

        val CREATE_NOTE_TABLE = "CREATE TABLE $NOTE_TABLE " +
                "(" +
                "	$NOTE_ID INTEGER PRIMARY KEY AUTOINCREMENT" +
                "	,$NOTE_TITLE TEXT" +
                "	,$NOTE_DESCRIPTION TEXT" +
                "	,$NOTE_DATE TEXT" +
                ")"
        db?.execSQL(CREATE_NOTE_TABLE)

        val INSERT_SAMPLE_TASK_DATA = "INSERT INTO $TASK_TABLE VALUES" +
                "(1, 'Project3', 'MOBDEVE', 'In progress', '11/20/23')" +
                ",(2, 'MCO2', 'CSOPESY', 'Todo', '11/20/23')" +
                ",(3, 'Final Proj', 'STINTSY', 'Todo', '12/20/23')" +
                ",(4, 'Notebook', 'STINTSY', 'Completed', '11/20/23')"
        db?.execSQL(INSERT_SAMPLE_TASK_DATA)

        val INSERT_SAMPLE_NOTE_DATA = "INSERT INTO $NOTE_TABLE VALUES" +
                "(1, 'Main Memory', 'Sample Description of Note 1', '11/20/23')" +
                ",(2, 'MP Specs', 'Sample Description of Note 2', '11/20/23')" +
                ",(3, 'Deadlines', 'Sample Description of Note 2', '12/20/23')"
        db?.execSQL(INSERT_SAMPLE_NOTE_DATA)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TASK_TABLE")
        db!!.execSQL("DROP TABLE IF EXISTS $NOTE_TABLE")
        onCreate(db)
    }
}