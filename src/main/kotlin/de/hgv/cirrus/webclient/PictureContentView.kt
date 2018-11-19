package de.hgv.cirrus.webclient

import com.vaadin.server.ExternalResource
import com.vaadin.server.Page
import com.vaadin.server.Resource
import com.vaadin.ui.CustomComponent
import com.vaadin.ui.Image
import de.hgv.cirrus.CirrusApplication
import de.hgv.cirrus.PictureRepository
import de.hgv.cirrus.model.Picture
import java.io.File

class PictureContentView(private val pictureRepository: PictureRepository): CustomComponent(), Updateable<Picture> {

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
        if (!ui.isAttached) {
            UIs.remove(Picture::class, this)
            return
        }

        ui.access {
            showImage(ExternalResource("${Page.getCurrent().location}/pictures/${item.id}", "image/jpeg"))
        }
    }

    private fun getNewestImage(): Resource? {
        val pic = pictureRepository.findTop1ByOrderByTimeDesc()

        return if (pic.isPresent) {
//            FileResource(File("${picturesDirectory.path}\\${pic.get().id}.jpg"))
            ExternalResource("${Page.getCurrent().location}/pictures/${pic.get().id}", "image/jpeg")
        } else {
            null
        }
    }

    private fun showImage(img: Resource?) {
        image = Image(null, img)
        image.setWidth("95%")
        image.setHeight("95%")
        image.styleName = "myimage"

        compositionRoot = image
    }

    override fun changeVisibility(visible: Boolean) {
        if (visible) {
            UIs.add(Picture::class, this)
            showImage(getNewestImage())
        } else {
            UIs.remove(Picture::class, this)
        }
    }
}