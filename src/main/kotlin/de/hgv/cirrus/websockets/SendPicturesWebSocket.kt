package de.hgv.cirrus.websockets

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import de.hgv.cirrus.CirrusApplication
import de.hgv.cirrus.PictureRepository
import de.hgv.cirrus.model.Picture
import org.springframework.web.socket.BinaryMessage
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.io.File
import java.util.*

class SendPicturesWebSocket(val pictureRepository: PictureRepository): TextWebSocketHandler() {

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        println("text")

        val bytes = Base64.getDecoder().decode(message.payload)

        val time = Date()
        val id = UUID.randomUUID().toString()

        val imageFile = File(CirrusApplication.serverPath + "\\$id.jpg")
        imageFile.writeBytes(bytes)

        val pic = pictureRepository.save(Picture(id, time, "jpg"))

        WebSocketSessions.receivePicturesSessions.forEach { it.sendMessage(TextMessage(jacksonObjectMapper().writeValueAsString(pic))) }
    }

    override fun handleBinaryMessage(session: WebSocketSession, message: BinaryMessage) {
        println("binary")

        val bytes = message.payload

        val time = Date()
        val id = UUID.randomUUID().toString()

        val imageFile = File(CirrusApplication.serverPath + "\\$id.jpg")
        imageFile.writeBytes(bytes.array())

        val pic = pictureRepository.save(Picture(id, time, "jpg"))

        WebSocketSessions.receivePicturesSessions.forEach { it.sendMessage(TextMessage(jacksonObjectMapper().writeValueAsString(pic))) }
    }
}