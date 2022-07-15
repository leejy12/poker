package com.example.poker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class LoginActivity : AppCompatActivity() {
    private lateinit var loginButton: Button
    private lateinit var loginNameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var queue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton = findViewById(R.id.loginButton)
        loginNameEditText = findViewById(R.id.loginName)
        passwordEditText = findViewById(R.id.loginPW)

        loginButton.setOnClickListener {
            val name: String = loginNameEditText.text.toString()
            val password: String = passwordEditText.text.toString()

            if (name.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Check your input!", Toast.LENGTH_SHORT).show()
            }
            else {
                println("name: $name")
                println("password: $password")
                val url = "${IP.getIP()}/player/login/name/$name/password/$password"
                println(url)
                queue = Volley.newRequestQueue(this)

                val stringRequest = StringRequest(Request.Method.POST,
                    url,
                    { response ->
                        println(response)
                        if (response == "0") {
                            // 로그인 성공
                            Global.currentPlayerName = name
                            val start = Intent(this, MainActivity::class.java)
                            startActivity(start)
                            finish()
                        }
                        else {
                            // 로그인 실패
                            Toast.makeText(this, "Incorrect name or password!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    { error -> Log.d("why?", error.message.toString()) }
                )

                stringRequest.setShouldCache(false)
                queue.add(stringRequest)
            }
        }
    }
}