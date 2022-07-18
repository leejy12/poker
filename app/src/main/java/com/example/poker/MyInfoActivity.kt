package com.example.poker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

class MyInfoActivity : AppCompatActivity() {
    var a = 0
    lateinit var rankimg : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_info)
        rankimg = findViewById(R.id.rankimage)
        val nameTextView = findViewById<TextView>(R.id.name)
        val chipCountTextView = findViewById<TextView>(R.id.chipCount)
        val bestWinTextView = findViewById<TextView>(R.id.rank)
        val queue = Volley.newRequestQueue(this)
        val url = "${IP.getIP()}/player/name/${Global.currentPlayerName}"
        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET,
            url,
            null,
            { response ->
                nameTextView.text = response.getJSONObject(0).getString("name")
                a = response.getJSONObject(0).getString("chip_count").toInt()
                chipCountTextView.text = a.toString()
                if(a>= 100000){
                    bestWinTextView.text = "대통령"
                    rankimg.setImageResource(R.drawable.president)
                }
                else if(a>=50000){
                    bestWinTextView.text = "재벌"
                    rankimg.setImageResource(R.drawable.rich)
                }
                else if(a>=20000){
                    bestWinTextView.text = "슈퍼스타"
                    rankimg.setImageResource(R.drawable.superstar)
                }
                else if(a>=10000){
                    bestWinTextView.text = "스타"
                    rankimg.setImageResource(R.drawable.star)
                }
                else if(a>=7000){
                    bestWinTextView.text = "총장"
                    rankimg.setImageResource(R.drawable.weakpresident)
                }
                else if(a>=5000){
                    bestWinTextView.text = "교수"
                    rankimg.setImageResource(R.drawable.professor)
                }
                else if(a>=3000){
                    bestWinTextView.text = "새내기"
                    rankimg.setImageResource(R.drawable.youngstudent)
                }
                else if(a>=2000){
                    bestWinTextView.text = "헌내기"
                    rankimg.setImageResource(R.drawable.olderstudent)
                }
                else{
                    bestWinTextView.text = "대학원생"
                    rankimg.setImageResource(R.drawable.graduatestudent)
                }
            },
            { error -> Log.d("why?", error.message.toString())}

        )

        jsonArrayRequest.setShouldCache(false)
        queue.add(jsonArrayRequest)
    }
}
