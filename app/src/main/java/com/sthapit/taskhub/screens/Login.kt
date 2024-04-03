import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController


@Composable
fun LoginScreen(

    navController: NavController,
    authViewModel : AuthViewModel,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("sth@gmail.com") }
    var password by remember { mutableStateOf("123123") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {

        // Dark overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
//                .background(Brush.verticalGradient(colors = listOf(androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.1f), androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.3f))))
        )

        // Centered Column for content
        Column(
            modifier = Modifier
                .align(Alignment.Center) // Align the Column itself within the Box
                .padding(16.dp)
                .fillMaxWidth(), // Fill the maximum width available
            horizontalAlignment = Alignment.CenterHorizontally, // Center content horizontally within the Column
            verticalArrangement = Arrangement.Center // Center content vertically within the Column
        ) {
            Text("Login to TaskHub", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))
            if (errorMessage != null) {
                Text("Error: $errorMessage", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Email Input
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(), // Make the TextField expand to fill the width
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Password Input
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(), // Make the TextField expand to fill the width
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Login Button
            Button(
                onClick = {
                    authViewModel.login(email, password, onLoginSuccess) { error ->
                        errorMessage = error
                    }
                },
                modifier = Modifier
                    .fillMaxWidth() // Expand button to fill the width
                    .height(56.dp) // Increase the button's height for a larger touch target
            ) {
                Text("Login", fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Navigation to Register Page
            TextButton(onClick = { navController.navigate("register") }) {
                Text("Don't have an account? Register", fontSize = 16.sp)
            }
        }
    }
}
