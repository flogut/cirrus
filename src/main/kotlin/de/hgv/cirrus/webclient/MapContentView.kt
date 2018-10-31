package de.hgv.cirrus.webclient

import com.vaadin.ui.CustomComponent
import com.vaadin.ui.Label
import de.hgv.cirrus.model.Picture

class MapContentView : CustomComponent(), Updateable<Picture> {

    init {
        compositionRoot = Label("Karte")
    }

    override fun add(item: Picture) {
        ui.access {
            // TODO Show picture
        }
    }
}