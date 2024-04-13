package com.sthapit.taskhub.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sthapit.taskhub.models.CardInfo

import com.sthapit.taskhub.utils.PayPalNetworkUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PaymentViewModel : ViewModel() {

    // This function will be modified to include a callback parameter to return the status.
    fun processPayment(onResult: (String) -> Unit) {
        viewModelScope.launch {
            try {
                // Switching to IO for network request
                val accessToken = withContext(Dispatchers.IO) {
                    PayPalNetworkUtil.getAccessToken()
                }
                accessToken?.let { token ->
                    Log.d("AccessToken", token)
                    // Proceed with creating the order
                    val orderResponse = withContext(Dispatchers.IO) {
                        PayPalNetworkUtil.createOrder(token )
                    }

                    if (orderResponse != null) {
                        // Assume success if response is not null
                        onResult("Payment Successful: Order Created")
                    } else {
                        onResult("Failed to create order")
                    }
                } ?: run {
                    // Access token is null, handle failure
                    onResult("Failed to obtain access token")
                }
            } catch (e: Exception) {
                // Handle any exceptions
                onResult("Error processing payment: ${e.localizedMessage}")
            }
        }
    }

    fun processCardPayment(cardInfo: CardInfo, onResult: (String, Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val accessToken = PayPalNetworkUtil.getAccessToken()
                if (accessToken != null) {
                    // Create order with card payment specifics
                    val orderResponse = PayPalNetworkUtil.createOrderWithCard(accessToken, cardInfo)

                    if (orderResponse != null) {
                        Log.d("OrderResponse", orderResponse)
                        onResult("Payment Successful: Order Created", true)
                    } else {
                        Log.e("OrderResponseError", "Failed to create order with card")
                        onResult("Failed to create order with card. Please check card details and try again.", false)
                    }
                } else {
                    Log.e("AccessTokenError", "Failed to obtain access token")
                    onResult("Failed to obtain access token. Please check your network connection.", false)
                }
            } catch (e: Exception) {
                Log.e("PaymentError", "Error processing payment: ${e.localizedMessage}")
                onResult("Error processing payment: ${e.localizedMessage}", false)
            }
        }
        fun hasMembership(): Boolean {
            // Implement the logic to check if the user has a membership.
            // This function should return true if the user has a membership, false otherwise.
            // You can use any appropriate method here, such as checking a local database, making a network request, etc.
            return true // Placeholder, replace with your actual implementation
        }
    }

    fun hasMembership(): Boolean {
        return true
    }
//    fun processCardPayment(cardInfo: CardInfo, onResult: (String) -> Unit) {
//        viewModelScope.launch {
//            try {
//                val accessToken = com.sthapit.taskhub.utils.PayPalNetworkUtil.getAccessToken()
//                accessToken?.let { token ->
//                    // Create order with card payment specifics
//                    val orderResponse = com.sthapit.taskhub.utils.PayPalNetworkUtil.createOrderWithCard(token, cardInfo)
//                    if (orderResponse != null) {
//                            Log.d("OrderResposne", orderResponse)
//
//                        onResult("Payment Successful: Order Created")
//                    } else {
//                        onResult("Failed to create order")
//                    }
//                } ?: onResult("Failed to obtain access token")
//            } catch (e: Exception) {
//                onResult("Error processing payment: ${e.localizedMessage}")
//            }
//        }
//    }
}
