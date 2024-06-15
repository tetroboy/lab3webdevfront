package com.example.maga_web_app.backendapi

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.http.GET
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.OkHttpClient

class AuthInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(newRequest)
    }
}


class RetrofitAuthHelper {
    companion object {
        public fun getInstance(baseUrl: String, token: String): Retrofit {
            // Define the interceptor to add the Authorization header
            val authInterceptor = Interceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(newRequest)
            }

            // Create an OkHttpClient and attach the interceptor to it
            val client = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .build()

            // Build the Retrofit instance with the OkHttpClient
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}
