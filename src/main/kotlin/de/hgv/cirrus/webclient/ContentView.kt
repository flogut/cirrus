package de.hgv.cirrus.webclient

import com.vaadin.ui.Component
import com.vaadin.ui.CustomComponent
import de.hgv.cirrus.DataRepository
import de.hgv.cirrus.PictureRepository
import de.hgv.cirrus.model.DataType

class ContentView(val type: DataType, val dataRepository: DataRepository, val pictureRepository: PictureRepository): CustomComponent() {

    private val contentView: Component = getContentView(type)

    init {
        setSizeFull()

        compositionRoot = contentView
    }

    private fun getContentView(dataType: DataType): Component = when(dataType) {
        DataType.PICTURE -> PictureContentView(pictureRepository)
        DataType.MAP -> MapContentView(dataRepository)
        else -> DataContentView(dataType, dataRepository)
    }

}