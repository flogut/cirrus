package de.hgv.cirrus.webclient

import com.vaadin.ui.CustomComponent
import com.vaadin.ui.Label
import de.hgv.cirrus.model.Data
import de.hgv.cirrus.model.DataType

class DataContentView(val type: DataType) : CustomComponent(), Updateable<Data> {

    val label = Label(type.toString())

    init {
        compositionRoot = label

        UIs.add(Data::class, this)
    }

    override fun add(item: Data) {
        ui.access {
            if (item.type == type) {
                label.value = "${item.type}: ${item.value}"
            }
        }
    }
}