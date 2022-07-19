package com.example.poker

import android.content.Intent
import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.poker.ImageViewScrolling.IEventEnd
import com.example.poker.ImageViewScrolling.Util
import kotlinx.android.synthetic.main.activity_slot.*
import java.util.*
import kotlin.random.Random.Default.nextInt

class SlotActivity : AppCompatActivity(), IEventEnd {
    private val TAG: String = this.javaClass.simpleName
    private lateinit var queue: RequestQueue
    internal var count_down = 0
    lateinit var resulttext : TextView
    var abc = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slot)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        resulttext = findViewById(R.id.resultslot)
        image1.setEventEnd(this@SlotActivity)
        image2.setEventEnd(this@SlotActivity)
        image3.setEventEnd(this@SlotActivity)
        val slotchip = findViewById<TextView>(R.id.slotmoney)
        queue = Volley.newRequestQueue(this)
        val url = "${IP.getIP()}/player/name/${Global.currentPlayerName}"
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                runOnUiThread {
                    abc = response.getJSONObject(0).getInt("chip_count")
                    slotchip.text = abc.toString()
                }
            },
            { error -> Log.d("why?", error.message.toString())}
        )
        jsonArrayRequest.setShouldCache(false)
        queue.add(jsonArrayRequest)
        up.setOnClickListener {
            if (abc >= 50) {
                val mediaPlayer = MediaPlayer.create(this,R.raw.slotmachine)
                mediaPlayer.start()
                up.visibility = View.GONE
                down.visibility = View.VISIBLE
                image1.setValueRandom(nextInt(7),
                    nextInt(10)+7)
                image2.setValueRandom(nextInt(6),
                    nextInt(10)+7)
                image3.setValueRandom(nextInt(6),
                    nextInt(10)+7)
                abc -=50
                val url1 = "${IP.getIP()}/player/name/${Global.currentPlayerName}/chip/$abc"
                queue = Volley.newRequestQueue(this)
                val stringRequest = StringRequest(Request.Method.POST,
                    url1,
                    { response ->
                        if (response == "0") {
                            slotmoney.text = abc.toString()
                        }
                    },
                    { error -> Log.d("why?", error.message.toString()) }
                )
                stringRequest.setShouldCache(false)
                queue.add(stringRequest)
            }
            else {
                Toast.makeText(this, "You do not have enough money", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun eventEnd(result: Int, count: Int) {
        if (count_down < 2) {
            count_down++
        }
        else {
            down.visibility = View.GONE
            up.visibility = View.VISIBLE
            count_down = 0
            if(image1.value == Util.triple && image2. value ==Util.triple && image3. value==Util.triple){
                Toast.makeText(this, "Biggest Jackpot!", Toast.LENGTH_SHORT).show()
                abc += 6000
                resulttext.text = "JACKPOT : +6000"
                val url2 = "${IP.getIP()}/player/name/${Global.currentPlayerName}/chip/$abc"
                queue = Volley.newRequestQueue(this)
                val stringRequest = StringRequest(Request.Method.POST,
                    url2,
                    { response ->
                        if (response == "0") {
                            slotmoney.text = abc.toString()
                        }
                    },
                    { error -> Log.d("why?", error.message.toString()) }
                )
                stringRequest.setShouldCache(false)
                queue.add(stringRequest)
            }
            if(image1.value == Util.seven && image2. value ==Util.seven && image3. value==Util.seven){
                Toast.makeText(this, "Jackpot!", Toast.LENGTH_SHORT).show()
                abc += 3000
                resulttext.text = "JACKPOT : +3000"
                val url2 = "${IP.getIP()}/player/name/${Global.currentPlayerName}/chip/$abc"
                queue = Volley.newRequestQueue(this)
                val stringRequest = StringRequest(Request.Method.POST,
                    url2,
                    { response ->
                        if (response == "0") {
                            slotmoney.text = abc.toString()
                        }
                    },
                    { error -> Log.d("why?", error.message.toString()) }
                )
                stringRequest.setShouldCache(false)
                queue.add(stringRequest)
            }
            else if(image1.value == image2. value &&image2.value == image3. value){
                Toast.makeText(this, "You win prize", Toast.LENGTH_SHORT).show()
                resulttext.text = "You win!"
                abc += 300
                val url2 = "${IP.getIP()}/player/name/${Global.currentPlayerName}/chip/$abc"
                queue = Volley.newRequestQueue(this)
                val stringRequest = StringRequest(Request.Method.POST,
                    url2,
                    { response ->
                        if (response == "0") {
                            slotmoney.text = abc.toString()
                        }
                    },
                    { error -> Log.d("why?", error.message.toString()) }
                )
                stringRequest.setShouldCache(false)
                queue.add(stringRequest)
            }
            else {
                resulttext.text = "Try next time"
                Toast.makeText(this, "You lose", Toast.LENGTH_SHORT).show()
            }
        }
    }
}