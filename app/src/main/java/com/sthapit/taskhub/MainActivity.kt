package com.sthapit.taskhub

import AuthViewModel
import LoginScreen

import RegisterScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.sthapit.taskhub.ui.theme.TaskhubTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val authViewModel: AuthViewModel by viewModels()
        setContent {
            FirebaseApp.initializeApp(this)
            TaskhubTheme {
                Surface() {
                    AppNavigation(authViewModel = authViewModel)
                }
            }
        }
    }
}

@Composable
fun AppNavigation(authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController = navController,authViewModel = authViewModel, onLoginSuccess = {
                navController.navigate("mainScreen")
            })
        }
        composable("register") {
            RegisterScreen(navController = navController,authViewModel = authViewModel, onRegisterSuccess = {
                navController.navigate("login")
            })
        }
        composable("mainScreen") {
            MainScreen(navController = navController,authViewModel = authViewModel,)
        }
    }
}

