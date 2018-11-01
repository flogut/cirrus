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

    private lateinit var image: Image

    init {
        Page.getCurrent().styles.add(".myimage { padding: 10px; }")

        showImage(getNewestImage())

        UIs.add(Picture::class, this)

        addDetachListener {
            UIs.remove(Picture::class, this)
        }
    }

    override fun add(item: Picture) {
        if (!ui.isAttached) return

        ui.access {
            showImage(FileResource(File("${picturesDirectory.path}\\${item.id}.jpg")))
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

    private fun showImage(img: FileResource?) {
        image = Image(null, img)
        image.setWidth("95%")
        image.setHeight("95%")
        image.styleName = "myimage"

        compositionRoot = image
    }

    override fun changeVisibility(visible: Boolean) {
        if (visible) {
            showImage(getNewestImage())
        }
    }
}