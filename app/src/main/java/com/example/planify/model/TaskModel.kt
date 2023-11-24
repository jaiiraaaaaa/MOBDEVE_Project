package com.example.planify.model

import java.io.Serializable

class TaskModel(
    var id: Int,
    var title: String,
    var subject: String,
    var status: String,
    var deadline: String): Serializable{
    companion object {
        private const val DEFAULT_ID = -1
    }
    constructor(title: String, subject: String, status: String, deadline: String) : this(DEFAULT_ID, title, subject, status, deadline)
    constructor() : this(DEFAULT_ID, "Empty","Empty", "Empty", "00/00/00")

}
