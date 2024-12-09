package org.example

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class Actor(
    var nombre: String,
    var fechaNacimiento: Date,
    var edad: Int,
    var altura: Double,
    var tieneDobleAccion: Boolean,
    var peliculas: MutableList<Pelicula> = mutableListOf()
) {
    companion object {
        private const val archivoPath = "src/main/resources/archivoExamen1B.json"
        private val gson = Gson()
        private val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        //Cargar actores ya existentes en el archivo json
        fun cargarActores(): MutableList<Actor> {
            val file = File(archivoPath)
            return if (file.exists()) {
                val jsonText = file.readText()
                val type = object : TypeToken<MutableList<Actor>>() {}.type
                gson.fromJson(jsonText, type) ?: mutableListOf()
            } else {
                mutableListOf()
            }
        }

        fun guardarActores(actores: List<Actor>) {
            val file = File(archivoPath)
            val json = gson.toJson(actores)
            file.writeText(json)
            println("Datos guardados exitosamente en archivo JSON")
            cargarActores()
        }


        //CREATE actor
        fun crearActor( actores: MutableList<Actor>, nombreActor: String, fechaNacimiento: String, edad: Int, altura: Double, dobleAccion: Boolean, peliculas: MutableList<Pelicula>) {
        val fechaFormateada = try {
            SimpleDateFormat("yyyy-MM-dd").parse(fechaNacimiento)
        } catch (e: Exception) {
            println("Formato de fecha inválido.")
            return
        }

        // Verifica si ya existe un actor con el mismo nombre
        val actorExistente = actores.find { it.nombre == nombreActor }
        if (actorExistente != null) {
            println("El actor ya existe. Actualizando información...")
            actorExistente.fechaNacimiento = fechaFormateada
            actorExistente.edad = edad
            actorExistente.altura = altura
            actorExistente.tieneDobleAccion = dobleAccion
            actorExistente.peliculas = peliculas
        } else {
            val nuevoActor = Actor(nombreActor, fechaFormateada, edad, altura, dobleAccion, peliculas)
            actores.add(nuevoActor)
            println("ACTOR CREADO EXITOSAMENTE")
        }
    }

    //READ actor
        fun verActoresInfo(actores: List<Actor>) {
            actores.forEachIndexed { index, actor ->
                println("${index + 1}. ${actor.nombre} (${actor.edad} años, Nacido el:  ${actor.fechaNacimiento}, Altura: ${actor.altura} m, Tiene un doble de acción: ${actor.tieneDobleAccion})")
            }
        }

    //UPDATE actor
        fun editarActor(actores: MutableList<Actor>, index: Int, nuevoNombre: String, nuevaFechaNacimiento: Date, nuevaEdad: Int, nuevaAltura: Double, nuevoDobleAccion: Boolean) {
            val actor = actores[index]
            actor.nombre = nuevoNombre
            actor.fechaNacimiento = nuevaFechaNacimiento
            actor.edad = nuevaEdad
            actor.altura = nuevaAltura
            actor.tieneDobleAccion = nuevoDobleAccion
            guardarActores(actores)
            print("ACTOR EDITADO EXITOSAMENTE")
        }

    //DELETE actor
        fun eliminarActor(actores: MutableList<Actor>, index: Int) {
            actores.removeAt(index)
            guardarActores(actores)
            print("ACTOR ELIMINADO EXITOSAMENTE")
        }



    //CREATE pelicula
    fun crearPelicula(actorIndex: Int, nombrePelicula: String, fechaEstreno: String, duracion: Int, calificacion: Double, basadoHistoriaReal: Boolean) {
        val nuevaPelicula = Pelicula(nombrePelicula, dateFormat.parse(fechaEstreno), duracion, calificacion, basadoHistoriaReal)
        val actores = cargarActores()
        val actor = actores[actorIndex]

        // Añadir la película al actor correspondiente
        actor.peliculas.add(nuevaPelicula)
        guardarActores(actores)
        print("PELICULA CREADA EXITOSAMENTE")
    }


    //READ pelicula
    fun verPeliculas(peliculas: List<Pelicula>) {
        peliculas.forEachIndexed { index, pelicula ->
            println("${index + 1}. ${pelicula.nombrePelicula} (Estreno: ${pelicula.fechaEstreno}, Duración: ${pelicula.duracion} min, Calificación: ${pelicula.calificacion}, Basado en una Historia Real: ${pelicula.basadoHistoriaReal})")
        }
    }

    //UPDATE pelicula
    fun editarPelicula(actorIndex: Int, peliculaIndex: Int, nombrePelicula: String, fechaEstreno: Date, duracion: Int, calificacion: Double, basadoHistoriaReal: Boolean) {
        val actores = cargarActores()
        val pelicula = actores[actorIndex].peliculas[peliculaIndex]
        pelicula.nombrePelicula = nombrePelicula
        pelicula.fechaEstreno = fechaEstreno
        pelicula.duracion = duracion
        pelicula.calificacion = calificacion
        pelicula.basadoHistoriaReal = basadoHistoriaReal
        guardarActores(actores)
        print("PELICULA EDITADA EXITOSAMENTE")
    }
    //DELETE pelicula
    fun eliminarPelicula(actorIndex: Int, peliculaIndex: Int) {
        val actores = cargarActores()
        actores[actorIndex].peliculas.removeAt(peliculaIndex)
        guardarActores(actores)
        print("PELICULA ELIMINADA EXITOSAMENTE")
    }
    }
}

