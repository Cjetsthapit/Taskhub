package com.sthapit.taskhub

import com.sthapit.taskhub.models.Task
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TaskViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

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
            try{

            val taskRef = db.collection("users").document(userId).collection("tasks").document(task.id)

            taskRef.set(task)
                .addOnSuccessListener { Log.d(TAG, "Task added to user $userId subcollection") }
                .addOnFailureListener { e -> Log.w(TAG, "Error adding task", e) }
                fetchTasks(userId)
            } catch (e: Exception){
                Log.e("TaskViewModel", "Error adding task", e)
            }

        }


    }

    fun updateTask(task: Task, userId: String) {
        viewModelScope.launch {
            try {
                val taskRef = db.collection("users").document(userId).collection("tasks").document(task.id)
                taskRef.set(task, SetOptions.merge())
                    .addOnSuccessListener { Log.d(TAG, "Task successfully updated") }
                    .addOnFailureListener { e -> Log.e(TAG, "Error updating task", e) }
                fetchTasks(userId)
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Error updating task", e)
            }
        }
    }

    fun deleteTask(task: Task, userId: String) {
        viewModelScope.launch {
            try {
                val taskRef = db.collection("users").document(userId).collection("tasks").document(task.id)
                taskRef.delete()
                    .addOnSuccessListener { Log.d(TAG, "Task successfully deleted") }
                    .addOnFailureListener { e -> Log.e(TAG, "Error deleting task", e) }
                fetchTasks(userId)
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Error deleting task", e)
            }
        }
    }
}
