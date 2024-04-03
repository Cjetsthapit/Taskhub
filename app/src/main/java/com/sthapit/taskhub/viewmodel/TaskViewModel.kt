package com.sthapit.taskhub

import com.sthapit.taskhub.models.Task
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TaskViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

//     fun fetchTasks(userId:String) {
//         if (userId == null) {
//             Log.e(TAG, "User ID is null.")
//             _tasks.value = emptyList() // Clear the tasks or handle this scenario as needed
//             return
//         }
//         Log.e("Fetching", userId)
//         db.collection("users").document(userId).collection("tasks")
//             .get()
//             .addOnSuccessListener { result ->
//                 for (document in result) {
//                     Log.d("TaskListing", "${document.id} => ${document.data}")
//                 }
//             }
//    }
suspend fun fetchTasks(userId: String) {
    if (userId.isEmpty()) {
        Log.e(TAG, "User ID is null or empty.")
        _tasks.value = emptyList() // Clear the tasks or handle this scenario as needed
        return
    }
    Log.e("Fetching", userId)
    try {
        val result = db.collection("users").document(userId).collection("tasks")
            .get()
            .await() // Use Kotlin coroutines to await the result without blocking

        val fetchedTasks = result.documents.mapNotNull { document ->
            try {
                // Map the Firestore document to the Task object
                Task(
                    id = document.id,
                    name = document.getString("name") ?: "",
                    status = document.getBoolean("status") ?: false,
                    timeEstimation = document.getString("timeEstimation") ?: ""
                )
            } catch (e: Exception) {
                Log.e("TaskParsing", "Error parsing task ${document.id}", e)
                null
            }
        }

        _tasks.value = fetchedTasks
    } catch (e: Exception) {
        Log.e(TAG, "Error fetching tasks for user $userId", e)
        _tasks.value = emptyList() // Optionally, handle errors differently
    }
}

     fun addTask(task: Task, userId: String) {
        viewModelScope.launch {
            val taskRef = db.collection("users").document(userId).collection("tasks").document(task.id)

            taskRef.set(task)
                .addOnSuccessListener { Log.d(TAG, "Task added to user $userId subcollection") }
                .addOnFailureListener { e -> Log.w(TAG, "Error adding task", e) }

        }


    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
//            db.collection("tasks").document(task.id.toString()).set(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
//            db.collection("tasks").document(task.id.toString()).delete()
        }
    }
}
