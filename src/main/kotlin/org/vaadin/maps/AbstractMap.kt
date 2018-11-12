package org.vaadin.maps

import com.vaadin.ui.AbstractJavaScriptComponent
import com.vaadin.ui.JavaScript

abstract class AbstractMap: AbstractJavaScriptComponent() {

    private var mapId: Int = nextMapId()

    init {
        id = getDomId()
        state.domId = getDomId()
        state.mapjs = ""
    }

    override fun getState(): MapState {
        return super.getState() as MapState
    }

    fun getDomId(): String = "lmap_$mapId"

    fun setMapjs(mapjs: String) {
        state.mapjs = mapjs
    }

    fun manipulateMap(js: String) {
        JavaScript.eval("var map = window.vaadinMaps.get('${getDomId()}');\n$js")
    }

    companion object {

        @JvmStatic
        var currMapId: Int = 0

        @JvmStatic
        fun nextMapId(): Int = ++currMapId
    }
}