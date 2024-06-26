package com.example.planify

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.planify.database.TaskDatabase
import com.example.planify.model.TaskModel
import java.util.Locale

class TaskEditableRecyclerView(private val tasksList: ArrayList<TaskModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val UpdateTaskRequest = 4

    // Interface to handle delete click events
    interface OnTaskDeleteClickListener {
        fun onTaskDeleteClick(task: TaskModel, position: Int)
    }
    private var onTaskDeleteClickListener: OnTaskDeleteClickListener? = null
    fun setOnTaskDeleteClickListener(listener: OnTaskDeleteClickListener) {
        this.onTaskDeleteClickListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_task_editable_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val task = tasksList[position]
        val titleTextView = holder.itemView.findViewById<TextView>(R.id.editable_task_title)
        val subjectTextView = holder.itemView.findViewById<TextView>(R.id.editable_task_subject)
        val statusTextView = holder.itemView.findViewById<TextView>(R.id.editable_task_status)
        val deadlineTextView = holder.itemView.findViewById<TextView>(R.id.editable_task_deadline)
        val editButton = holder.itemView.findViewById<ImageButton>(R.id.task_edit_btn)
        val deleteButton = holder.itemView.findViewById<ImageButton>(R.id.task_delete_btn)

        titleTextView.text = task.title
        subjectTextView.text = task.subject
        statusTextView.text = task.status
        deadlineTextView.text = task.deadline

        setStatusBackground(statusTextView, task.status)

        editButton.setOnClickListener {
            val intent = Intent(it.context, EditTaskActivity::class.java)
            if(position >= 0) {
                intent.putExtra("taskPosition", position)
                intent.putExtra("task", task)
                // Update task request => EditTaskActivity which handles both updating and deleting the task
                (it.context as Activity).startActivityForResult(intent, UpdateTaskRequest)
            }

        }
        deleteButton.setOnClickListener {
            onTaskDeleteClickListener?.onTaskDeleteClick(task, position)
        }
    }
    override fun getItemCount(): Int {
        return tasksList.size
    }

    private fun setStatusBackground(textView: TextView, status: String) {
        when (status.lowercase(Locale.getDefault())) {
            "todo" -> textView.setBackgroundResource(R.drawable.tasks_todo_cell_bg)
            "in progress" -> textView.setBackgroundResource(R.drawable.tasks_in_progress_cell_bg)
            "completed" -> textView.setBackgroundResource(R.drawable.tasks_completed_cell_bg)
            else -> textView.setBackgroundResource(R.drawable.tasks_body_cell_bg) // Default background
        }
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.editable_task_title)
        val subjectTextView: TextView = view.findViewById(R.id.editable_task_subject)
        val statusTextView: TextView = view.findViewById(R.id.editable_task_status)
        val deadlineTextView: TextView = view.findViewById(R.id.editable_task_deadline)
    }
    fun returnIdCount(): Int {
        return tasksList.size + 1
    }
    fun addTask(task: TaskModel) {
        tasksList.add(task)
        notifyDataSetChanged()
    }

    fun updateTask(updatedTask: TaskModel) {
        val index = tasksList.indexOfFirst { it.id == updatedTask.id }
        if (index != -1) {
            tasksList[index] = updatedTask
            notifyItemChanged(index)
            notifyDataSetChanged() // Make sure to call notifyDataSetChanged
        }
    }
    fun removeTask(position: Int) {
        tasksList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }
}