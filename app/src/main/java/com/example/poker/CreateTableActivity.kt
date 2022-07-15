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

class CreateTableActivity : AppCompatActivity() {
    private lateinit var createbuttonfinally : Button
    private lateinit var gameEditText: EditText
    private lateinit var maxnumEditText: EditText
    private lateinit var queue: RequestQueue


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_table)

        createbuttonfinally = findViewById(R.id.createbuttonfinally)
        gameEditText = findViewById(R.id.creategamename)
        maxnumEditText = findViewById(R.id.maxnumber)

        createbuttonfinally.setOnClickListener{
            val gamename: String = gameEditText.text.toString()
            val maxnumber: String = maxnumEditText.text.toString()

            if (gamename.isEmpty() || maxnumber.isEmpty()) {
                Toast.makeText(this, "Check your input!", Toast.LENGTH_SHORT).show()
            }
            else {
                println("name: $gamename")
                println("password: $maxnumber")
                println("creator : ${Global.currentPlayerName}")
                val url = "${IP.getIP()}/game/newTable/$gamename/$maxnumber/${Global.currentPlayerName}"
                println(url)
                queue = Volley.newRequestQueue(this)

                val stringRequest = StringRequest(Request.Method.POST,
                    url,
                    { response ->
                        println(response)
                        if (response == "0") {
                            // 성공
                            val start = Intent(this, InTableactivity::class.java)
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