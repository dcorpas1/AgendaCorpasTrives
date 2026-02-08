package com.example.appcorpas.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appcorpas.data.local.entity.UsuarioEntity

/**
 * Aquí ponemos las funciones para hablar con la base de datos del movil.
 * Usamos Room para no tener que escribir SQL complicado a mano.
 */
@Dao
interface UsuarioDao {

    /**
     * Saca todos los usuarios que tenemos guardados en el móvil.
     */
    @Query("SELECT * FROM usuarios")
    suspend fun obtenerTodos(): List<UsuarioEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(usuarios: List<UsuarioEntity>)

    /**
     * Borra un usuario buscando por su ID (el email).
     */
    @Query("DELETE FROM usuarios WHERE id = :userId")
    suspend fun borrarPorId(userId: String)
}