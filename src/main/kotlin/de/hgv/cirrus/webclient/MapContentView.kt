package de.hgv.cirrus.webclient

import com.vaadin.server.Page
import com.vaadin.ui.CustomComponent
import com.vaadin.ui.JavaScript
import de.hgv.cirrus.DataRepository
import de.hgv.cirrus.model.Data
import de.hgv.cirrus.model.DataType
import org.vaadin.maps.Map

class MapContentView(private val dataRepository: DataRepository): CustomComponent(), Updateable<Data> {

    private lateinit var map: Map
    private var point: Pair<Double, Double> = getCurrentPoint()

    init {
        setupMap()
        UIs.add(Data::class, this)

        addDetachListener {
            UIs.remove(Data::class, this)
        }

        Page.getCurrent().addBrowserWindowResizeListener { _ ->
            setupMap()
        }
    }

    private fun setupMap() {
        map = Map()

        Page.getCurrent().styles.add(".mymap { padding: 10px; }")
        map.styleName = "mymap"

        id = "mymapc"
        JavaScript.getCurrent().addFunction("myGetSize") {
            val width = it.getNumber(0)

            map.setWidth("${width * .95}px")
            map.setHeight("${400 * .95}px")

            val (lat, lon) = point

            var mapjs = MapContentView::class.java.getResourceAsStream("/map.js").bufferedReader().readText()
            mapjs = mapjs.replace("\$lat", lat.toString())
            mapjs = mapjs.replace("\$lon", lon.toString())
            mapjs = mapjs.replace("\$zoom", "13")

            map.setMapjs(mapjs)

            compositionRoot = map
        }
        JavaScript.getCurrent()
            .execute("myGetSize(document.getElementById('$id').clientWidth);")
    }

    override fun add(item: Data) {
        if (!ui.isAttached) {
            UIs.remove(Data::class, this)
            return
        }

        ui.access {
            if (item.type == DataType.LATITUDE) {
                point = item.value to point.second
                map.manipulateMap(
                    "window.vaadinMarkers.get('${map.getDomId()}').setLatLng([${point.first}, ${point.second}]);\n" +
                            "window.vaadinMaps.get('${map.getDomId()}').setView([${point.first}, ${point.second}]);"
                )
            } else if (item.type == DataType.LONGITUDE) {
                point = point.first to item.value
                map.manipulateMap(
                    "window.vaadinMarkers.get('${map.getDomId()}').setLatLng([${point.first}, ${point.second}]);\n" +
                            "window.vaadinMaps.get('${map.getDomId()}').setView([${point.first}, ${point.second}]);"
                )
            }
        }
    }

    private fun getCurrentPoint(): Pair<Double, Double> {
        val lat = dataRepository.findTop1ByTypeOrderByTimeDesc(DataType.LATITUDE).map { it.value }.orElse(48.1)
        val lon = dataRepository.findTop1ByTypeOrderByTimeDesc(DataType.LONGITUDE).map { it.value }.orElse(11.6)

        return lat to lon
    }

    override fun changeVisibility(visible: Boolean) {
        if (visible) {
            UIs.add(Data::class, this)
            setupMap()
        } else {
            UIs.remove(Data::class, this)
        }
    }
}