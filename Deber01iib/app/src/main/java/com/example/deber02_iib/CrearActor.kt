package com.example.deber02_iib


import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat

class CrearActor : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_actor)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_crear_actor)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        AlertDialog.Builder(this).setMessage("entrada a la pantalla crearActor").show()
        //val actor: Actor? = intent.getParcelableExtra("actor")
        val actor = intent.getParcelableExtra<Actor>("actor")
        val boton = findViewById<Button>(R.id.btn_crear)
        if (actor != null) {
            val nombreEditText = findViewById<EditText>(R.id.input_nombre)
            val fechaNacimientoEditText=findViewById<EditText>(R.id.inputFechaNac)
            val edadEditText = findViewById<EditText>(R.id.input_edad)
            val alturaEditText = findViewById<EditText>(R.id.input_altura)
            val dobleAccionCheckBox = findViewById<Switch>(R.id.input_dobleAccion)
            boton.setText("Actualizar")
            nombreEditText.setText(actor.nombre)
            fechaNacimientoEditText.setText(SimpleDateFormat("dd/MM/yyyy").format(actor.fechaNacimiento))
            edadEditText.setText(actor.edad.toString())
            alturaEditText.setText(actor.altura.toString())
            dobleAccionCheckBox.isChecked = actor.tieneDobleAccion
            boton.setOnClickListener {
                actualizarActor(actor)
            }
        } else {
            boton.setOnClickListener {
                crearActor()
            }
        }
    }
    private fun actualizarActor(actor: Actor) {
        val nombre = findViewById<EditText>(R.id.input_nombre).text.toString()
        val fechaNacimiento = SimpleDateFormat("dd/MM/yyyy").parse(findViewById<EditText>(R.id.inputFechaNac).text.toString())
        val edad = findViewById<EditText>(R.id.input_edad).text.toString().toInt()
        val altura = findViewById<EditText>(R.id.input_altura).text.toString().toDouble()
        val dobleAccion = findViewById<Switch>(R.id.input_dobleAccion).isChecked
        val index = BaseDatosMemoria.arregloActor.indexOfFirst { it.nombre == actor.nombre }
        // Actualiza el actor con los nuevos datos
        actor.nombre = nombre
        actor.fechaNacimiento = fechaNacimiento
        actor.edad = edad
        actor.altura = altura
        actor.tieneDobleAccion = dobleAccion
        // Reemplaza el actor en la base de datos en memoria
        if (index != -1) {
            BaseDatosMemoria.arregloActor[index] = actor
        }
        setResult(RESULT_OK)
        finish()
    }
    private fun crearActor() {
        // Obtén los datos de los campos
        val nombre = findViewById<EditText>(R.id.input_nombre).text.toString()
        val fechaNacimiento = SimpleDateFormat("dd/MM/yyyy").parse(findViewById<EditText>(R.id.inputFechaNac).text.toString())
        val edad = findViewById<EditText>(R.id.input_edad).text.toString().toInt()
        val altura = findViewById<EditText>(R.id.input_altura).text.toString().toDouble()
        val dobleAccion = findViewById<Switch>(R.id.input_dobleAccion).isChecked
        val nuevoactor = Actor(nombre, fechaNacimiento, edad, altura, dobleAccion)
        BaseDatosMemoria.arregloActor.add(nuevoactor)
        // Devuelve un resultado indicando que se creó un nuevo actor
        setResult(RESULT_OK)
        finish()
    }
}
