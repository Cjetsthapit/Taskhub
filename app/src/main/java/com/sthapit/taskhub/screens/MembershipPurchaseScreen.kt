package com.sthapit.taskhub.screens


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

import com.sthapit.taskhub.viewmodel.PaymentViewModel



import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.sthapit.taskhub.models.CardInfo
import com.sthapit.taskhub.utils.ValidatorsUtil

@Composable
fun MembershipPurchaseScreen(
    paymentViewModel: PaymentViewModel = viewModel(),
    onPurchaseSuccess: () -> Unit
) {
    var paymentStatus by remember { mutableStateOf("Ready to make payment") }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    val cardNumber = remember { mutableStateOf("") }
    val expiryYear = remember { mutableStateOf("") }
    val expiryMonth = remember { mutableStateOf("") }
    val cvv = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Paypal",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 32.dp)
        )
        OutlinedTextField(
            value = cardNumber.value,
            onValueChange = { cardNumber.value = it },
            label = { Text("Card Number") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        OutlinedTextField(
            value = expiryYear.value,
            onValueChange = { expiryYear.value = it },
            label = { Text("Expiry Year") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        OutlinedTextField(
            value = expiryMonth.value,
            onValueChange = { expiryMonth.value = it },
            label = { Text("Expiry Month") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        OutlinedTextField(
            value = cvv.value,
            onValueChange = { cvv.value = it },
            label = { Text("CVV") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            )
        )
        Button(onClick = {
            if (!ValidatorsUtil.isValidCardNumber(cardNumber.value) ||
                !ValidatorsUtil.isValidExpiry(expiryMonth.value, expiryYear.value) ||
                !ValidatorsUtil.isValidCVV(cvv.value)
            ) {
                // Show error message if any of the fields are invalid
                dialogMessage = "Please check your input fields for errors."
                showDialog = true
            } else {
                // Process payment if all fields are valid
                paymentStatus = "Processing..."
                val cardInfo = CardInfo(cardNumber.value, expiryMonth.value, expiryYear.value, cvv.value)
                paymentViewModel.processCardPayment(cardInfo) { status, success ->
                    dialogMessage = status
                    showDialog = false
                    if (success) {
                        onPurchaseSuccess()
                    }
                }
            }
        }) {
            Text("Pay with Card")
        }

        // Display dialog if showDialog is true
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Payment Status") },
                text = { Text(dialogMessage) },
                confirmButton = {
                    Button(
                        onClick = { showDialog = false }
                    ) {
                        Text("OK")
                    }
                }
            )
        }
    }
}
