package com.sthapit.taskhub.utils

import android.util.Log
import com.google.gson.JsonParser
import com.sthapit.taskhub.models.CardInfo
import okhttp3.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.util.Base64

import java.util.UUID

object PayPalNetworkUtil {
    private const val BASE_URL = "https://api-m.sandbox.paypal.com"
    private const val CLIENT_ID = "AUCyUs3S4-1gGQ3DmCxreKeenGtOkB4K0nUxIXsUHMDLOfAxR8jcRXuUkNn6vL4mn7AhVnDEcH2IDtI4"
    private const val CLIENT_SECRET = "EJZbNCINSkb4eUQ1yP45TU0xykJc1cfWDvnxWgCEcIG0t-j9HB8jXIPxn0Jhfllixfzzltn1kDJv7hsa"

    private val client = OkHttpClient()

    suspend fun getAccessToken(): String? = withContext(Dispatchers.IO) {
        val url = "$BASE_URL/v1/oauth2/token"
        val credentials = "$CLIENT_ID:$CLIENT_SECRET"
        val encodedCredentials = Base64.getEncoder().encodeToString(credentials.toByteArray())
        val requestBody = RequestBody.create("application/x-www-form-urlencoded".toMediaTypeOrNull(), "grant_type=client_credentials")

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .header("Authorization", "Basic $encodedCredentials")
            .build()

        client.newCall(request).execute().use { response ->
            val responseString = response.body?.string()
            val jsonObject = JsonParser.parseString(responseString).asJsonObject
            return@withContext jsonObject.get("access_token").asString
        }
    }

    suspend fun createOrder(accessToken: String): String? = withContext(Dispatchers.IO) {
        val url = "$BASE_URL/v2/checkout/orders"
        val requestBody = RequestBody.create(
            "application/json".toMediaTypeOrNull(),
            """
            {
                "intent": "CAPTURE",
                "purchase_units": [
                    {
                        "amount": {
                            "currency_code": "USD",
                            "value": "100.00"
                        }
                    }
                ]
            }
            """.trimIndent()
        )

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .header("Authorization", "Bearer $accessToken")
            .header("Content-Type", "application/json")
            .build()

        val response = client.newCall(request).execute()
        return@withContext if (response.isSuccessful) {
            response.body?.string() // Order creation success response
        } else {
            null
        }

    }


    suspend fun createOrderWithCard(accessToken: String, cardInfo: CardInfo): String? = withContext(Dispatchers.IO) {
        val url = "$BASE_URL/v2/checkout/orders"

        val requestBody = RequestBody.create(
            "application/json".toMediaTypeOrNull(),
            """
            {
                "intent": "CAPTURE",
                "purchase_units": [
                    {
                        "amount": {
                            "currency_code": "USD",
                            "value": "100.00"
                        }
                    }
                ],
                "payment_source": {
                    "card": {
                        "number": "${cardInfo.number}",
                        "expiry": "${cardInfo.expiryYear}-${cardInfo.expiryMonth}",
                        "security_code": "${cardInfo.cvv}"
                    }
                }
            }
            """.trimIndent()
        )

        Log.d("JSON", requestBody.toString())

        val uniqueRequestId = UUID.randomUUID().toString()
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .header("Authorization", "Bearer $accessToken")
            .header("Content-Type", "application/json")
            .header("PayPal-Request-Id", uniqueRequestId)
            .build()

        client.newCall(request).execute().use { response ->
            return@withContext if (response.isSuccessful) {
                response.body?.string()  // Order creation success response
            } else {
                null
//                response.body?.string()  // Log this or handle it to understand what went wrong
            }
        }
    }

}
