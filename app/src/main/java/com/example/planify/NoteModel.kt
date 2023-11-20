package com.example.planify

import java.io.Serializable

data class NoteModel(
    val id: Int,
    val title: String,
    val description: String,
    val date: String,
) : Serializable
