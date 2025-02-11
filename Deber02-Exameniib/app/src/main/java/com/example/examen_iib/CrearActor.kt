package com.example.examen_iib
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat

class CrearActor : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private var actorId: Int = -1 // Variable para almacenar el ID del actor si estamos en modo de edición

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_actor)

        // Inicializar DatabaseHelper
        databaseHelper = DatabaseHelper(this)

        // Referencias a las vistas
        val inputNombre = findViewById<EditText>(R.id.input_nombre)
        val inputFechaNac = findViewById<EditText>(R.id.inputFechaNac)
        val inputEdad = findViewById<EditText>(R.id.input_edad)
        val inputAltura = findViewById<EditText>(R.id.input_altura)
        val inputTieneDobleAccion = findViewById<Switch>(R.id.input_dobleaccion)
        val btnCrear = findViewById<Button>(R.id.btn_crear)


        // Verificar si la actividad se inició en modo de edición
        if (intent.hasExtra("actorId")) {
            actorId = intent.getIntExtra("actorId", -1)
            val actor = databaseHelper.getActor(actorId)

            if (actor != null) {
                inputNombre.setText(actor.nombre)
                inputFechaNac.setText(SimpleDateFormat("dd/MM/yyyy").format(actor.fechaNacimiento))
                inputEdad.setText(actor.edad.toString())
                inputAltura.setText(actor.altura.toString())
                inputTieneDobleAccion.isChecked = actor.tieneDobleAccion

                btnCrear.text = "Actualizar" // Cambiar el texto del botón
            }
        }

        // Listener para el botón de crear o actualizar actor
        btnCrear.setOnClickListener {
            val nombre = inputNombre.text.toString()
            val fechaNacStr = inputFechaNac.text.toString()
            val edad = inputEdad.text.toString().toIntOrNull()
            val altura = inputAltura.text.toString().toDoubleOrNull()
            val dobleAccion = inputTieneDobleAccion.isChecked

            if (nombre.isEmpty() || fechaNacStr.isEmpty() || edad == null || altura == null) {
                Toast.makeText(this, "Por favor, completa todos los campos correctamente.", Toast.LENGTH_SHORT).show()
            } else {
                val dateFormat = SimpleDateFormat("dd/MM/yyyy")
                val fechaNac = dateFormat.parse(fechaNacStr)

                if (actorId == -1) {
                    // Crear un nuevo actor
                    val nuevoActor = Actor(
                        idActor = 0, // Se generará automáticamente por la base de datos
                        nombre = nombre,
                        fechaNacimiento = fechaNac!!,
                        edad = edad,
                        altura = altura,
                        tieneDobleAccion = dobleAccion
                    )

                    // Insertar el nuevo actor en la base de datos
                    val resultado = databaseHelper.addActor(nuevoActor)

                    if (resultado > 0) {
                        Toast.makeText(this, "Actor creado exitosamente.", Toast.LENGTH_SHORT).show()
                        finish() // Cerrar la actividad y volver a la anterior
                    } else {
                        Toast.makeText(this, "Error al crear el actor.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Actualizar un actor existente
                    val actorActualizado = Actor(
                        idActor = actorId,
                        nombre = nombre,
                        fechaNacimiento = fechaNac!!,
                        edad = edad,
                        altura = altura,
                        tieneDobleAccion = dobleAccion
                    )

                    val resultado = databaseHelper.updateActor(actorActualizado)

                    if (resultado > 0) {
                        Toast.makeText(this, "Actor actualizado exitosamente.", Toast.LENGTH_SHORT).show()
                        finish() // Cerrar la actividad y volver a la anterior
                    } else {
                        Toast.makeText(this, "Error al actualizar el actor.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
