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
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.sthapit.taskhub.viewmodel.PaymentViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    paymentViewModel: PaymentViewModel,
    onRegisterClick: () -> Unit // Add a callback for navigating to the register screen
) {
    var email by remember { mutableStateOf("sth@gmail.com") }
    var password by remember { mutableStateOf("123123") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val currentContext = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        // Dark overlay
        Box(
            modifier = Modifier.fillMaxSize()
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
                        onLoginSuccess()
                        val hasMembership = MembershipManager.getMembershipStatus(currentContext)
                        Toast.makeText(currentContext, "Membership Status: $hasMembership", Toast.LENGTH_SHORT).show()
                        Log.d("LoginScreen", "Membership Status: $hasMembership")
                        if (hasMembership) {
                            // Navigate to HomeScreen if user has membership
                            navController.navigate("home") {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = true
                                }
                            }
                        } else {
                            // Navigate to MembershipPurchaseScreen if user does not have membership
                            navController.navigate("membershipPurchase") {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = true
                                }
                            }
                        }
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