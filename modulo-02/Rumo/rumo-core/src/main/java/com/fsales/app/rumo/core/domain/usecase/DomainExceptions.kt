package com.fsales.app.rumo.core.domain.usecase

import com.fsales.app.rumo.core.domain.model.GanhoErro
import com.fsales.app.rumo.core.domain.model.GastoErro
import com.fsales.app.rumo.core.domain.model.SonhoErro

/**
 * Wrappers que transportam erros de domínio tipados em [Result.failure],
 * já que [kotlin.Result] exige [Throwable].
 *
 * A UI desempacota via `(throwable as? XErroException)?.erro`
 * e mapeia para @StringRes no próprio arquivo de Screen.
 */
class GanhoErroException(val erro: GanhoErro) : Exception(erro.toString())
class GastoErroException(val erro: GastoErro) : Exception(erro.toString())
class SonhoErroException(val erro: SonhoErro) : Exception(erro.toString())

