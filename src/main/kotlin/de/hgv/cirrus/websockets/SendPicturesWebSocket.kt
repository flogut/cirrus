package de.hgv.cirrus.websockets

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import de.hgv.cirrus.CirrusApplication
import de.hgv.cirrus.PictureRepository
import de.hgv.cirrus.controller.ReadController
import de.hgv.cirrus.model.Picture
import de.hgv.cirrus.webclient.UIs
import org.springframework.web.socket.BinaryMessage
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.io.File
import java.util.*

class SendPicturesWebSocket(
    private val pictureRepository: PictureRepository,
    private val readController: ReadController
): TextWebSocketHandler() {

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val bytes = Base64.getDecoder().decode(message.payload)

        handlePicture(bytes)
    }

    override fun handleBinaryMessage(session: WebSocketSession, message: BinaryMessage) {
        val bytes = message.payload

        handlePicture(bytes.array())
    }

    private fun handlePicture(bytes: ByteArray) {
        val time = Date()
        val id = UUID.randomUUID().toString()

        val imageFile = File(CirrusApplication.serverPath + "\\$id.jpg")
        imageFile.writeBytes(bytes)

        val pic = pictureRepository.save(Picture(id, time, "jpg"))

        readController.setNewestPicture(pic.id, bytes)

        val textMessage = TextMessage(jacksonObjectMapper().writeValueAsString(pic))
        WebSocketSessions.receivePicturesSessions.forEach { it.sendMessage(textMessage) }

        UIs.getUpdateables(Picture::class).forEach { it.add(pic) }
    }
}