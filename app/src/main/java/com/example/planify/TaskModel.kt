package com.example.planify

import java.io.Serializable

data class TaskModel(
    override var id: Int,
    var title: String,
    var subject: String,
    var status: String,
    var deadline: String
): UpdatableModel
