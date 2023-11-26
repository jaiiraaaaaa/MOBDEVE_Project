package com.example.planify.model

import java.io.Serializable

data class NoteModel(
    var id: Int,
    var title: String,
    var description: String,
    var date: String, ) : Serializable {
    companion object {
        private const val DEFAULT_ID = -1
    }
    constructor(title: String, description: String, date: String) : this(DEFAULT_ID, title, description, date)
    constructor() : this(DEFAULT_ID, "Empty", "Empty", "00/00/00")
}



