package com.yeidckon.practica06_recycler
data class Contacto(
    val id: Int,
    val nombre: String,
    val telefono: String
)

// Función para simular una base de datos o API devolviendo una lista
fun obtenerContactosDummy(): List<Contacto> {
    return listOf(
        Contacto(1, "Yeidckon Lugo", "123-456-7890"),
        Contacto(2, "Luis Humberto", "098-765-4321"),
        Contacto(3, "Pito Perez", "101-292-3838"),
        Contacto(4, "El de las aguas", "321-321-3210"),
        Contacto(5, "Doña Chuy", "123-123-1234")
    )
}