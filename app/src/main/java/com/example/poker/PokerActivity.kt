package com.example.poker

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.slider.Slider
import okhttp3.*
import org.json.JSONObject
import org.json.JSONTokener
import java.lang.Float.min

class PokerActivity : AppCompatActivity() {
    private val serverUrl = "ws://192.249.18.128/websockets"

    // game stuff
    private lateinit var gameId: String
    private var player = 0 // am I player 1 or player 2?
    private var myTurn = false
    private var dealer = 0

    private var myStack = 0
    private var opStack = 0
    private var myBet = 0
    private var opBet = 0
    private var startStack = Global.currentPlayerMoney
    private lateinit var myCard: String
    private lateinit var opName: String
    private lateinit var opCard: String

    // UI stuff
    private lateinit var opNameTextView: TextView

    private lateinit var myStackTextView: TextView
    private lateinit var opStackTextView: TextView

    private lateinit var myBetTextView: TextView
    private lateinit var opBetTextView: TextView

    private lateinit var myCardTextView: TextView
    private lateinit var opCardTextView: TextView

    private lateinit var myCardPaper: ImageView

    private lateinit var raiseBtn: Button
    private lateinit var checkCallBtn: Button
    private lateinit var foldBtn: Button

    private lateinit var searchGameDlg: Dialog
    private lateinit var raiseDlg: Dialog
    private lateinit var gameOverDlg: Dialog

    // WebSocket stuff
    private lateinit var client: OkHttpClient
    private lateinit var ws: WebSocket

    private val suitToSymbol: Map<String, String> = mapOf("S" to "♠", "H" to "♥")
    private fun numToStr(num: Int): String {
        return if (num == 1)
            "A"
        else
            num.toString()
    }

    private fun cardToTextView(tv: TextView, card: String) {
        val sp = card.split("-")
        val cardNum = sp[0].toInt()
        when (sp[1]) {
            "S" -> tv.setTextColor(applicationContext.getColor(R.color.black))
            "H" -> tv.setTextColor(applicationContext.getColor(R.color.red))
        }
        tv.text = "${numToStr(cardNum)}${suitToSymbol[sp[1]]}"
    }

    private inner class PokerWebSocketListener : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            val msg = JSONTokener(text).nextValue() as JSONObject

