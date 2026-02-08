package com.example.appcorpas.di

import android.app.Application
import androidx.room.Room
import com.example.appcorpas.data.local.dao.UsuarioDao
import com.example.appcorpas.data.local.database.AgendaDatabase
import com.example.appcorpas.data.remote.datasource.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): AgendaDatabase {
        return Room.databaseBuilder(
            app,
            AgendaDatabase::class.java,
            "agenda_db"
        ).build()
    }

    @Provides
    fun provideUsuarioDao(db: AgendaDatabase): UsuarioDao {
        return db.usuarioDao()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {

        // Creamos un cliente
        val clienteLento = okhttp3.OkHttpClient.Builder()
            .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .build()

        //Usamos ese cliente para Retrofit
        return Retrofit.Builder()
            .baseUrl("https://randomuser.me/")
            .client(clienteLento)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}