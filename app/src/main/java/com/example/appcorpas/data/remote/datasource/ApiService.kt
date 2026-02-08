package com.example.appcorpas.data.remote.datasource

import com.example.appcorpas.data.remote.model.RespuestaApi
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    // Conecta con https://randomuser.me/api/
    @GET("api")
    suspend fun obtenerUsuarios(
        @Query("results") cantidad: Int = 50
    ): Response<RespuestaApi>
}