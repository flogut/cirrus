package de.hgv.cirrus.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import de.hgv.cirrus.CirrusApplication
import de.hgv.cirrus.DataRepository
import de.hgv.cirrus.LineParser
import de.hgv.cirrus.PictureRepository
import de.hgv.cirrus.model.Data
import de.hgv.cirrus.model.DataType
import de.hgv.cirrus.model.Picture
import de.hgv.cirrus.webclient.UIs
import de.hgv.cirrus.websockets.WebSocketSessions
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.socket.TextMessage
import java.io.File
import java.util.*

@Controller
@RequestMapping("/")
class WriteController(
    val dataRepository: DataRepository,
    val pictureRepository: PictureRepository,
    val readController: ReadController
) {

    private val picturesDirectory = File(CirrusApplication.serverPath)

    @PostMapping("/picture")
    @ResponseBody
    fun addPicture(@RequestParam picture: MultipartFile, @RequestParam token: String): Picture {
        if (token != "authToken") {
            throw IllegalAccessException("You are not authenticated")
        }

        val id = UUID.randomUUID().toString()
        var pic = Picture(id, Date(), "jpg")

        picture.transferTo(File("${picturesDirectory.path}\\$id.jpg"))

        pic = pictureRepository.save(pic)

        readController.setNewestPicture(pic.id, picture.bytes)

        val message = TextMessage(jacksonObjectMapper().writeValueAsString(pic))

        WebSocketSessions.receivePicturesSessions.forEach { it.sendMessage(message) }

        UIs.getUpdateables(Picture::class).forEach { it.add(pic) }

        return pic
    }

    @PostMapping("/data", params=["type", "value", "token"])
    @ResponseBody
    fun addData(@RequestParam type: String, @RequestParam value: Double, @RequestParam token: String): Data {
        if (token != "authToken") {
            throw IllegalAccessException("You are not authenticated")
        }

        val dataType = DataType.get(type) ?: throw IllegalArgumentException("DataType is not valid")
        val date = Date()
        var data = Data(UUID.randomUUID().toString(), dataType, value, date, date.time)
        data = dataRepository.save(data)
        val message = TextMessage(jacksonObjectMapper().writeValueAsString(data))

        WebSocketSessions.receiveDataSessions.forEach { it.sendMessage(message) }

        UIs.getUpdateables(Data::class).forEach { it.add(data) }

        return data
    }

    @PostMapping("/data", params = ["line", "token"])
    @ResponseBody
    fun addData(@RequestParam line: String, @RequestParam token: String): Iterable<Data> {
        if (token != "authToken") {
            throw IllegalAccessException("You are not authenticated")
        }

        val dataList = dataRepository.saveAll(LineParser.parse(line))
        
        for (data in dataList) {
            val message = TextMessage(jacksonObjectMapper().writeValueAsString(data))

            WebSocketSessions.receiveDataSessions.forEach { it.sendMessage(message) }

            UIs.getUpdateables(Data::class).forEach { it.add(data) }
        }

        return dataList
    }
}