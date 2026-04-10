package com.fsales.app.rumo.core.di

import com.fsales.app.rumo.core.data.repository.GanhoRepositoryImpl
import com.fsales.app.rumo.core.data.repository.GastoRepositoryImpl
import com.fsales.app.rumo.core.data.repository.SonhoRepositoryImpl
import com.fsales.app.rumo.core.domain.repository.GanhoRepository
import com.fsales.app.rumo.core.domain.repository.GastoRepository
import com.fsales.app.rumo.core.domain.repository.SonhoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindGanhoRepository(repository: GanhoRepositoryImpl): GanhoRepository

    @Binds
    @Singleton
    abstract fun bindGastoRepository(repository: GastoRepositoryImpl): GastoRepository

    @Binds
    @Singleton
    abstract fun bindSonhoRepository(repository: SonhoRepositoryImpl): SonhoRepository
}
