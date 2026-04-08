package com.fsales.app.e_aluno.data.di

import com.fsales.app.e_aluno.data.AlunoDao
import com.fsales.app.e_aluno.data.AlunoFakeDao
import com.fsales.app.e_aluno.data.AlunoRepositoryImpl
import com.fsales.app.e_aluno.domain.AlunoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindAlunoRepository(impl: AlunoRepositoryImpl): AlunoRepository

    @Binds
    @Singleton
    abstract fun bindAlunoDao(
        impl: AlunoFakeDao
    ): AlunoDao
}