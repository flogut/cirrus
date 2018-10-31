package de.hgv.cirrus.webclient

import com.vaadin.ui.CustomComponent
import com.vaadin.ui.Label
import de.hgv.cirrus.model.Picture

class PictureContentView : CustomComponent(), Updateable<Picture> {

    init {
        compositionRoot = Label("Bilder")
    }

    override fun add(item: Picture) {
        ui.access {
            // TODO Show picture
        }
    }
}