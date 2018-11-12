package de.hgv.cirrus.model

import java.util.*
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "data")
data class Data(
    @Id val id: String,
    @Enumerated(EnumType.ORDINAL) val type: DataType,
    val value: Double,
    val time: Date,
    val timeMillis: Long
)