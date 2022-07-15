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

        initViews()
        initListeners()
    }

    private fun initViews() {
        editId = findViewById(R.id.registerId)
        editDisplayName = findViewById(R.id.registerDisplayName)
        editPassword = findViewById(R.id.registerPassword)
        button = findViewById(R.id.registerButton)
    }

    private fun initListeners() {
        button.setOnClickListener {
            val userName = editId.text.toString()
            val displayName = editDisplayName.text.toString()
            val password = editPassword.text.toString()

            val newUser = UserRegisterRequest(userName, displayName, password)

            val call: Call<ResponseType<User>> =
                ServiceCreator.userService.postRegister(newUser)

            call.enqueue(object : Callback<ResponseType<User>> {
                override fun onResponse(
                    call: Call<ResponseType<User>>,
                    response: Response<ResponseType<User>>
                ) {
                    if (response.code() == 200) {
                        val data = response.body()?.data

                        Toast.makeText(
                            this@MyInfoActivity,
                            "${data?.userName}님 반갑습니다!",
                            Toast.LENGTH_LONG
                        ).show()
                        startActivity(Intent(this@MyInfoActivity, LoginActivity::class.java))
                    }
                    else if (response.code() == 400) {
                        Toast.makeText(
                            this@MyInfoActivity,
                            "id 또는 닉네임이 존재합니다.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    else {
                        Toast.makeText(this@MyInfoActivity, "회원가입 실패", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ResponseType<User>>, t: Throwable) {
                    Log.e("NetworkTest", "error:$t")
                }
            })
        }
    }

}