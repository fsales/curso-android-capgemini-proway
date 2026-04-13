package com.fsales.app.rumo.core.data.room.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.fsales.app.rumo.core.data.room.entity.enums.PrioridadeSonho
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate

@Entity(
    tableName = "sonhos",
    indices = [Index(value = ["prioridade"])]
)
data class SonhoEntity(
    @field:PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val titulo: String,
    val descricao: String? = null,
    val valorMeta: BigDecimal,
    val valorAtual: BigDecimal = BigDecimal.ZERO,
    val prioridade: PrioridadeSonho = PrioridadeSonho.MEDIA,
    val prazoAlvo: LocalDate? = null,
    val dataCriacao: Instant = Instant.now(),
)

