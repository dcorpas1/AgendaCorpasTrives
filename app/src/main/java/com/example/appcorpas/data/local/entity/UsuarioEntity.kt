package com.example.appcorpas.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Avisa de que esto es una tabla de la base de datos
@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey val id: String, // Usaremos el email o ID como clave única
    val nombre: String,
    val telefono: String,
    val foto: String
)