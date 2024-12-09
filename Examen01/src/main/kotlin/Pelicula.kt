package org.example

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.example.Actor.Companion
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

data class Pelicula(
    var nombrePelicula: String,
    var fechaEstreno: Date,
    var duracion: Int,
    var calificacion: Double,
    var basadoHistoriaReal: Boolean
) {
    companion object {
        private const val archivoPath = "src/main/resources/archivoExamen1B.json"
        private val gson = Gson()
        private val dateFormat = SimpleDateFormat("yyyy-MM-dd")

        //cargamos peliculas ya existentes en el json
        fun cargarPeliculas(): MutableList<Pelicula> {
            val file = File(archivoPath)
            return if (file.exists()) {
                val jsonText = file.readText()
                val type = object : TypeToken<MutableList<Pelicula>>() {}.type
                gson.fromJson(jsonText, type) ?: mutableListOf()
            } else {
                mutableListOf()
            }
        }

        fun guardarPeliculas(peliculas: List<Pelicula>) {
            val file = File(Pelicula.archivoPath)
            val json = Pelicula.gson.toJson(peliculas)
            file.writeText(json)
            println("Peliculas guardadas exitosamente en el archivo JSON")
            cargarPeliculas()
        }
    }
}

