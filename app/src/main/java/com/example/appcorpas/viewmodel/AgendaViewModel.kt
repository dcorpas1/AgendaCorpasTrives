package com.example.appcorpas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcorpas.data.local.entity.UsuarioEntity
import com.example.appcorpas.data.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel que controla la pantalla principal.
 * Se encarga de guardar la lista de contactos para que no se pierda
 * si giramos el móvil o cambiamos de app.
 */
@HiltViewModel
class AgendaViewModel @Inject constructor(
    private val repository: ContactRepository
) : ViewModel() {

    // Aquí guardamos la lista de usuarios que se ve en la pantalla
    private val _contactos = MutableStateFlow<List<UsuarioEntity>>(emptyList())
    val contactos: StateFlow<List<UsuarioEntity>> = _contactos

    init {
        // Nada más abrir cargamos los datos
        cargarDatos()
    }

    /**
     * Le pide al repositorio que nos dé los contactos.
     * @param forzar Si es true, obliga a descargar de internet
     */
    fun cargarDatos(forzar: Boolean = false) {
        viewModelScope.launch {
            try {
                val lista = repository.obtenerContactos(forzarRecarga = forzar)
                _contactos.value = lista
            } catch (e: Exception) {
                // Si falla algo, no hacemos nada por ahora
            }
        }
    }

    /**
     * Borra un contacto y actualiza la lista de la pantalla al momento.
     */
    fun borrarContacto(id: String) {
        viewModelScope.launch {
            repository.borrarContacto(id)
            // Quitamos el usuario de la lista visualmente para que desaparezca rápido
            _contactos.value = _contactos.value.filter { it.id != id }
        }
    }
}