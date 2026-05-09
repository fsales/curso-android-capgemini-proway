package com.fsales.app.smartcontact.di

import android.content.Context
import androidx.room.Room
import com.fsales.app.smartcontact.data.local.dao.ContatoDao
import com.fsales.app.smartcontact.data.local.database.SmartContactDatabase
import com.fsales.app.smartcontact.repository.ContatoRepository
import com.fsales.app.smartcontact.repository.ContatoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindContatoRepository(impl: ContatoRepositoryImpl): ContatoRepository

    companion object {

        @Provides
        @Singleton
        fun provideDatabase(@ApplicationContext context: Context): SmartContactDatabase {
            return Room.databaseBuilder(
                context,
                SmartContactDatabase::class.java,
                "smart-contact-db",
            ).build()
        }

        @Provides
        @Singleton
        fun provideContatoDao(database: SmartContactDatabase): ContatoDao {
            return database.contatoDao()
        }
    }
}

