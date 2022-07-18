package com.example.poker
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_roulette.*

class RouletteActivity : AppCompatActivity() ,Animation.AnimationListener{

    var abc = 0
    private lateinit var queue: RequestQueue
    private var count = 0
    private var flag = false
    private var powerButton: Button? = null
    private var BetBtn:Button?= null
    private var BetCoinBtn : Button?=null
    val prizes = intArrayOf(0,26,3,35,12,28,7,29,18,22,9,31,14,20,1,33,16,24,5,10,23,8,30,11,36,13,27,6,34,17,25,2,21,4,19,15,32)
    private var mSpinDuration :Long = 0
    private var mSpinRevolution = 0f
    var pointerImageView:ImageView? = null
    var infoText: TextView? = null
    var prizeText = "N/A"
    var finalselection = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_roulette)
        val roulettechip = findViewById<TextView>(R.id.roulettemoney)
        queue = Volley.newRequestQueue(this)
        val url = "${IP.getIP()}/player/name/${Global.currentPlayerName}"
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                runOnUiThread {
                    abc = response.getJSONObject(0).getInt("chip_count")
                    roulettechip.text = abc.toString()
                }
            },
            { error -> Log.d("why?", error.message.toString())}
        )
        jsonArrayRequest.setShouldCache(false)
        queue.add(jsonArrayRequest)

        /**get Id*/
        powerButton = findViewById(R.id.spinBtn)
        powerButton!!.setOnTouchListener(PowerTouchListener())
        intSpinner()
        BetBtn = findViewById(R.id.BetBtn)
        BetBtn?.setOnClickListener{
            showBet()
        }

    }

    private fun showBet() {
        val selectednumber : ArrayList<String> = arrayListOf()
        val builder : AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("베팅할 숫자를 선택하세요!")
        builder.setMultiChoiceItems(R.array.numbers,null){
            po, which , ischecked ->

            val numbers : Array<String> = resources.getStringArray(R.array.numbers)
            if(ischecked){
                selectednumber.add(numbers[which])
            }else{
                selectednumber.remove(numbers[which])
            }
        }
        builder.setPositiveButton("OK"){
            p0, p1 ->
            finalselection=""
            for(item:String in selectednumber){
                finalselection= finalselection +item
            }
            BetBtn?.isEnabled = false
            powerButton?.isEnabled = true
            Toast.makeText(applicationContext, "선택된 숫자는 $finalselection",Toast.LENGTH_LONG).show()

        }
        builder.setNegativeButton("Cancel"){
            dialog, p1 -> dialog.cancel()
        }
        val alertDialog : AlertDialog = builder.create()
        alertDialog.show()

    }

    /**
     * All the vars you need
     * */


    private fun intSpinner() {
        pointerImageView = findViewById(R.id.wheel)
        infoText = findViewById(R.id.resultTv)
    }

    fun startSpinner() {
        mSpinRevolution = 3600f
        mSpinDuration = 5000

        if (count >= 30){
            mSpinDuration = 1000
            mSpinRevolution = (3600 * 2).toFloat()
        }
        if (count >= 60){
            mSpinDuration = 15000
            mSpinRevolution = (3600 * 3).toFloat()

        }

        // Final point of rotation defined right here

        val end = Math.floor(Math.random() * 360).toInt() // random : 0-360
        println("asasasasasasasasa : $end")
        val numOfPrizes = prizes.size // quantity of prize
        val shift = 0 // shit where the arrow points
        val prizeIndex = (shift + end) *numOfPrizes / 360
        println("asasasasasasasasa : $shift  $prizeIndex")
        var a =finalselection.toInt()
        var num=0
        var correct = 0
        while(a>0){
            if((a%10)==prizes[prizeIndex]){
                correct+=1
            }
            a=a/10
            num+=1
        }
        if(correct==1){
            prizeText = "Number : ${prizes[prizeIndex]} , YOU WIN"
            //돈추가해주기
        }
        else{
            prizeText = "Number : ${prizes[prizeIndex]}, YOU LOSE"
        }
        val rotateAnim = RotateAnimation(
            0f,mSpinRevolution + end,
            Animation.RELATIVE_TO_SELF,
            0.5f,Animation.RELATIVE_TO_SELF,0.5f
        )
        rotateAnim.interpolator = DecelerateInterpolator()
        rotateAnim.repeatCount = 0
        rotateAnim.duration = mSpinDuration
        rotateAnim.setAnimationListener(this)
        rotateAnim.fillAfter = true
        pointerImageView!!.startAnimation(rotateAnim)

    }

    override fun onAnimationStart(p0: Animation?) {
        infoText!!.text = "Spinning..."
    }

    override fun onAnimationEnd(p0: Animation?) {
        infoText!!.text = prizeText
        powerButton?.isEnabled = false
        BetBtn?.isEnabled = true
    }

    override fun onAnimationRepeat(p0: Animation?) {}

    private inner class PowerTouchListener: View.OnTouchListener {
        override fun onTouch(p0: View?, motionEvent: MotionEvent?): Boolean {

            when(motionEvent!!.action){
                MotionEvent.ACTION_DOWN ->{
                    flag = true
                    count = 0
                    Thread{
                        while (flag){
                            count++
                            if (count == 100){
                                try {
                                    Thread.sleep(100)
                                }catch (e: InterruptedException) {
                                    e.printStackTrace()
                                }
                                count = 0
                            }
                            try {
                                Thread.sleep(10)
                            }
                            catch (e: InterruptedException){
                                e.printStackTrace()
                            }
                        }
                    }.start()
                    return true
                }
                MotionEvent.ACTION_UP ->{
                    flag = false
                    startSpinner()
                    return false
                }

            }


            return false
        }

    }
}