            when (msg.getString("type")) {
                "waiting" -> {
                    runOnUiThread {
                        searchGameDlg.show()
                    }
                }
                "exit" -> {
                    runOnUiThread {
                        val earn = myStack - startStack
                        if (earn >= 0) {
                            gameOverDlg.findViewById<TextView>(R.id.diff).setTextColor(applicationContext.getColor(R.color.green))
                        }
                        else {
                            gameOverDlg.findViewById<TextView>(R.id.diff).setTextColor(applicationContext.getColor(R.color.red))
                        }
                        gameOverDlg.findViewById<TextView>(R.id.diff).text = "${if (earn >= 0) "+" else ""}$earn"
                        gameOverDlg.findViewById<TextView>(R.id.stackChange).text = "$startStack → $myStack"
                        gameOverDlg.show()
                    }
                }
                "gameStart" -> {
                    runOnUiThread {
                        if (searchGameDlg.isShowing)
                            searchGameDlg.dismiss()

                        gameId = msg.getString("gameId")

                        player = msg.getInt("player")
                        dealer = msg.getInt("dealer")
                        myTurn = player == dealer

                        raiseBtn.text = "RAISE"
                        checkCallBtn.text = "CHECK"
                        if (myTurn) {
                            checkCallBtn.isEnabled = true
                            raiseBtn.isEnabled = true
                        }
                        else {
                            checkCallBtn.isEnabled = false
                            foldBtn.isEnabled = false
                            raiseBtn.isEnabled = false
                        }

                        myCard = msg.getString("card")
                        myBet = 100
                        myStack -= myBet

                        val op = msg.getJSONObject("opponent")
                        opName = op.getString("name")
                        opBet = 100
                        opStack = op.getInt("stack") - opBet
                        opCard = op.getString("card")

                        myBetTextView.text = myBet.toString()
                        opBetTextView.text = opBet.toString()

                        opNameTextView.text = opName
                        opStackTextView.text = opStack.toString()
                        myStackTextView.text = myStack.toString()

                        cardToTextView(myCardTextView, myCard)
                        cardToTextView(opCardTextView, opCard)
                    }
                }
                "moveResult" -> {
                    /**
                     * {
                     *   "type": "moveResult",
                     *   "moveResult": {
                     *     "player": 1 or 2,
                     *     "status": 0, 1, 2 or 3
                     *     "stack1", "bet1", "stack2", "bet2": int
                     *   }
                     * }
                     */
                    runOnUiThread {
                        val moveResult = msg.getJSONObject("moveResult")
                        val lastPlayer = moveResult.getInt("player")
                        val status = moveResult.getInt("status")
                        myStack = moveResult.getInt("stack$player")
                        opStack = moveResult.getInt("stack${3 - player}")
                        myBet = moveResult.getInt("bet$player")
                        opBet = moveResult.getInt("bet${3 - player}")

                        myStackTextView.text = myStack.toString()
                        opStackTextView.text = opStack.toString()
                        myBetTextView.text = myBet.toString()
                        opBetTextView.text = opBet.toString()

                        // betting is not over
                        if (status == 0) {
                            if (player != lastPlayer) {
                                myTurn = true
                                foldBtn.isEnabled = true
                                if (opBet > 100)
                                    checkCallBtn.text = "CALL"
                                checkCallBtn.isEnabled = true
                                raiseBtn.isEnabled = true
                                foldBtn.setTextColor(applicationContext.getColor(R.color.white))
                                checkCallBtn.setTextColor(applicationContext.getColor(R.color.white))
                                raiseBtn.setTextColor(applicationContext.getColor(R.color.white))
                            }
                            else {
                                // It is not my turn, so deactivate all buttons
                                foldBtn.isEnabled = false
                                checkCallBtn.isEnabled = false
                                raiseBtn.isEnabled = false
                                foldBtn.setTextColor(applicationContext.getColor(R.color.gray))
                                checkCallBtn.setTextColor(applicationContext.getColor(R.color.gray))
                                raiseBtn.setTextColor(applicationContext.getColor(R.color.gray))
                            }
                        }
                        // betting is over. Showdown!
                        else {
                            // reveal my card
                            myCardPaper.setBackgroundResource(R.drawable.card_front)
                            myCardTextView.visibility = View.VISIBLE
                        }
                    }
                }
                "newGame" -> {
                    runOnUiThread {
                        // hide both cards
                        myCardTextView.visibility = View.INVISIBLE
                        myCardPaper.setBackgroundResource(R.drawable.card_back)
                        println("new game!")

                        // extract json values
                        dealer = msg.getInt("dealer")
                        myCard = msg.getString("card")
                        myBet = msg.getInt("bet")
                        myStack = msg.getInt("stack")
                        opCard = msg.getJSONObject("opponent").getString("card")
                        opBet = msg.getJSONObject("opponent").getInt("bet")
                        opStack = msg.getJSONObject("opponent").getInt("stack")

                        // set up UI
                        if (player == dealer) {
                            myTurn = true
                            checkCallBtn.isEnabled = true
                            checkCallBtn.text = "CHECK"
                            foldBtn.isEnabled = true
                            raiseBtn.isEnabled = true
                            foldBtn.setTextColor(applicationContext.getColor(R.color.white))
                            checkCallBtn.setTextColor(applicationContext.getColor(R.color.white))
                            raiseBtn.setTextColor(applicationContext.getColor(R.color.white))
                        }
                        else {
                            myTurn = false
                            checkCallBtn.isEnabled = false
                            foldBtn.isEnabled = false
                            raiseBtn.isEnabled = false
                            foldBtn.setTextColor(applicationContext.getColor(R.color.gray))
                            checkCallBtn.setTextColor(applicationContext.getColor(R.color.gray))
                            raiseBtn.setTextColor(applicationContext.getColor(R.color.gray))
                        }

                        cardToTextView(myCardTextView, myCard)
                        cardToTextView(opCardTextView, opCard)

                        myBetTextView.text = myBet.toString()
                        opBetTextView.text = opBet.toString()
                        myStackTextView.text = myStack.toString()
                        opStackTextView.text = opStack.toString()
                    }
                }
            }
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            webSocket.close(1000, reason)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poker)

        opNameTextView = findViewById(R.id.opName)

        myStackTextView = findViewById(R.id.myStack)
        opStackTextView = findViewById(R.id.opStack)

        myCardTextView = findViewById(R.id.myCard)
        opCardTextView = findViewById(R.id.opCard)

        myBetTextView = findViewById(R.id.myBet)
        opBetTextView = findViewById(R.id.opBet)

        myCardPaper = findViewById(R.id.myCardPaper)

        raiseBtn = findViewById(R.id.raiseBtn)
        checkCallBtn = findViewById(R.id.checkCallBtn)
        foldBtn = findViewById(R.id.foldBtn)

        searchGameDlg = Dialog(this)
        searchGameDlg.setContentView(R.layout.search_game_dialog)
        searchGameDlg.setCancelable(false)
        searchGameDlg.setCanceledOnTouchOutside(false)
        val cancelSearchButton = searchGameDlg.findViewById<Button>(R.id.cancelSearchButton)
        cancelSearchButton.setOnClickListener {
            searchGameDlg.dismiss()
            finish()
        }

        gameOverDlg = Dialog(this)
        gameOverDlg.setContentView(R.layout.game_end_dialog)
        gameOverDlg.setCanceledOnTouchOutside(false)
        val closeButton = gameOverDlg.findViewById<Button>(R.id.closeButton)
        closeButton.setOnClickListener {
            gameOverDlg.dismiss()
            finish()
        }

        raiseDlg = Dialog(this)
        raiseDlg.setContentView(R.layout.raise_dialog)
        val slider = raiseDlg.findViewById<Slider>(R.id.slider)
        slider.stepSize = 10f
        val raiseAmount = raiseDlg.findViewById<TextView>(R.id.raiseAmount)
        val confirmRaiseBtn = raiseDlg.findViewById<Button>(R.id.confirmRaiseBtn)

        slider.addOnChangeListener { _, value, _ -> raiseAmount.text = value.toInt().toString() }

        raiseBtn.setOnClickListener {
            slider.valueFrom = min((2 * opBet).toFloat(), myStack.toFloat())
            slider.valueTo = min(myStack.toFloat(), opStack.toFloat())
            slider.value = slider.valueFrom
            raiseDlg.show()
        }

        confirmRaiseBtn.setOnClickListener {
            ws.send("{ \"gameId\": \"$gameId\", \"player\": $player, \"action\": \"RAISE ${slider.value.toInt()}\" }")
            raiseDlg.dismiss()
        }

        foldBtn.setOnClickListener {
            ws.send("{ \"gameId\": \"$gameId\", \"player\": $player, \"action\": \"FOLD\" }")
        }

        checkCallBtn.setOnClickListener {
            ws.send("{ \"gameId\": \"$gameId\", \"player\": $player, \"action\": \"${checkCallBtn.text}\" }")
        }

        client = OkHttpClient()

        // TODO: get myStack from Intent
        myStack = Global.currentPlayerMoney

        val wsConUrl = "$serverUrl?name=${Global.currentPlayerName}&stack=$myStack"
        val request = okhttp3.Request.Builder().url(wsConUrl).build()
        val listener = PokerWebSocketListener()
        ws = client.newWebSocket(request, listener)
    }

    override fun onDestroy() {
        runOnUiThread {
            super.onDestroy()
            ws.close(1000, Global.currentPlayerName)
        }
    }
}