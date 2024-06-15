package com.example.maga_web_app.backendapi

import com.example.maga_web_app.models.*
import retrofit2.Call
import retrofit2.http.*

interface DjangoApi {
    @POST("/login")
    fun  getToken(
        @Body info: LogInBody
    ): Call<Token>
}