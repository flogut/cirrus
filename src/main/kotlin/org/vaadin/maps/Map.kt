package org.vaadin.maps

import com.vaadin.annotations.JavaScript
import com.vaadin.annotations.StyleSheet

@StyleSheet("leaflet.css")
@JavaScript("leaflet.js", "maps-connector.js", "images/marker-icon.png", "images/marker-shadow.png")
class Map : AbstractMap()