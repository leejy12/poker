package com.example.poker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView

class InTableActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var players: ArrayList<Player>
    private lateinit var playerAdapter: PlayerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_table)

        recyclerView = findViewById(R.id.playerRecyclerView)

        TODO("Establish WebSocket connection and receive players")
    }
}