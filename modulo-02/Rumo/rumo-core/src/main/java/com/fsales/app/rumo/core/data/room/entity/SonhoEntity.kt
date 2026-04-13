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
    // Índices para melhorar consultas por prioridade e por data de criação.
    // Adicionar um índice em `dataCriacao` ajuda a acelerar ordenações e
    // buscas que usam essa coluna (ex: listarTodos ORDER BY dataCriacao).
    indices = [
        Index(value = ["prioridade"]),
        Index(value = ["dataCriacao"]),
    ]
)
data class SonhoEntity(
    @field:PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val titulo: String,
    val descricao: String? = null,
    val valorMeta: BigDecimal,
    val prioridade: PrioridadeSonho = PrioridadeSonho.MEDIA,
    val prazoAlvo: LocalDate? = null,
    val dataCriacao: Instant = Instant.now(),
    val concluido: Boolean = false,
)

