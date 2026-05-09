package com.fsales.app.smartcontact.di

import com.fsales.app.smartcontact.BuildConfig
import com.fsales.app.smartcontact.network.service.ViaCepService
import com.fsales.app.smartcontact.repository.CepRepository
import com.fsales.app.smartcontact.repository.CepRepositoryImpl
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val VIA_CEP_BASE_URL = "https://viacep.com.br/"

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @Binds
    @Singleton
    abstract fun bindCepRepository(impl: CepRepositoryImpl): CepRepository

    companion object {

        @Provides
        @Singleton
        fun provideJson(): Json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true   // trata null como valor default (@Serializable default)
        }

        @Provides
        @Singleton
        fun provideOkHttpClient(): OkHttpClient {
            val level = if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE

            return OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply { this.level = level })
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()
        }

        @Provides
        @Singleton
        fun provideRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit {
            val contentType = "application/json".toMediaType()
            return Retrofit.Builder()
                .baseUrl(VIA_CEP_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(json.asConverterFactory(contentType))
                .build()
        }

        @Provides
        @Singleton
        fun provideViaCepService(retrofit: Retrofit): ViaCepService =
            retrofit.create(ViaCepService::class.java)
    }
}




