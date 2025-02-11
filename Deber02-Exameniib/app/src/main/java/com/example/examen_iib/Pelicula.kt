package com.example.examen_iib

import java.util.Date

data class Pelicula(
    var idPelicula: Int,
    var nombre: String?,
    var fechaEstreno: Date,
    var duracion: Int,
    var calificacion: Double,
    var basadoHistoriaReal: Boolean,
    var id_Actor: Int,
    val latitud: Double,
    val longitud: Double
)