package com.example.maga_web_app

import UserApi
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setMargins
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.maga_web_app.backendapi.RetrofitAuthHelper

class AboutmeActivity : AppCompatActivity() {
    private lateinit var userDetailsLayout: LinearLayout
    private lateinit var tokenString: String
    private lateinit var apiService: UserApi
    // var UserApi = RetrofitHelper.getInstance().create(UserApi::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_me)
        userDetailsLayout = findViewById(R.id.userDetailsLayout)
//
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://yourdjangoapi.com/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        val apiService = retrofit.create(UserApi::class.java)
//        fetchUserDetails(apiService)

        // { Authorization: `Bearer ${this.$store.state.accessToken}` }
        tokenString = intent.getStringExtra("AUTH_TOKEN") ?: ""

        Log.d("tokenString: ", tokenString)

        apiService = RetrofitAuthHelper.getInstance("http://192.168.2.187:8080/", tokenString).create(UserApi::class.java)
        apiService.getUserDetails()
        // fetchAboutmeDetails()
        // Toast.makeText(applicationContext, tokenString, Toast.LENGTH_LONG).show()

        apiService.getUserDetails().enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                if (response.isSuccessful) {
                    response.body()?.let { userDetails ->
                        displayUserDetails(userDetails)
                    }
                } else {
                    // Handle the case where the response is not successful
                    Log.d("Error:","${response.message()}")
                    displayError("Failed to load user details: ${response.message()}")

                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                // Handle the case where the call failed
                Log.d("Error","${t.message}")
                displayError("Error: ${t.message}")
            }
        })
    }

    private fun fetchUserDetails(apiService: UserApi) {
        apiService.getUserDetails().enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                if (response.isSuccessful) {
                    response.body()?.let { userDetails ->
                        displayUserDetails(userDetails)
                    }
                } else {
                    // Handle the case where the response is not successful
                    displayError("Failed to load user details: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                // Handle the case where the call failed
                displayError("Error: ${t.message}")
            }
        })
    }

    private fun displayUserDetails(userDetails: Map<String, String>) {
        userDetails.forEach { (key, value) ->
            val textView = TextView(this).apply {
                text = "$key: $value"
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                    setMargins(16)
                }
            }
            userDetailsLayout.addView(textView)
        }
    }

    private fun displayError(message: String) {
        val errorTextView = TextView(this).apply {
            text = message
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                setMargins(16)
            }
        }
        userDetailsLayout.addView(errorTextView)
    }
}