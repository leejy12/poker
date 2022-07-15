package com.example.poker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.poker.Data.ResponseType
import com.example.poker.Data.User
import java.io.IOException

class MyInfoActivity : AppCompatActivity() {
    lateinit var username: String
    private var user: User? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_info)
    }

    private fun initUser() {
        username = intent.getStringExtra("id").toString()
//        Log.d(TAG, "$userId")

        getUser(username!!)
    }

    private fun getUser(id: String) {

    }

}