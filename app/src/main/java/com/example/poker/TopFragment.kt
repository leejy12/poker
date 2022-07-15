package com.example.poker

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.telecom.Call
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.poker.Data.ResponseType
import retrofit2.Callback
import retrofit2.Response

class TopFragment: Fragment() {
    val TAG: String = TopFragment::class.java.simpleName
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var myInfoButton: Button
    private lateinit var logoutButton: Button
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.fragment_top, container, false)

        // initialize views
        loginButton = view.findViewById<Button>(R.id.toLoginButton)
        registerButton = view.findViewById<Button>(R.id.toRegisterButton)
        myInfoButton = view.findViewById<Button>(R.id.toMyInfoButton)
        logoutButton = view.findViewById<Button>(R.id.toLogoutButton)
        // set up listeners
        loginButton.setOnClickListener{
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }

        registerButton.setOnClickListener {
            val intent = Intent(activity, SignupActivity::class.java)
            startActivity(intent)
        }

        myInfoButton.setOnClickListener {
            val intent = Intent(activity, MyInfoActivity::class.java)
            intent.putExtra("id", Global.currentPlayerName)
            startActivity(intent)
        }

        logoutButton.setOnClickListener {

        }

        if (Global.currentPlayerName != null) {
            loginButton.visibility = View.GONE
            registerButton.visibility = View.GONE
            myInfoButton.visibility = View.VISIBLE
            logoutButton.visibility = View.VISIBLE
        }
        else {
            loginButton.visibility = View.VISIBLE
            registerButton.visibility = View.VISIBLE
            myInfoButton.visibility = View.GONE
            logoutButton.visibility = View.GONE
        }

        return view
    }
}