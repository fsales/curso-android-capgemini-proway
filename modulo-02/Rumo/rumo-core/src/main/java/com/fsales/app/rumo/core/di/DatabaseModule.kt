package com.fsales.app.rumo.core.di

import android.content.Context
import androidx.room.Room
import com.fsales.app.rumo.core.config.room.AppDatabase
import com.fsales.app.rumo.core.data.room.dao.GanhoDao
import com.fsales.app.rumo.core.data.room.dao.GastoDao
import com.fsales.app.rumo.core.data.room.dao.SonhoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseModule {
    companion object {

        @Provides
        @Singleton
        fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "rumo-app"
            ).fallbackToDestructiveMigration(true).build()
        }

        @Provides
        @Singleton
        fun provideGanhoDao(database: AppDatabase): GanhoDao = database.ganhoDao()

        @Provides
        @Singleton
        fun provideGastoDao(database: AppDatabase): GastoDao = database.gastoDao()

        @Provides
        @Singleton
        fun provideSonhoDao(database: AppDatabase): SonhoDao = database.sonhoDao()
    }
}
