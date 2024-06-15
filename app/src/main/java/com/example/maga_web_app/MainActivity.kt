package com.example.maga_web_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.maga_web_app.R
import com.example.maga_web_app.backendapi.DjangoApi
import com.example.maga_web_app.backendapi.RetrofitHelper
import com.example.maga_web_app.models.LogInBody
import com.example.maga_web_app.models.Token
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    var djangoApi = RetrofitHelper.getInstance().create(DjangoApi::class.java)
    var job: Job? = null
    val token = MutableLiveData<Token>()
    val postsLoadError = MutableLiveData<String?>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activity_main_loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val loginInfo = LogInBody(email, password)
            djangoApi.getToken(loginInfo).enqueue(object : Callback<Token> {
                override fun onFailure(call: Call<Token>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<Token>, response: Response<Token>) {
                    if (response.isSuccessful && response.body() != null) {
                        token.value = response.body()
                        // Log.d("Token: ", getTokenAsString())

                        // Navigate to AboutmeActivity if token is received
                        val intent = Intent(this@MainActivity, AboutmeActivity::class.java)
                        intent.putExtra("AUTH_TOKEN", getTokenAsString())
                        startActivity(intent)
                    } else {
                        Toast.makeText(applicationContext, "Wrong Email or Password", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }


        emailEditText.setText("zimenkov@test.ukr.net");
        passwordEditText.setText("test");
        // activity_main_loginButton.performClick();
        // delay(1000);
        // activity_main_loginButton.performClick();
    }

    public fun getTokenAsString(): String {
        // return token.value.toString()
        return token.value!!.access
    }

}