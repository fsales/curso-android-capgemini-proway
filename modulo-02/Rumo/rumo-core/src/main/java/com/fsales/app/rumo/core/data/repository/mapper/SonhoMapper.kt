package com.fsales.app.rumo.core.data.repository.mapper

import com.fsales.app.rumo.core.data.room.entity.SonhoEntity
import com.fsales.app.rumo.core.domain.model.PrioridadeSonho
import com.fsales.app.rumo.core.domain.model.Sonho
import com.fsales.app.rumo.core.domain.model.StatusSonho
import com.fsales.app.rumo.core.data.room.entity.enums.PrioridadeSonho as PrioridadeSonhoEntity
import com.fsales.app.rumo.core.data.room.entity.enums.StatusSonho as StatusSonhoEntity

fun SonhoEntity.toDomain(): Sonho = Sonho(
    id = id,
    titulo = titulo,
    descricao = descricao,
    valorMeta = valorMeta,
    valorAtual = valorAtual,
    prioridade = prioridade.toDomain(),
    prazoAlvo = prazoAlvo,
    status = status.toDomain(),
    dataCriacao = dataCriacao,
    dataConclusao = dataConclusao
)

fun Sonho.toEntity(): SonhoEntity = SonhoEntity(
    id = id,
    titulo = titulo,
    descricao = descricao,
    valorMeta = valorMeta,
    valorAtual = valorAtual,
    prioridade = prioridade.toEntity(),
    prazoAlvo = prazoAlvo,
    status = status.toEntity(),
    dataCriacao = dataCriacao,
    dataConclusao = dataConclusao
)

private fun PrioridadeSonhoEntity.toDomain(): PrioridadeSonho = when (this) {
    PrioridadeSonhoEntity.BAIXA -> PrioridadeSonho.BAIXA
    PrioridadeSonhoEntity.MEDIA -> PrioridadeSonho.MEDIA
    PrioridadeSonhoEntity.ALTA -> PrioridadeSonho.ALTA
}

private fun PrioridadeSonho.toEntity(): PrioridadeSonhoEntity = when (this) {
    PrioridadeSonho.BAIXA -> PrioridadeSonhoEntity.BAIXA
    PrioridadeSonho.MEDIA -> PrioridadeSonhoEntity.MEDIA
    PrioridadeSonho.ALTA -> PrioridadeSonhoEntity.ALTA
}

private fun StatusSonhoEntity.toDomain(): StatusSonho = when (this) {
    StatusSonhoEntity.NAO_INICIADO -> StatusSonho.NAO_INICIADO
    StatusSonhoEntity.EM_ANDAMENTO -> StatusSonho.EM_ANDAMENTO
    StatusSonhoEntity.CONCLUIDO -> StatusSonho.CONCLUIDO
    StatusSonhoEntity.PAUSADO -> StatusSonho.PAUSADO
}

private fun StatusSonho.toEntity(): StatusSonhoEntity = when (this) {
    StatusSonho.NAO_INICIADO -> StatusSonhoEntity.NAO_INICIADO
    StatusSonho.EM_ANDAMENTO -> StatusSonhoEntity.EM_ANDAMENTO
    StatusSonho.CONCLUIDO -> StatusSonhoEntity.CONCLUIDO
    StatusSonho.PAUSADO -> StatusSonhoEntity.PAUSADO
}
