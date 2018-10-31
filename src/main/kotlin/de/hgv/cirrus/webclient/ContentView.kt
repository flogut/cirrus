package de.hgv.cirrus.webclient

import com.vaadin.ui.Component
import com.vaadin.ui.CustomComponent
import de.hgv.cirrus.model.DataType

class ContentView(val type: DataType): CustomComponent() {

    private val contentView: Component = getContentView(type)

    init {
        compositionRoot = contentView
    }

    private fun getContentView(dataType: DataType): Component = when(dataType) {
        DataType.PICTURE -> PictureContentView()
        DataType.MAP -> MapContentView()
        else -> DataContentView(dataType)
    }

}