package com.example.planify

import java.util.Date

data class TaskModel(
    val id: Int,
    val title: String,
    val subject: String,
    val status: String,
    val deadline: String
)
