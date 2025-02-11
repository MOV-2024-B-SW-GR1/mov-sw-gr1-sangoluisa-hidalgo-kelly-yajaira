package com.example.examen_iib

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat

class CrearPelicula : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    private var actorId: Int = 0
    private var peliculaId: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_pelicula)

        // Inicializar DatabaseHelper
        databaseHelper = DatabaseHelper(this)

        actorId = intent.getIntExtra("actorId", -1)
        peliculaId = intent.getIntExtra("peliculaId", -1)

        val inputNombre = findViewById<EditText>(R.id.input_nombrePelicula)
        val inputFechaEst = findViewById<EditText>(R.id.input_fechaEstreno)
        val inputDuracion = findViewById<EditText>(R.id.input_duracion)
        val inputCalificacion = findViewById<EditText>(R.id.input_calificacion)
        val inputBasadaHistoriaReal = findViewById<Switch>(R.id.input_basadaHistoriaReal)
        val inputLatitud = findViewById<EditText>(R.id.input_latitud)
        val inputLongitud = findViewById<EditText>(R.id.input_longitud)

        val btnCrear = findViewById<Button>(R.id.btn_crear_pelicula)

        if (peliculaId != -1) {
            val pelicula = databaseHelper.getPeliculaById(peliculaId)
            inputNombre.setText(pelicula?.nombre)
            inputFechaEst.setText(SimpleDateFormat("dd/MM/yyyy").format(pelicula?.fechaEstreno))
            inputDuracion.setText(pelicula?.duracion.toString())
            inputCalificacion.setText(pelicula?.calificacion.toString())
            inputBasadaHistoriaReal.isChecked = pelicula?.basadoHistoriaReal == true
            btnCrear.text = "Actualizar Película"
        }

        btnCrear.setOnClickListener {
            val nombre = inputNombre.text.toString()
            val fechaEst = inputFechaEst.text.toString()
            val duracion = inputDuracion.text.toString().toIntOrNull()
            val calificacion = inputCalificacion.text.toString().toDoubleOrNull()
            val basadoHistoriaReal = inputBasadaHistoriaReal.isChecked
            val latitud = inputLatitud.text.toString().toDoubleOrNull()
            val longitud = inputLongitud.text.toString().toDoubleOrNull()

            if (nombre.isEmpty() || fechaEst.isEmpty() || duracion == null || calificacion == null) {
                Toast.makeText(this, "Por favor, completa todos los campos correctamente.", Toast.LENGTH_SHORT).show()
            } else {
                val dateFormat = SimpleDateFormat("dd/MM/yyyy")
                val fechaEstr = dateFormat.parse(fechaEst)

                val nuevaPelicula = Pelicula(
                    idPelicula = peliculaId, // Usar el ID existente si estamos actualizando
                    nombre = nombre,
                    fechaEstreno = fechaEstr!!,
                    duracion = duracion,
                    calificacion = calificacion,
                    basadoHistoriaReal = basadoHistoriaReal,
                    id_Actor = actorId,
                    latitud = latitud ?: -0.20873040876115068,  // Usa el valor ingresado
                    longitud = longitud ?: -78.48696779291207
                )

                val resultado: Long = if (peliculaId == -1) {
                    // Insertar nueva película
                    databaseHelper.addPelicula(nuevaPelicula)
                } else {
                    // Actualizar película existente
                    databaseHelper.updatePelicula(nuevaPelicula).toLong()
                }

                if (resultado > 0) {
                    val message = if (peliculaId == -1) {
                        "Película creada exitosamente."
                    } else {
                        "Película actualizada exitosamente."
                    }
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

                    finish() // Cerrar la actividad y volver a la anterior
                } else {
                    Toast.makeText(this, "Error al guardar la película.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
