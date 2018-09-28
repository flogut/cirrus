package de.hgv.cirrus.websockets

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import de.hgv.cirrus.DataRepository
import de.hgv.cirrus.model.Data
import de.hgv.cirrus.model.DataType
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.*

class SendDataWebSocket(val dataRepository: DataRepository): TextWebSocketHandler() {

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val text = message.payload

        val cmd = text.split(" ")
        if (cmd.size == 1) {
            throw IllegalArgumentException("$cmd is no valid message. Format: <data type> <value>")
        }

        val type = DataType.get(cmd[0]) ?: throw IllegalArgumentException("${cmd[0]} is no valid type")

        val value = cmd[1].toDoubleOrNull() ?: throw IllegalArgumentException("${cmd[1]} is no double")

        val date = Date()

        var data = Data(UUID.randomUUID().toString(), type, value, date, date.time)

        data = dataRepository.save(data)

        WebSocketSessions.receiveDataSessions.forEach { it.sendMessage(TextMessage(jacksonObjectMapper().writeValueAsString(data))) }
    }

}