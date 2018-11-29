package de.hgv.cirrus

import de.hgv.cirrus.model.Data
import de.hgv.cirrus.model.DataType
import org.slf4j.LoggerFactory
import java.util.*

object LineParser {

    val LOGGER = LoggerFactory.getLogger(LineParser::class.java)

    fun parse(line: String): List<Data> {
        val date = Date()
        val types = DataType.typeMap

        val list = line
            .split(",")
            .asSequence()
            .map { it.split(":") }
            .filter { it.size == 2 }
            .map { (type, value) -> types[type] to value }
            .filter { (type, _) -> type != null }
            .map { (type, value) -> type to value.toDoubleOrNull() }
            .filter { (_, value) -> value != null }
            .map { (type, value) -> Data(UUID.randomUUID().toString(), type!!, value!!, date, date.time) }
            .toList()

        LOGGER.info("Nachricht enthält ${list.size} Werte")

        return list
    }

}