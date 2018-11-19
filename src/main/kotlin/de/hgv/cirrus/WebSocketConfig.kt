package de.hgv.cirrus

import de.hgv.cirrus.controller.ReadController
import de.hgv.cirrus.websockets.ReceiveDataWebSocketHandler
import de.hgv.cirrus.websockets.ReceivePicturesWebSocketHandler
import de.hgv.cirrus.websockets.SendDataWebSocket
import de.hgv.cirrus.websockets.SendPicturesWebSocket
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean

@Configuration
@EnableWebSocket
class WebSocketConfig(
    val dataRepository: DataRepository,
    val pictureRepository: PictureRepository,
    val readController: ReadController
):
    WebSocketConfigurer {

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(ReceiveDataWebSocketHandler(), "/receiveData")
        registry.addHandler(ReceivePicturesWebSocketHandler(), "/receivePictures")
        registry.addHandler(SendDataWebSocket(dataRepository), "/sendData")
        registry.addHandler(SendPicturesWebSocket(pictureRepository, readController), "/sendPictures")
    }

    @Bean
    fun createWebSocketContainer(): ServletServerContainerFactoryBean {
        val container = ServletServerContainerFactoryBean()
        container.setMaxBinaryMessageBufferSize(5 * 1024 * 1024)
        container.setMaxTextMessageBufferSize(5 * 1024 * 1024)
        container.setMaxSessionIdleTimeout(24 * 60 * 1000)

        return container
    }

//    @Bean
//    fun receiveDataWebSocketHandler(): ReceiveDataWebSocketHandler = ReceiveDataWebSocketHandler()
//
//    @Bean
//    fun receivePicturesWebSocketHandler(): ReceivePicturesWebSocketHandler = ReceivePicturesWebSocketHandler()
//
//    @Bean
//    fun sendDataWebSocketHandler(dataRepository: DataRepository): SendDataWebSocket = SendDataWebSocket(dataRepository)
//
//    @Bean
//    fun sendPicturesWebSocketHandler(pictureRepository: PictureRepository, readController: ReadController): SendPicturesWebSocket =
//        SendPicturesWebSocket(pictureRepository, readController)
}