package com.example.appcorpas.data.remote.model

// Estructura completa de la respuesta de la API
data class RespuestaApi(
    val results: List<UsuarioApi>
)

// Datos del usuario que vienen de Internet
data class UsuarioApi(
    val name: Name,
    val email: String,
    val cell: String,
    val picture: Picture
)

data class Name(
    val first: String,
    val last: String
)

data class Picture(
    val large: String,
    val medium: String,
    val thumbnail: String
)