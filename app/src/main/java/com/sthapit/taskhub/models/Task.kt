package com.sthapit.taskhub.models


data class Task(
    var id: String = "",
    val name: String = "",
    val status: Boolean = false,
    val timeEstimation: String = ""
)