package com.fsales.app.rumo.core.di

import com.fsales.app.rumo.core.domain.usecase.ConcluirSonhoUseCase
import com.fsales.app.rumo.core.domain.usecase.ListarGanhosPorMesUseCase
import com.fsales.app.rumo.core.domain.usecase.ListarGastosPorMesUseCase
import com.fsales.app.rumo.core.domain.usecase.ListarSonhosUseCase
import com.fsales.app.rumo.core.domain.usecase.ObterProjecaoSonhosUseCase
import com.fsales.app.rumo.core.domain.usecase.ObterSaldoMensalUseCase
import com.fsales.app.rumo.core.domain.usecase.ObterSonhoUseCase
import com.fsales.app.rumo.core.domain.usecase.SalvarGanhoUseCase
import com.fsales.app.rumo.core.domain.usecase.SalvarGastoUseCase
import com.fsales.app.rumo.core.domain.usecase.SalvarSonhoUseCase
import com.fsales.app.rumo.core.domain.usecase.impl.ConcluirSonhoUseCaseImpl
import com.fsales.app.rumo.core.domain.usecase.impl.ListarGanhosPorMesUseCaseImpl
import com.fsales.app.rumo.core.domain.usecase.impl.ListarGastosPorMesUseCaseImpl
import com.fsales.app.rumo.core.domain.usecase.impl.ListarSonhosUseCaseImpl
import com.fsales.app.rumo.core.domain.usecase.impl.ObterProjecaoSonhosUseCaseImpl
import com.fsales.app.rumo.core.domain.usecase.impl.ObterSaldoMensalUseCaseImpl
import com.fsales.app.rumo.core.domain.usecase.impl.ObterSonhoUseCaseImpl
import com.fsales.app.rumo.core.domain.usecase.impl.SalvarGanhoUseCaseImpl
import com.fsales.app.rumo.core.domain.usecase.impl.SalvarGastoUseCaseImpl
import com.fsales.app.rumo.core.domain.usecase.impl.SalvarSonhoUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    @Singleton
    abstract fun bindSalvarGanho(impl: SalvarGanhoUseCaseImpl): SalvarGanhoUseCase

    @Binds
    @Singleton
    abstract fun bindListarGanhosPorMes(impl: ListarGanhosPorMesUseCaseImpl): ListarGanhosPorMesUseCase

    @Binds
    @Singleton
    abstract fun bindSalvarGasto(impl: SalvarGastoUseCaseImpl): SalvarGastoUseCase

    @Binds
    @Singleton
    abstract fun bindListarGastosPorMes(impl: ListarGastosPorMesUseCaseImpl): ListarGastosPorMesUseCase

    @Binds
    @Singleton
    abstract fun bindListarSonhos(impl: ListarSonhosUseCaseImpl): ListarSonhosUseCase

    @Binds
    @Singleton
    abstract fun bindSalvarSonho(impl: SalvarSonhoUseCaseImpl): SalvarSonhoUseCase

    @Binds
    @Singleton
    abstract fun bindObterSaldoMensal(impl: ObterSaldoMensalUseCaseImpl): ObterSaldoMensalUseCase

    @Binds
    @Singleton
    abstract fun bindObterProjecaoSonhos(impl: ObterProjecaoSonhosUseCaseImpl): ObterProjecaoSonhosUseCase

    @Binds
    @Singleton
    abstract fun bindObterSonho(impl: ObterSonhoUseCaseImpl): ObterSonhoUseCase

    @Binds
    @Singleton
    abstract fun bindConcluirSonho(impl: ConcluirSonhoUseCaseImpl): ConcluirSonhoUseCase
}
