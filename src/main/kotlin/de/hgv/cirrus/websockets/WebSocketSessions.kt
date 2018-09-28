package de.hgv.cirrus.websockets

import org.springframework.web.socket.WebSocketSession

object WebSocketSessions {

    val receiveDataSessions = mutableListOf<WebSocketSession>()
    val receivePicturesSessions = mutableListOf<WebSocketSession>()
}
