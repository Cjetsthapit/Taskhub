package com.sthapit.taskhub.models

import java.util.UUID

//data class com.sthapit.taskhub.models.Task(
//    var id: String = UUID.randomUUID().toString(),
//    var name: String,
//    var status: Boolean, // true for Done, false for To Do
//    var timeEstimation: String
//)

data class Task(
    var id: String = "",
    val name: String = "",
    val status: Boolean = false,
    val timeEstimation: String = ""
)