//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.input.ImeAction
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController // Import if using Jetpack Compose Navigation
//
//@Composable
//fun RegisterScreen(
//    authViewModel: AuthViewModel = viewModel(),
//    navController: NavController, // Add NavController parameter for navigation
//    onRegisterSuccess: () -> Unit
//) {
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var errorMessage by remember { mutableStateOf<String?>(null) }
//
//    Column(modifier = Modifier.padding(16.dp)) {
//        Text("Register on TaskHub", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp)) // App title and page context
//        if (errorMessage != null) {
//            Text("Error: $errorMessage", fontSize = 18.sp)
//            Spacer(modifier = Modifier.height(16.dp))
//        }
//
//        // Email Input
//        OutlinedTextField(
//            value = email,
//            onValueChange = { email = it },
//            label = { Text("Email") },
//            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//
//        // Password Input
//        OutlinedTextField(
//            value = password,
//            onValueChange = { password = it },
//            label = { Text("Password") },
//            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Register Button
//        Button(onClick = {
//            authViewModel.register(email, password, onRegisterSuccess) { error ->
//                errorMessage = error
//            }
//        }) {
//            Text("Register")
//        }
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Navigation to Login Page
//        TextButton(onClick = { navController.navigate("login") }) { // Assuming "login" is the route name for the LoginScreen
//            Text("Already have an account? Log in", fontSize = 16.sp)
//        }
//    }
//}

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun RegisterScreen(

    navController: NavController, // Add NavController parameter for navigation
    authViewModel : AuthViewModel,
    onRegisterSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("sth@gmail.com") }
    var password by remember { mutableStateOf("123123") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Center content in the screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, // Center horizontally
            verticalArrangement = Arrangement.Center, // Center vertically in the Box
            modifier = Modifier.fillMaxWidth() // Fill the width of the Box
        ) {
            Text("Register on TaskHub", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))
            if (errorMessage != null) {
                Text("Error: $errorMessage", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Email Input
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(), // Make the TextField fill the width
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Password Input
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(), // Make the TextField fill the width
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Register Button
            Button(
                onClick = {
                    authViewModel.register(email, password, onRegisterSuccess) { error ->
                        errorMessage = error
                    }
                },
                modifier = Modifier
                    .fillMaxWidth() // Make the Button fill the width
                    .height(56.dp) // Increase the height for better touch target
            ) {
                Text("Register", fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Navigation to Login Page
            TextButton(onClick = { navController.navigate("login") }) {
                Text("Already have an account? Log in", fontSize = 16.sp)
            }
        }
    }
}

