package com.example.poker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

class MyInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_info)

        val nameTextView = findViewById<TextView>(R.id.name)
        val chipCountTextView = findViewById<TextView>(R.id.chipCount)
        val bestWinTextView = findViewById<TextView>(R.id.bestWin)
        val queue = Volley.newRequestQueue(this)
        val url = "${IP.getIP()}/player/name/${Global.currentPlayerName}"

        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET,
            url,
            null,
            { response ->
                nameTextView.text = response.getJSONObject(0).getString("name")
                chipCountTextView.text = response.getJSONObject(0).getString("chip_count")
                bestWinTextView.text = response.getJSONObject(0).getString("best_win")
            },
            { error -> Log.d("why?", error.message.toString())}
        )

        jsonArrayRequest.setShouldCache(false)
        queue.add(jsonArrayRequest)
    }
}