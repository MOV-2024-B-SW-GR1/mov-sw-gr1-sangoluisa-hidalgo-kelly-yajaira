package com.example.examen_iib

import java.util.Date

data class Actor(
    var idActor: Int,
    var nombre: String?,
    var fechaNacimiento: Date,
    var edad: Int,
    var altura: Double,
    var tieneDobleAccion: Boolean
)