package com.example.planify

import java.io.Serializable
import java.util.Date

data class DataNote(
    val title: String,
    val date: String,
    val content: String,
    ) : Serializable
