package com.example.appcorpas.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.appcorpas.data.local.dao.UsuarioDao
import com.example.appcorpas.data.local.entity.UsuarioEntity

@Database(entities = [UsuarioEntity::class], version = 1)
abstract class AgendaDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
}