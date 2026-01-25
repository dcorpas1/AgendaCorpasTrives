# Agenda Corpas & Trives (Fase 2)

Entrega de la primera y segunda fase del proyecto. Es una app que conecta a una API para mostrar contactos y ver sus detalles.

## Autores
* **Corpas**
* **Trives**

## Qué hemos usado
* **Lenguaje:** Kotlin + Jetpack Compose.
* **Arquitectura:** MVVM (Model-View-ViewModel).
* **Internet:** Retrofit (para la API) y Coil (para las imágenes).
* **Gestión de Errores:** Controlamos si falla la conexión.

## Lo que hace la App
1.  **Conexión API:** Descarga usuarios reales de `randomuser.me`.
2.  **Lista y Detalle:** Puedes ver la lista y pinchar para ver la foto en grande.
3.  **Modo "Sin Internet":** Si la conexión falla, la app no se cierra; muestra un aviso de error.
4.  **Botón Recargar:** Hemos añadido un botón de Refresh, arriba a la derecha para reintentar si vuelve el internet.
