package de.hgv.cirrus.websockets

import org.springframework.web.socket.WebSocketSession

object WebSocketSessions {

    val receiveDataSessions: MutableList<WebSocketSession> = mutableListOf<WebSocketSession>()
    val receivePicturesSessions: MutableList<WebSocketSession> = mutableListOf<WebSocketSession>()
}
