package com.example.planify

import java.io.Serializable

data class NoteModel(
    var id: Int,
    var title: String,
    var description: String,
    var date: String,
) : Serializable
