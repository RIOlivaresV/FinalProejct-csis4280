package com.example.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.io.InputStream
import java.net.URISyntaxException
import io.socket.client.IO
import io.socket.emitter.Emitter
import io.socket.client.Socket
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject

class login : AppCompatActivity() {
    var mSocket: Socket? = null
    private var url: String = "http://100.25.40.176:5000/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var string: String?
        try {
            val inputStream: InputStream = assets.open("source.txt")
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            string = String(buffer)
            url = string
        } catch (e: Exception) {
            Log.d("error", e.message.toString())
        }

        try {
            mSocket = IO.socket(url)
        } catch (e: URISyntaxException) {
            Log.d("URI error", e.message.toString())
        }

        try {
            mSocket?.connect()
        } catch (e: Exception) {
            textViewResult.text = " Failed to connect. " + e.message
        }

//        mSocket?.on(Socket.EVENT_CONNECT, Emitter.Listener {
//            mSocket?.emit("messages", "hi")
//        });

        mSocket?.on("loginAction", onLoginSuccess)

        buttonLogin.setOnClickListener {
            val email = txtEmail.text.toString()
            val password = txtPassword.text.toString()
            val json : String = "{'email': ${email}, 'password': ${password}}"
            val userJson = JSONObject(json)
            textViewResult.text = json
            mSocket?.emit("authentication", userJson)
        }

    }

    var onLoginSuccess = Emitter.Listener {
        val data =it[0] as Boolean
        if (data)
            startActivity(Intent(this, MainActivity::class.java))
        else
            textViewResult.text="Email or password are wrong. Try with another one.";
    }

    override fun onDestroy() {
        super.onDestroy()
        mSocket?.disconnect()
    }
}