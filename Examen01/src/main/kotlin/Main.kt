package org.example

import org.example.Actor.Companion.cargarActores
import java.text.SimpleDateFormat
import java.util.Scanner
import kotlin.system.exitProcess

import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream

val dateFormat = SimpleDateFormat("yyyy-MM-dd")
fun main() {
    val actores = Actor.cargarActores()
    val peliculas = Pelicula.cargarPeliculas()
    val scanner = Scanner(System.`in`)
    var salir = false

    while (!salir) {
        println("\n---------------BIENVENIDO-------------")
        println("Menú Principal:")
        println("1. Ver todos los Actores")
        println("2. Crear Actor")
        println("3. Editar Actor")
        println("4. Eliminar Actor")
        println("5. Descargar Datos en formato PDF")
        println("6. Descargar Datos en formato TXT")
        println("7. Salir")

        print("Elige una opción: ")
        when (scanner.nextInt()) {
            1 -> {
                Actor.verActoresInfo(actores)
                print("Seleccione el número del actor para gestionar sus películas: (Si desea volver ingrese 0) ")
                val indexActor = scanner.nextInt() - 1
                if (indexActor in actores.indices) {
                    menuPelicula(actores, indexActor)
                } else {
                    println("Ha regresado al menu Principal")
                }
            }

            2 -> {
                print("Ingrese el nombre del actor: ")
                val nombreActor = scanner.next()
                print("Ingrese la fecha de nacimiento (yyyy-MM-dd): ")
                val fechaNacimiento = scanner.next()
                print("Ingrese la edad: ")
                val edad = scanner.nextInt()
                print("Ingrese la altura (en m): ")
                val altura = scanner.nextDouble()
                print("¿Tiene doble acción? (Sí/No): ")
                val tieneDobleAccion = scanner.next().equals("Sí", ignoreCase = true)

                println("Creando actor.... ")
                val peliculasActor = mutableListOf<Pelicula>()

                print("Ingrese el número de películas: ")
                val numPeliculas = scanner.nextInt()
                val nuevasPeliculas = mutableListOf<Pelicula>()
                for (i in 1..numPeliculas) {

                    println("Datos de la película $i:")
                    print("Nombre: ")
                    val nombrePelicula = scanner.next()

                    print("Fecha de estreno (yyyy-MM-dd): ")
                    val fechaEstreno = scanner.next()

                    print("Duración (en minutos): ")
                    val duracion = scanner.nextInt()
                    print("Calificación (1,0 - 10,0): ")
                    val calificacion = scanner.nextDouble()
                    print("¿Esta basada en una historia real? (Sí/No): ")
                    val basadoHistoriaReal = scanner.next().equals("Sí", ignoreCase = true)

                    val nuevaPelicula = Pelicula(nombrePelicula, dateFormat.parse(fechaEstreno), duracion, calificacion, basadoHistoriaReal)
                    nuevasPeliculas.add(nuevaPelicula)
                }

                Actor.crearActor(actores, nombreActor, fechaNacimiento, edad, altura, tieneDobleAccion, nuevasPeliculas)

                cargarActores()
                Pelicula.cargarPeliculas()
            }
            3 -> {
                Actor.verActoresInfo(actores)
                print("Seleccione el número del actor a editar: ")
                val indexActor = scanner.nextInt() - 1

                if (indexActor in 0 until actores.size) {
                    val actor = actores[indexActor]

                    print("Ingrese el nuevo nombre del actor: ")
                    val nuevoNombre = scanner.next()

                    print("Ingrese la nueva fecha de nacimiento (yyyy-MM-dd): ")
                    val nuevaFechaNacimiento = scanner.next()

                    print("Ingrese la nueva edad: ")
                    val nuevaEdad = scanner.nextInt()

                    print("Ingrese la nueva altura: ")
                    val nuevaAltura = scanner.nextDouble()

                    print("¿Tiene doble acción? (Sí/No): ")
                    val nuevoDobleAccion = scanner.next().equals("Sí", ignoreCase = true)

                    Actor.editarActor(actores, indexActor , nuevoNombre, dateFormat.parse(nuevaFechaNacimiento), nuevaEdad, nuevaAltura, nuevoDobleAccion)

                    //cargarActores()
                    cargarActores()
                    Pelicula.cargarPeliculas()


                } else {
                    println("No existe el actor con el nombre ingresado.")
                }
            }
            4 -> {
                Actor.verActoresInfo(actores)
                print("Seleccione el número del actor a eliminar: ")
                val indexActor = scanner.nextInt() - 1

                if (indexActor in 0 until actores.size) {
                    Actor.eliminarActor(actores,indexActor)
                     print("AActor eliminado")

                    cargarActores()

                } else {
                    println("No existe el actor ingresado.")
                }
            }
            5 -> {
                val filePath = "src/main/resources/DatosdelArchivoenFormatoPDF.pdf"
                try {
                    exportarDatosPDF(actores, filePath)
                    println("Datos exportados exitosamente en formato PDF: $filePath")
                } catch (e: Exception) {
                    println("Lo sentimos, sucedio un error en la descarga.")
                }
            }
            6 ->{
                val filePath = "src/main/resources/DatosdelArchivoenFormatoTXT.txt"
                try {
                    exportarDatosTXT(actores, filePath)
                    println("Datos exportados exitosamente en formato TXT: $filePath")
                } catch (e: Exception) {
                    println("Lo sentimos, sucedio un error en la descarga.")
                }
            }
            7 -> {
                salir = true
            }
            else -> {
                println("Opción no válida.")
            }
        }
    }
}

