package com.example.poker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.poker.Data.ResponseType
import com.example.poker.Data.User
import com.example.poker.Data.UserRegisterRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyInfoActivity : AppCompatActivity() {
    lateinit var editId: EditText
    lateinit var editDisplayName: EditText
    lateinit var editPassword: EditText
    lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_info)
    }
}