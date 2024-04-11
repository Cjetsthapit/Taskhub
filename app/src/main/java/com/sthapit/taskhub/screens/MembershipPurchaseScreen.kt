package com.sthapit.taskhub.screens

import AuthViewModel
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import android.content.Context
import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import com.paypal.android.sdk.payments.PayPalConfiguration
import com.paypal.android.sdk.payments.PayPalPayment
import com.paypal.android.sdk.payments.PayPalService
import com.paypal.android.sdk.payments.PaymentActivity
import java.math.BigDecimal

@Composable
fun MembershipPurchaseButton(
    context: Context,
    onClick: () -> Unit
) {
    Button(
        onClick = { onClick() },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Purchase Membership")
    }
}

@Composable
fun MembershipPurchaseScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    onPurchaseSuccess: () -> Unit
) {
    var membershipType by remember { mutableStateOf("Basic") }
    var amount by remember { mutableStateOf(10.00) } // Set default amount

    val context = LocalContext.current // Capture the context outside the lambda

    // Function to initiate PayPal payment
    fun initiatePayPalPayment() {
        // Initialize PayPal configuration
        val config = PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX) // Use sandbox for testing
            .clientId("AWAmfyQc597GK4Ic84UWocHaYtKxQ85MTgDtOlqMEnaWVogY-7Itq4C5dvCuyfkBhUzOd6Agzek09CTT")

        // Pass the PayPal configuration to the intent
        val intent = Intent(context, PayPalService::class.java)
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config)
        context.startService(intent) // Start the PayPalService

        val payment = PayPalPayment(
            BigDecimal.valueOf(amount), // Payment amount
            "USD", // Currency code
            "Membership Purchase - $membershipType", // Item description
            PayPalPayment.PAYMENT_INTENT_SALE // Payment intent
        )

        // Start PayPal PaymentActivity with the payment object
        val paymentIntent = Intent(context, PaymentActivity::class.java)
        paymentIntent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment)
        val requestCode = 123 // You can define your own request code
        (context as? ComponentActivity)?.startActivityForResult(paymentIntent, requestCode)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Display membership type and amount
        Text(
            text = "Membership Type: $membershipType",
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Amount: $$amount",
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Button to initiate PayPal payment
        MembershipPurchaseButton(
            context = context,
            onClick = { initiatePayPalPayment() } // Use the captured context here
        )
    }
}