fun menuPelicula(actores: MutableList<Actor>, indexActor: Int) {
    val actor = actores[indexActor]
    val scanner = java.util.Scanner(System.`in`)
    var salir = false

    while (!salir) {
        println("\n--------------------------------------------------")
        println("Menú de Películas para el actor ${actor.nombre}:")
        println("1. Ver Películas")
        println("2. Crear Película")
        println("3. Editar Película")
        println("4. Eliminar Película")
        println("5. Regresar al menú principal")
        println("6. Salir del programa")

        print("Seleccione una opción: ")
        when (scanner.nextInt()) {
            1 -> Actor.verPeliculas(actor.peliculas)
            2 -> {
                print("Ingrese el nombre de la película: ")
                val nombrePelicula = scanner.next()

                print("Ingrese la fecha de estreno (yyyy-MM-dd): ")
                val fechaEstreno = scanner.next()

                print("Ingrese la duración de la película en minutos: ")
                val duracion = scanner.nextInt()

                print("Ingrese la calificación de la película: (1,0 - 10,0) ")
                val calificacion = scanner.nextDouble()

                print("¿Esta basada en una historia real? (Sí/No): ")
                val basadoHistoriaReal = scanner.next().equals("Si", ignoreCase = true)

                val nuevaPelicula = Pelicula(nombrePelicula, dateFormat.parse(fechaEstreno), duracion, calificacion, basadoHistoriaReal)

                actor.peliculas.add(nuevaPelicula)

                Actor.crearPelicula(indexActor, nombrePelicula, fechaEstreno, duracion, calificacion, basadoHistoriaReal)

                cargarActores()
                Pelicula.cargarPeliculas()


            }
            3 -> {
                Actor.verPeliculas(actor.peliculas)
                print("Seleccione el número de la película a editar: ")
                val indexPelicula = scanner.nextInt() - 1

                if (indexPelicula in 0 until actor.peliculas.size) {
                    val pelicula = actor.peliculas[indexPelicula]

                    print("Ingrese el nuevo nombre de la película (o presione Enter para mantener el actual): ")
                    val nuevoNombre = scanner.next()
                    if (nuevoNombre.isNotEmpty()) {
                        pelicula.nombrePelicula = nuevoNombre
                    }

                    print("Ingrese la nueva fecha de estreno (yyyy-MM-dd) (o presione Enter para mantener la actual): ")
                    val nuevaFechaEstreno = scanner.next()
                    if (nuevaFechaEstreno.isNotEmpty()) {
                        pelicula.fechaEstreno = java.text.SimpleDateFormat("yyyy-MM-dd").parse(nuevaFechaEstreno)
                    }

                    print("Ingrese la nueva duración en minutos (o presione Enter para mantener la actual): ")
                    val nuevaDuracion = scanner.next()
                    if (nuevaDuracion.isNotEmpty()) {
                        pelicula.duracion = nuevaDuracion.toInt()
                    }

                    print("Ingrese la nueva calificación (1.0 - 10.0) (o presione Enter para mantener la actual): ")
                    val nuevaCalificacion = scanner.next()
                    if (nuevaCalificacion.isNotEmpty()) {
                        pelicula.calificacion = nuevaCalificacion.toDouble()
                    }

                    print("¿Está basada en una historia real? (Sí/No) (o presione Enter para mantener la actual): ")
                    val basadoHistoriaRealInput = scanner.next()
                    if (basadoHistoriaRealInput.isNotEmpty()) {
                        pelicula.basadoHistoriaReal = basadoHistoriaRealInput.equals("Sí", ignoreCase = true)
                    }


                    Actor.editarPelicula(indexActor, indexPelicula, pelicula.nombrePelicula, pelicula.fechaEstreno, pelicula.duracion, pelicula.calificacion, pelicula.basadoHistoriaReal)
                   // println("PELICULA EDITADA EXITOSAMENTE.")
                    cargarActores()

                } else {
                    println("Índice de película inválido.")
                }
            }
            4 -> {
                Actor.verPeliculas(actor.peliculas)
                print("Seleccione el número de la película a eliminar: ")
                val indexPelicula = scanner.nextInt() - 1
                if (indexPelicula in 0 until actor.peliculas.size) {
                    Actor.eliminarPelicula(indexActor, indexPelicula)
                    actor.peliculas.removeAt(indexPelicula)
                   // println("PELICULA ELIMINADA EXITOSAMENTE.")
                    cargarActores()

                } else {
                    println("Índice de película inválido.")
                }
            }
            5 -> salir = true
            6 -> {
                println("Saliendo del programa...")
                exitProcess(0)

            }
            else -> println("Opción inválida. Por favor, seleccione una opción válida.")
        }
    }
}


