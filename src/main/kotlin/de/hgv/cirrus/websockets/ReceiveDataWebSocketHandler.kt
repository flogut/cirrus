package de.hgv.cirrus.websockets

import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

class ReceiveDataWebSocketHandler: TextWebSocketHandler() {

    override fun afterConnectionEstablished(session: WebSocketSession) {
        WebSocketSessions.receiveDataSessions.add(session)
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        WebSocketSessions.receiveDataSessions.remove(session)
    }



}