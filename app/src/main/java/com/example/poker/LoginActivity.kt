package com.example.poker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.poker.Data.ResponseType
import com.example.poker.Data.UserLoginRequest
import retrofit2.Call
import java.io.IOException

class LoginActivity : AppCompatActivity() {
    private val TAG: String = this.javaClass.simpleName
    private var mBinding: ActivityLoginBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initListeners()
    }

    private fun initBinding() {
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initListeners() {
        // --------------------------------- 로그인 버튼 -------------------------------------------
        binding.loginButton.setOnClickListener {
            val userName = binding.loginId.text.toString()
            val password = binding.loginPW.text.toString()

            if(userName == "" || password == "") {
                Toast.makeText(this@LoginActivity, "빈 칸이 있습니다.", Toast.LENGTH_LONG).show()
            }

            localLogin(userName, password)
        }

        binding.registerButton.setOnClickListener{
            val intent = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun localLogin(userName: String, password: String) {
        val call: Call<ResponseType<Int>> =
            ServiceCreator.userService.postLogin(UserLoginRequest(userName, password))

        Log.d(TAG, "local Login is Executed")
        login(call)
    }

    private fun login(call: Call<ResponseType<Int>>) {
        var loginFlag = false

        val thread = Thread {
            try {
                Global.currentUserId = call.execute().body()?.data
                Log.d(TAG, "currentId: ${Global.currentUserId}")
                loginFlag = true

            } catch(e: IOException) {
                loginFlag = false
            }
        }
        thread.start()

        try {
            thread.join()
            Log.d(TAG, "$loginFlag")
            if(loginFlag) {
                setJwt()

                Global.socket?.connect()
                Global.socket?.emit("login", Global.currentUserId)
            }
        } catch(e: Exception){
            e.printStackTrace()
        }
    }

    private fun setJwt() {
        Log.d(TAG, "before setJwt, currentId: ${Global.currentUserId}")
        val call: Call<ResponseType<String>> =
            ServiceCreator.jwtService.setJwt(Global.headers, JwtRequest(Global.currentUserId!!))

        var flag = false
        val thread = Thread {
            try {
                val token: String? = call.execute().body()?.data
                Log.d(TAG, "token: ${token}")
                PreferenceManager.setString(this@LoginActivity, "JWT", token!!)
                Global.headers["token"] = token
                flag = true
            } catch(e: IOException) {
                e.printStackTrace()
            }
        }
        thread.start()

        try {
            thread.join()
            Log.d(TAG, "$flag")
            if(flag) {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
            }
        } catch(e: Exception){
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}