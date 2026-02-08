package com.example.appcorpas.data.repository

import com.example.appcorpas.data.local.dao.UsuarioDao
import com.example.appcorpas.data.local.entity.UsuarioEntity
import com.example.appcorpas.data.remote.datasource.ApiService
import javax.inject.Inject

/**
 * Este es el Repositorio.
 * Su trabajo es decidir si cogemos los contactos de la base de datos del móvil
 * o si tenemos que descargarlos de internet (Retrofit).
 *
 * @property apiService Para llamar a la API de RandomUser.
 * @property usuarioDao Para guardar y leer de la base de datos del móvil.
 */
class ContactRepository @Inject constructor(
    private val apiService: ApiService,
    private val usuarioDao: UsuarioDao
) {

    /**
     * Función principal para conseguir la lista de contactos.
     *
     * Lo que hace es:
     * si no le decimos que fuerce, mira primero en el móvil para que cargue rpido.
     * Si no hay nada guardado o queremos actualizar , llama a Internet.
     *
     * Si descarga cosas nuevas, las guarda automáticamente en la base de datos.
     *
     * @param forzarRecarga Si es true, pasa de lo local y se baja todo nuevo de internet.
     * @return La lista de usuarios preparada para mandarla a la pantalla.
     */
    suspend fun obtenerContactos(forzarRecarga: Boolean = false): List<UsuarioEntity> {

        // Primero miramos en cas
        if (!forzarRecarga) {
            val datosLocales = usuarioDao.obtenerTodos()
            if (datosLocales.isNotEmpty()) {
                return datosLocales
            }
        }

        // Si no hay nada, llamamos a la API
        try {
            val respuesta = apiService.obtenerUsuarios()

            if (respuesta.isSuccessful && respuesta.body() != null) {
                val listaApi = respuesta.body()!!.results

                // Pasamos los datos que vienen feos de la API al formato de nuestra base de datos
                val listaParaGuardar = listaApi.map { apiUser ->
                    UsuarioEntity(
                        id = apiUser.email,
                        nombre = "${apiUser.name.first} ${apiUser.name.last}",
                        telefono = apiUser.cell,
                        foto = apiUser.picture.large
                    )
                }

                // Guardamos en la base de datos para que estén ahí la próxima vez
                usuarioDao.insertarTodos(listaParaGuardar)
                return listaParaGuardar

            } else {
                return generarUsuarioError("Fallo en la API: ${respuesta.code()}")
            }

        } catch (e: Exception) {
            e.printStackTrace()
            // Si falla internet, por lo menos intentamos devolver lo que haya guardado
            if (forzarRecarga) {
                val datosLocales = usuarioDao.obtenerTodos()
                if (datosLocales.isNotEmpty()) return datosLocales
            }
            return generarUsuarioError("Error: ${e.message}")
        }
    }

    /**
     * Crea un usuario falso para cuando hay errores, así no se queda la pantalla en blanco
     * y el usuario sabe qué ha pasado.
     */
    private fun generarUsuarioError(mensaje: String): List<UsuarioEntity> {
        return listOf(
            UsuarioEntity(
                id = "error",
                nombre = mensaje,
                telefono = "Sin conexión",
                foto = ""
            )
        )
    }

    /**
     * Borra un contacto de la base de datos usando su ID (el email).
     * @param id El identificador del usuario que queremos borrar.
     */
    suspend fun borrarContacto(id: String) {
        usuarioDao.borrarPorId(id)
    }
}