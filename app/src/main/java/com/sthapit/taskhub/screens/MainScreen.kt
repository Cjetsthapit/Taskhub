package com.sthapit.taskhub

import AuthViewModel
import com.sthapit.taskhub.models.Task
import android.content.ContentValues.TAG

import android.util.Log

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import java.util.UUID


//@Composable
//fun MainScreen(taskViewModel: TaskViewModel = viewModel(),navController: NavController, authViewModel : AuthViewModel,) {
//    val tasks by taskViewModel.tasks.collectAsState()
//    var showDialog by remember { mutableStateOf(false) }
//    var editingTask by remember { mutableStateOf<Task?>(null) }
//    val userId by authViewModel.userId.collectAsState()
//
//    LaunchedEffect(userId) {
//
//        userId?.let { taskViewModel.fetchTasks(it) }
//    }
//
//    Scaffold(
//        floatingActionButton = {
//            FloatingActionButton(onClick = { editingTask = Task(name = "", status = false, timeEstimation = ""); showDialog = true }) {
//                Icon(Icons.Filled.Edit, contentDescription = "Add Task")
//            }
//        }
//    ) { padding ->
//        TaskList(tasks = tasks, modifier = Modifier.padding(padding), onEdit = { task ->
//            editingTask = task.copy() // Make a copy for editing
//            showDialog = true
//        }, onDelete = { task ->
//            userId?.let { taskViewModel.deleteTask(task, it) }
//
//        })
//    }
//
//    if (showDialog) {
//        TaskDialog(task = editingTask, onDismiss = { showDialog = false }) { task ->
//            userId?.let { id ->
//                if (task.id.isEmpty()) {
//                    Log.d("Adding", task.toString())
//                    taskViewModel.addTask(task.copy(id = UUID.randomUUID().toString()), id)
//                } else {
//                    Log.d("Updating", task.toString())
//                    taskViewModel.updateTask(task, id)
//                }
//            } ?: Log.e(TAG, "User ID is null. Make sure the user is logged in.")
//            showDialog = false
//        }
//    }
//}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(taskViewModel: TaskViewModel = viewModel(), navController: NavController, authViewModel: AuthViewModel) {
    val tasks by taskViewModel.tasks.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var editingTask by remember { mutableStateOf<Task?>(null) }
    val userId by authViewModel.userId.collectAsState()

    LaunchedEffect(userId) {
        userId?.let { taskViewModel.fetchTasks(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TaskHub") }, // Set the title for your AppBar
                actions = {
                    // Add actions if necessary, for example, a logout button
                    IconButton(onClick = { /* Handle logout here */ }) {
                        Icon(Icons.Filled.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { editingTask = Task(name = "", status = false, timeEstimation = ""); showDialog = true }) {
                Icon(Icons.Filled.Edit, contentDescription = "Add Task")
            }
        }
    ) { padding ->
        TaskList(tasks = tasks, modifier = Modifier.padding(padding), onEdit = { task ->
            editingTask = task.copy() // Make a copy for editing
            showDialog = true
        }, onDelete = { task ->
            userId?.let { taskViewModel.deleteTask(task, it) }
        })
    }

    if (showDialog) {
        TaskDialog(task = editingTask, onDismiss = { showDialog = false }) { task ->
            userId?.let { id ->
                if (task.id.isEmpty()) {
                    taskViewModel.addTask(task.copy(id = UUID.randomUUID().toString()), id)
                } else {
                    taskViewModel.updateTask(task, id)
                }
            } ?: Log.e(TAG, "User ID is null. Make sure the user is logged in.")
            showDialog = false
        }
    }
}


@Composable
fun TaskDialog(task: Task?, onDismiss: () -> Unit, onSave: (Task) -> Unit) {
    var id by remember { mutableStateOf(task?.id ?: "") }
    var name by remember { mutableStateOf(task?.name ?: "") }
    var status by remember { mutableStateOf(task?.status ?: false)}
    var timeEstimation by remember { mutableStateOf(task?.timeEstimation ?: "") }

    Dialog(onDismissRequest = onDismiss) {
        // Apply additional styling here
        Card(
            modifier = Modifier
                .padding(16.dp) // Outer padding from the edge of the screen
                .fillMaxWidth() // Makes the card expand to max width
                .wrapContentHeight(), // Adjust height based on content
            shape = RoundedCornerShape(12.dp), // Rounded corners
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp) // Shadow elevation
        ) {
            Column(
                modifier = Modifier
                    .padding(all = 24.dp) // Inner padding for the content of the card
                    .fillMaxWidth(), // Fill the width inside the card
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Task Details", style = MaterialTheme.typography.headlineSmall)
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Task Name") },
                )
                OutlinedTextField(
                    value = timeEstimation,
                    onValueChange = { timeEstimation = it },
                    label = { Text("Time Estimation (hours)") },
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = status,
                        onCheckedChange = { status = it },
                        colors = CheckboxDefaults.colors() // Customize Checkbox colors here
                    )
                    Text("Status: ${if (status) "Done" else "To Do"}")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = { onSave(Task(id = id, name = name, status = status, timeEstimation = timeEstimation)) },
                        colors = ButtonDefaults.buttonColors() // Customize Button colors here
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}
//@Composable
//fun TaskDialog(task: Task?, onDismiss: () -> Unit, onSave: (Task) -> Unit) {
//    var name by remember { mutableStateOf(task?.name ?: "") }
//    var status by remember { mutableStateOf(task?.status ?: false) } // Adjusted to Boolean
//    var timeEstimation by remember { mutableStateOf(task?.timeEstimation ?: "") }
//
//    Dialog(onDismissRequest = onDismiss) {
//        // Set the Surface color here
//        Card(
//            modifier = Modifier.padding(16.dp),
//            colors = CardDefaults.cardColors(containerColor = Color.LightGray), // Your chosen color
//            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp) // Apply elevation here
//        ){
//            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
//                Text("Task Details", style = MaterialTheme.typography.headlineSmall)
//                OutlinedTextField(
//                    value = name,
//                    onValueChange = { name = it },
//                    label = { Text("Task Name") },
//                )
//                OutlinedTextField(
//                    value = timeEstimation,
//                    onValueChange = { timeEstimation = it },
//                    label = { Text("Time Estimation (hours)") },
//                )
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Checkbox(
//                        checked = status,
//                        onCheckedChange = { status = it },
//                        colors = CheckboxDefaults.colors() // Customize Checkbox colors here
//                    )
//                    Text("Status: ${if (status) "Done" else "To Do"}")
//                }
//                Spacer(modifier = Modifier.height(8.dp))
//                Row {
//                    Button(
//                        onClick = { onSave(Task(id = UUID.randomUUID().toString(), name = name, status = status, timeEstimation = timeEstimation)) },
//                        colors = ButtonDefaults.buttonColors() // Customize Button colors here
//                    ) {
//                        Text("Save")
//                    }
//                    Spacer(Modifier.width(8.dp))
//                    Button(onClick = onDismiss) {
//                        Text("Cancel")
//                    }
//                }
//            }
//        }
//    }
//}
//@Composable
//fun TaskDialog(task: Task?, onDismiss: () -> Unit, onSave: (Task) -> Unit) {
//    var name by remember { mutableStateOf(task?.name ?: "") }
//    var status by remember { mutableStateOf(task?.status ?: "")}
//    var timeEstimation by remember { mutableStateOf(task?.timeEstimation ?: "") }
//
//    Dialog(onDismissRequest = onDismiss) {
//        Surface(modifier = Modifier.padding(16.dp)) {
//            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
//                Text("Task Details", style = MaterialTheme.typography.headlineSmall)
//                OutlinedTextField(
//                    value = name,
//                    onValueChange = { name = it },
//                    label = { Text("Task Name") }
//                )
//                OutlinedTextField(
//                    value = timeEstimation,
//                    onValueChange = { timeEstimation = it },
//                    label = { Text("Time Estimation (hours)") }
//                )
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Checkbox(
//                        checked = status as Boolean,
//                        onCheckedChange = { status = it }
//                    )
//                    Text("Status: ${if (status as Boolean) "Done" else "To Do"}")
//                }
//                Spacer(modifier = Modifier.height(8.dp))
//                Row {
//                    Button(onClick = { onSave(Task(id = UUID.randomUUID().toString(),name = name, status = status as Boolean, timeEstimation = timeEstimation)) }) {
//                        Text("Save")
//                    }
//                    Spacer(Modifier.width(8.dp))
//                    Button(onClick = onDismiss) {
//                        Text("Cancel")
//                    }
//                }
//            }
//        }
//    }
//}

@Composable
fun TaskList(tasks: List<Task>, modifier: Modifier = Modifier, onEdit: (Task) -> Unit, onDelete: (Task) -> Unit) {
    LazyColumn(modifier = modifier) {
        items(tasks) { task ->
            TaskItem(task, onEdit = { onEdit(task) }, onDelete = { onDelete(task) })
            Divider()
        }
    }
}

@Composable
fun TaskItem(task: Task, onEdit: () -> Unit, onDelete: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Column(modifier = Modifier.weight(1f).clickable { onEdit() }) {
            Text(text = task.name, style = MaterialTheme.typography.titleMedium)
            Text(text = "Status: ${if (task.status) "Done" else "To Do"}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Estimation: ${task.timeEstimation} hours", style = MaterialTheme.typography.bodySmall)
        }
        IconButton(onClick = onEdit) {
            Icon(Icons.Filled.Share, contentDescription = "Share")
        }
        IconButton(onClick = onEdit) {
            Icon(Icons.Filled.Edit, contentDescription = "Edit")
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Filled.Delete, contentDescription = "Delete")
        }
    }
}


