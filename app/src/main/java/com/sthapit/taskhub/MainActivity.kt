package com.sthapit.taskhub

import AuthViewModel
import LoginScreen

import RegisterScreen
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.sthapit.taskhub.screens.MembershipPurchaseScreen
import com.sthapit.taskhub.ui.theme.TaskhubTheme
import com.sthapit.taskhub.viewmodel.PaymentViewModel

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private val paymentViewModel: PaymentViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FirebaseApp.initializeApp(this)
            TaskhubTheme {
                Surface() {
                    AppNavigation(authViewModel = authViewModel, paymentViewModel = paymentViewModel)
                }
            }
        }
    }
}

@Composable
fun AppNavigation(authViewModel: AuthViewModel, paymentViewModel: PaymentViewModel) {
    val navController = rememberNavController()

    val status by authViewModel.status.collectAsState()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                navController = navController,
                authViewModel = authViewModel,
                onLoginSuccess = {
                    if (it == "1") {
                        navController.navigate("mainScreen") {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                        }
                    } else {
                        navController.navigate("membershipPurchase") {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                        }
                    }
                },
                paymentViewModel = paymentViewModel,
                onRegisterClick = {
                    navController.navigate("register")
                }
            )
        }
        composable("register") {
            RegisterScreen(
                navController = navController,
                authViewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate("login")
                }
            )
        }
        composable("mainScreen") {
            MainScreen(navController = navController, authViewModel = authViewModel)
        }
        composable("membershipPurchase") {
            MembershipPurchaseScreen(
                authViewModel = authViewModel,
                paymentViewModel = paymentViewModel, // Pass the paymentViewModel parameter here
                onPurchaseSuccess = {
                    navController.navigate("mainScreen")
                }
            )
        }
    }
}