fun exportarDatosPDF(actores: List<Actor>, filePath: String) {
    val document = Document()
    PdfWriter.getInstance(document, FileOutputStream(filePath))
    document.open()
    document.add(Paragraph("Lista de Actores"))
    document.add(Paragraph(" "))
    actores.forEach { actor ->
        document.add(Paragraph("Nombre: ${actor.nombre}"))
        document.add(Paragraph("Fecha de Nacimiento: ${actor.fechaNacimiento}"))
        document.add(Paragraph("Edad: ${actor.edad} años"))
        document.add(Paragraph("Altura: ${actor.altura} m"))
        document.add(Paragraph("Tiene Doble Acción: ${if (actor.tieneDobleAccion) "Sí" else "No"}"))
        document.add(Paragraph("Películas:"))
        actor.peliculas.forEach { pelicula ->
            document.add(Paragraph("    Título: ${pelicula.nombrePelicula}"))
            document.add(Paragraph("    Estreno: ${pelicula.fechaEstreno}"))
            document.add(Paragraph("    Duración: ${pelicula.duracion} min"))
            document.add(Paragraph("    Calificación: ${pelicula.calificacion}"))
            document.add(Paragraph("    Basado en una Historia Real: ${if (pelicula.basadoHistoriaReal) "Sí" else "No"}"))
            document.add(Paragraph(" "))
        }
        document.add(Paragraph(" "))
    }
    document.close()
}

fun exportarDatosTXT(actores: List<Actor>, filePath: String) {
    val file = File(filePath)
    file.bufferedWriter().use { writer ->
        writer.write("Lista de Actores y sus Películas")
        writer.newLine()
        writer.newLine()

        actores.forEach { actor ->
            writer.write("Nombre: ${actor.nombre}")
            writer.newLine()
            writer.write("Fecha de Nacimiento: ${actor.fechaNacimiento}")
            writer.newLine()
            writer.write("Edad: ${actor.edad} años")
            writer.newLine()
            writer.write("Altura: ${actor.altura} m")
            writer.newLine()
            writer.write("Tiene Doble Acción: ${if (actor.tieneDobleAccion) "Sí" else "No"}")
            writer.newLine()
            writer.write("Películas:")
            writer.newLine()

            actor.peliculas.forEach { pelicula ->
                writer.write("  - Título: ${pelicula.nombrePelicula}")
                writer.newLine()
                writer.write("    Estreno: ${pelicula.fechaEstreno}")
                writer.newLine()
                writer.write("    Duración: ${pelicula.duracion} min")
                writer.newLine()
                writer.write("    Calificación: ${pelicula.calificacion}")
                writer.newLine()
                writer.write("    Basado en una Historia Real: ${if (pelicula.basadoHistoriaReal) "Sí" else "No"}")
                writer.newLine()
            }

            writer.newLine() // Espacio entre actores
        }
    }
}
