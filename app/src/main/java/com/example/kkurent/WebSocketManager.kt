package com.example.kkurent.network

import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

class WebSocketManager(private val serverUrl: String) {
    private var webSocketClient: WebSocketClient? = null

    fun connect(onMessageReceived: (String) -> Unit) {
        webSocketClient = object : WebSocketClient(URI(serverUrl)) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                println("WebSocket Connected!")
            }

            override fun onMessage(message: String?) {
                message?.let { onMessageReceived(it) }
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                println("WebSocket Closed: $reason")
            }

            override fun onError(ex: Exception?) {
                ex?.printStackTrace()
            }
        }
        webSocketClient?.connect()
    }

    fun sendMessage(message: String) {
        webSocketClient?.send(message)
    }

    fun close() {
        webSocketClient?.close()
    }
}
