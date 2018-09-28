package de.hgv.cirrus.websockets

import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

class ReceivePicturesWebSocketHandler: TextWebSocketHandler() {

    override fun afterConnectionEstablished(session: WebSocketSession) {
        WebSocketSessions.receivePicturesSessions.add(session)
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        WebSocketSessions.receivePicturesSessions.remove(session)
    }
}