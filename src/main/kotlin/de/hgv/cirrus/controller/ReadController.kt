package de.hgv.cirrus.controller

import de.hgv.cirrus.CirrusApplication
import de.hgv.cirrus.DataRepository
import de.hgv.cirrus.PictureRepository
import de.hgv.cirrus.model.Data
import de.hgv.cirrus.model.DataType
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import java.io.File
import java.text.SimpleDateFormat

@Controller
@RequestMapping("/")
class ReadController(val dataRepository: DataRepository,
                     val pictureRepository: PictureRepository
) {

    private val picturesDirectory = File(CirrusApplication.serverPath)

    @GetMapping("/pictures")
    @ResponseBody
    fun getAllPictures() = pictureRepository.findAll()

    @GetMapping("/pictures/{id}")
    @ResponseBody
    fun getPicture(@PathVariable id: String): ResponseEntity<ByteArray> {
        if (picturesDirectory.listFiles().any { it.nameWithoutExtension == id.toString() }) {
            val img = File("${picturesDirectory.path}\\$id.jpg")

            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(img.readBytes())
        } else {
            throw IllegalArgumentException("No picture with id $id was found")
        }
    }

    @GetMapping("/picture")
    @ResponseBody
    fun getNewestPicture(): ResponseEntity<ByteArray> {
        val picture = pictureRepository.findTop1ByOrderByTimeDesc().orElseThrow { IllegalArgumentException("No picture was uploaded yet") }

        val img = File("${picturesDirectory.path}\\${picture.id}.jpg")

        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(img.readBytes())
    }

    @GetMapping("/data")
    @ResponseBody
    fun getAllData(@RequestParam type: String? = null, @RequestParam time: String? = null): Iterable<Data> {
        val dataType = DataType.get(type)
        val date = time?.let { SimpleDateFormat("yyyy-mm-dd hh:mm:ss").parse(time) }
        return when {
            dataType == null && date == null -> dataRepository.findAll()
            dataType != null && date == null -> dataRepository.findByType(dataType)
            dataType == null && date != null -> dataRepository.findByTimeGreaterThan(date)
            dataType != null && date != null -> dataRepository.findByTypeAndTimeGreaterThan(dataType, date)
            else -> throw Exception("Mathematically guaranteed to never happen")
        }
    }

}