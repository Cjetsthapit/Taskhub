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
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit // Add a callback for navigating to the register screen
) {
    var email by remember { mutableStateOf("sth@gmail.com") }
    var password by remember { mutableStateOf("123123") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {

        // Dark overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
        )

        // Centered Column for content
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
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
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Password Input
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Login Button
            Button(
                onClick = {
                    authViewModel.login(email, password, onLoginSuccess) {
                        // If login is successful, navigate to MembershipPurchaseScreen
                        onLoginSuccess()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Login", fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Navigation to Register Page
            TextButton(onClick = onRegisterClick) {
                Text("Don't have an account? Register", fontSize = 16.sp)
            }
        }
    }
}
