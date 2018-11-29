package de.hgv.cirrus.websockets

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import de.hgv.cirrus.DataRepository
import de.hgv.cirrus.LineParser
import de.hgv.cirrus.model.Data
import de.hgv.cirrus.model.DataType
import de.hgv.cirrus.webclient.UIs
import org.slf4j.LoggerFactory
import org.springframework.web.socket.BinaryMessage
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.*

class SendDataWebSocket(private val dataRepository: DataRepository): TextWebSocketHandler() {

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val text = message.payload
        LOGGER.info("Text-Nachricht empfangen: $text")
        if (text.matches(Regex("""\w+ -?\d+.?\d+"""))) {
            LOGGER.info("Nachricht ist vom Typ 1")
            handleData(text)
        } else {
            LOGGER.info("Nachricht ist vom Typ 2")
            handleLine(text)
        }
    }

    override fun handleBinaryMessage(session: WebSocketSession, message: BinaryMessage) {
        LOGGER.info("Binäre Nachricht empfangen: ${message.payload.array().joinToString()}")
        LOGGER.info("Binäre Nachrichten werden momentan noch nicht bearbeitet => An Flo wenden")
    }

    private fun handleData(text: String) {
        val cmd = text.split(" ")
        if (cmd.size != 2) {
            throw IllegalArgumentException("$cmd is no valid message. Format: <data type> <value>")
        }

        val type = DataType.get(cmd[0]) ?: throw IllegalArgumentException("${cmd[0]} is no valid type")

        val value = cmd[1].toDoubleOrNull() ?: throw IllegalArgumentException("${cmd[1]} is no double")

        val date = Date()

        var data = Data(UUID.randomUUID().toString(), type, value, date, date.time)

        data = dataRepository.save(data)
        val textMessage = TextMessage(jacksonObjectMapper().writeValueAsString(data))

        WebSocketSessions.receiveDataSessions.forEach { it.sendMessage(textMessage) }

        UIs.getUpdateables(Data::class).forEach { it.add(data) }
    }

    fun handleLine(text: String) {
        val dataList = dataRepository.saveAll(LineParser.parse(text))

        for (data in dataList) {
            val textMessage = TextMessage(jacksonObjectMapper().writeValueAsString(data))

            WebSocketSessions.receiveDataSessions.forEach { it.sendMessage(textMessage) }

            UIs.getUpdateables(Data::class).forEach { it.add(data) }
        }
    }

    companion object {
        val LOGGER = LoggerFactory.getLogger(SendDataWebSocket::class.java)
    }

}