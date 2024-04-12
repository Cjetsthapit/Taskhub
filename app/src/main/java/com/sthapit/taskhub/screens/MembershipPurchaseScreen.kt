package com.sthapit.taskhub.screens


import androidx.compose.foundation.layout.Column

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*

import androidx.lifecycle.viewmodel.compose.viewModel

import com.sthapit.taskhub.viewmodel.PaymentViewModel



import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import com.sthapit.taskhub.models.CardInfo
import com.sthapit.taskhub.utils.ValidatorsUtil


@Composable
fun MembershipPurchaseScreen(
    paymentViewModel: PaymentViewModel = viewModel(),
    onPurchaseSuccess: () -> Unit
) {
    var paymentStatus by remember { mutableStateOf("Ready to make payment") }
    var cardNumber by remember { mutableStateOf("") }
    var expiryYear by remember { mutableStateOf("") }
    var expiryMonth by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Payment Status") },
            text = { Text(dialogMessage) },
            confirmButton = {
                TextButton(
                    onClick = { showDialog = false }
                ) {
                    Text("OK")
                }
            }
        )
    }

    Column() {
        TextField(
            value = cardNumber,
            onValueChange = { cardNumber = it },
            label = { Text("Card Number") }
        )
        TextField(
            value = expiryYear,
            onValueChange = { expiryYear = it },
            label = { Text("Expiry Year") }
        )
        TextField(
            value = expiryMonth,
            onValueChange = { expiryMonth = it },
            label = { Text("Expiry Month") }
        )
        TextField(
            value = cvv,
            onValueChange = { cvv = it },
            label = { Text("CVV") }
        )
        Button(onClick = {
            if (!ValidatorsUtil.isValidCardNumber(cardNumber) || !ValidatorsUtil.isValidExpiry(expiryMonth, expiryYear) || !ValidatorsUtil.isValidCVV(cvv)) {
                dialogMessage = "Please check your input fields for errors."
                showDialog = true
            } else {
                paymentStatus = "Processing..."
                val cardInfo = CardInfo(cardNumber, expiryMonth, expiryYear, cvv)
                paymentViewModel.processCardPayment(cardInfo) { status, success ->
                    dialogMessage = status
                    showDialog = true
                    if (success) {
                        onPurchaseSuccess()
                    }
                }
            }
        }) {
            Text("Pay with Card")
        }
    }
}

