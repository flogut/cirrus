package de.hgv.cirrus.webclient

import com.vaadin.server.FileResource
import com.vaadin.server.Page
import com.vaadin.ui.CustomComponent
import com.vaadin.ui.Image
import de.hgv.cirrus.CirrusApplication
import de.hgv.cirrus.PictureRepository
import de.hgv.cirrus.model.Picture
import java.io.File

class PictureContentView(val pictureRepository: PictureRepository) : CustomComponent(), Updateable<Picture> {

    private val picturesDirectory = File(CirrusApplication.serverPath)

    var image: Image

    init {
        Page.getCurrent().styles.add(".myimage { padding: 10px; }")

        image = Image(null, getNewestImage())
        image.setWidth("95%")
        image.setHeight("95%")
        image.styleName = "myimage"

        compositionRoot = image

        UIs.add(Picture::class, this)
    }

    override fun add(item: Picture) {
        println("called")
        ui.access {
            image = Image(null, FileResource(File("${picturesDirectory.path}\\${item.id}.jpg")))
            image.setWidth("95%")
            image.setHeight("95%")
            image.styleName = "myimage"
            compositionRoot = image
        }
    }

    private fun getNewestImage(): FileResource? {
        val pic = pictureRepository.findTop1ByOrderByTimeDesc()

        return if (pic.isPresent) {
            FileResource(File("${picturesDirectory.path}\\${pic.get().id}.jpg"))
        } else {
            null
        }
    }
}