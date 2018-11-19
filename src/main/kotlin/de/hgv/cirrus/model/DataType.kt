package de.hgv.cirrus.model

enum class DataType {
    PICTURE, MAP, HEIGHT, TEMPERATURE, LONGITUDE, LATITUDE, PRESSURE, DUST, VOLTAGE, INTERNAL_TEMPERATURE;

    override fun toString(): String = when(this) {
        HEIGHT -> "Höhe"
        TEMPERATURE -> "Temperatur"
        LONGITUDE -> "Breitengrad"
        LATITUDE -> "Längengrad"
        PRESSURE -> "Druck"
        DUST -> "Feinstaub"
        VOLTAGE -> "Spannung"
        INTERNAL_TEMPERATURE -> "Interne Temperatur"
        PICTURE -> "Bilder"
        MAP -> "Karte"
    }

    fun isInternal(): Boolean = when(this) {
        LONGITUDE -> true
        LATITUDE -> true
        INTERNAL_TEMPERATURE -> true
        VOLTAGE -> true
        else -> false
    }

    companion object {
        val typeMap = mapOf(
            "T" to TEMPERATURE,
            "P" to PRESSURE,
            "D" to DUST,
            "Vo" to VOLTAGE,
            "LA" to LATITUDE,
            "LO" to LONGITUDE,
            "H" to HEIGHT,
            "IT" to INTERNAL_TEMPERATURE
        )

        fun get(string: String?): DataType? = when (string) {
            "height" -> HEIGHT
            "temperature" -> TEMPERATURE
            "longitude" -> LONGITUDE
            "latitude" -> LATITUDE
            "pressure" -> PRESSURE
            "dust" -> DUST
            "voltage" -> VOLTAGE
            "internal_temperature" -> INTERNAL_TEMPERATURE
            "picture" -> PICTURE
            else -> null
        }

    }
}