package com.fsales.app.rumo.core.data.room.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.fsales.app.rumo.core.data.room.entity.enums.TipoGanho
import java.math.BigDecimal
import java.time.LocalDate

@Entity(
    tableName = "ganhos",
    indices = [
        Index(value = ["anoReferencia", "mesReferencia"]),
        Index(value = ["dataRecebimento"])
    ]
)
data class GanhoEntity(
    @field:PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val descricao: String,
    val valor: BigDecimal,
    val dataRecebimento: LocalDate,
    val mesReferencia: Int,
    val anoReferencia: Int,
    val tipo: TipoGanho,
    val recorrente: Boolean = false,
    val observacao: String? = null
)