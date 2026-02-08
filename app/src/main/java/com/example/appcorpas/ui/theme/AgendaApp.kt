package com.example.appcorpas

// 1. IMPORTS (Esto es lo que te faltaba casi seguro)
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//modelo de dato. Necesario para que UsuarioApi no salga rojo
data class RandomUserResponse(@SerializedName("results") val results: List<UsuarioApi>)
data class UsuarioApi(val name: Name, val email: String, val cell: String, val picture: Picture)
data class Name(val first: String, val last: String)
data class Picture(val large: String, val medium: String)

// RETROFIT. para contectar a internet
interface RandomUserApi {
    @GET("api/")
    suspend fun obtenerUsuarios(@Query("results") results: Int = 20, @Query("inc") inc: String = "name,email,cell,picture"): RandomUserResponse
}

object RetrofitClient {
    val api: RandomUserApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://randomuser.me/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RandomUserApi::class.java)
    }
}

//VIEWMODEL
class AgendaViewModel(application: Application) : AndroidViewModel(application) {

    private val _usuarios = MutableStateFlow<List<UsuarioApi>>(emptyList())
    val usuarios: StateFlow<List<UsuarioApi>> = _usuarios

    init {
        cargarUsuarios()
    }

    fun cargarUsuarios() {
        viewModelScope.launch {
            try {
                // intentamos descarar con internet
                val response = RetrofitClient.api.obtenerUsuarios()

                // Si funciona, mostramos los contactos reales
                _usuarios.value = response.results

            } catch (e: Exception) {
                //SI FALLA INTERNET
                e.printStackTrace()

                // Creamos un contacto para que se vea en la pantalla
                val avisoError = UsuarioApi(
                    Name("⚠️ NO HAY", "INTERNET"),
                    "Comprueba tu conexión WiFi",
                    "Modo Offline",
                    Picture("https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/1024px-No_image_available.svg.png", "") // Foto vacía o de error
                )

                // Mostramos ese aviso en la lista
                _usuarios.value = listOf(avisoError)
            }
        }
    }
}