package de.hgv.cirrus

import de.hgv.cirrus.model.Data
import de.hgv.cirrus.model.DataType
import de.hgv.cirrus.model.Picture
import org.springframework.data.repository.CrudRepository
import java.util.*

interface DataRepository : CrudRepository<Data, Int> {
    fun findByType(type: DataType): Iterable<Data>

    fun findByTimeGreaterThan(time: Date): Iterable<Data>

    fun findByTypeAndTimeGreaterThan(type: DataType, time: Date): Iterable<Data>
}

interface PictureRepository : CrudRepository<Picture, Int> {
    fun findTop1ByOrderByTimeDesc(): Optional<Picture>
}