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

class SignupActivity : AppCompatActivity() {
    private lateinit var registerNameEditText: EditText
    private lateinit var registerPasswordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var queue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        registerNameEditText = findViewById(R.id.registerName)
        registerPasswordEditText = findViewById(R.id.registerPassword)
        registerButton = findViewById(R.id.registerButton)

        registerButton.setOnClickListener {
            val name = registerNameEditText.text.toString()
            val password = registerPasswordEditText.text.toString()

            if (name.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Check your input!", Toast.LENGTH_SHORT).show()
            }
            else {
                val url = "${IP.getIP()}/player/signup/name/$name/password/$password"
                queue = Volley.newRequestQueue(this)

                val stringRequest = StringRequest(Request.Method.POST,
                    url,
                    { response ->
                        if (response == "0") {
                            // 가입 성공
                            Global.currentPlayerName = name
                            val start = Intent(this, MainActivity::class.java)
                            startActivity(start)
                            finish()
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