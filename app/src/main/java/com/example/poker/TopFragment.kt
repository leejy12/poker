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
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
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
            val url = "${IP.getIP()}/player/logout/name/${Global.currentPlayerName}"
            val queue = Volley.newRequestQueue(activity)

            val stringRequest = StringRequest(
                Request.Method.POST,
                url,
                { response ->
                    if (response == "0") {
                        Log.d("logout-response", response)
                        Global.currentPlayerName = null
                        requireActivity().recreate()
                    }
                },
                { error -> Log.d("why?", error.message.toString()) }
            )

            stringRequest.setShouldCache(false)
            queue.add(stringRequest)
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