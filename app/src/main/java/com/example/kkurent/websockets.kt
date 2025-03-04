package com.example.kkurent

import okhttp3.*
import okio.ByteString
import android.util.Log

class ChatWebSocket {
    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null

    fun connectWebSocket() {
        val request = Request.Builder().url("ws://10.0.2.2:8080").build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("WebSocket", "Connected")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("WebSocket", "Message received: $text")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.d("WebSocket", "Closed: $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("WebSocket", "Error: ${t.message}")
            }
        })
    }

    fun sendMessage(sender: String, message: String) {
        val jsonMessage = """{"sender": "$sender", "messageText": "$message"}"""
        webSocket?.send(jsonMessage)
    }

    fun closeWebSocket() {
        webSocket?.close(1000, "Goodbye")
    }
}
