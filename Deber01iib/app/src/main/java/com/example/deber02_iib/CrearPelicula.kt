package com.example.deber02_iib

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat

class CrearPelicula : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_pelicula)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val pelicula = intent.getParcelableExtra<Pelicula>("pelicula")
        val boton = findViewById<Button>(R.id.btn_crear_pelicula)
        if (pelicula != null) {
            val nombreEditText = findViewById<EditText>(R.id.input_nombrePelicula)
            val fechaEstreno = findViewById<EditText>(R.id.input_fechaEstreno)
            val duracion = findViewById<EditText>(R.id.input_duracion)
            val calificacion = findViewById<EditText>(R.id.input_calificacion)
            val basadaHistoriaReal = findViewById<Switch>(R.id.input_basadoHistoriaReal)
            boton.setText("Actualizar")
            nombreEditText.setText(pelicula.nombre)
            fechaEstreno.setText(SimpleDateFormat("dd/MM/yyyy").format(pelicula.fechaEstreno))
            duracion.setText(pelicula.duracion.toString())
            calificacion.setText(pelicula.calificacion.toString())
            basadaHistoriaReal.isChecked = pelicula.basadoHistoriaReal
            boton.setOnClickListener {
                actualizarPelicula(pelicula)
            }
        } else {
            boton.setOnClickListener {
                crearPelicula()
            }
        }
    }
    fun actualizarPelicula(pelicula: Pelicula){
        val nombre = findViewById<EditText>(R.id.input_nombrePelicula).text.toString()
        val fechaEstreno = SimpleDateFormat("dd/MM/yyyy").parse(findViewById<EditText>(R.id.input_fechaEstreno).text.toString())
        val duracion = findViewById<EditText>(R.id.input_duracion).text.toString().toInt()
        val calificacion = findViewById<EditText>(R.id.input_calificacion).text.toString().toDouble()
        val basadaHistoriaReal = findViewById<Switch>(R.id.input_basadoHistoriaReal).isChecked
        val index = BaseDatosMemoria.arregloPeliculas.indexOfFirst { it.nombre == pelicula.nombre }
        // Actualiza el actor con los nuevos datos
        pelicula.nombre = nombre
        pelicula.fechaEstreno = fechaEstreno
        pelicula.duracion = duracion
        pelicula.calificacion = calificacion
        pelicula.basadoHistoriaReal = basadaHistoriaReal
        // Reemplaza el actor en la base de datos en memoria
        if (index != -1) {
            BaseDatosMemoria.arregloPeliculas[index] = pelicula
        }
        setResult(RESULT_OK)
        finish()
    }
    fun crearPelicula(){
        // Obtén los datos de los campos
        val nombre = findViewById<EditText>(R.id.input_nombrePelicula).text.toString()
        val fechaEstreno = SimpleDateFormat("dd/MM/yyyy").parse(findViewById<EditText>(R.id.input_fechaEstreno).text.toString())
        val duracion = findViewById<EditText>(R.id.input_duracion).text.toString().toInt()
        val calificacion = findViewById<EditText>(R.id.input_calificacion).text.toString().toDouble()
        val basadaHistoriaReal = findViewById<Switch>(R.id.input_basadoHistoriaReal).isChecked
        val nuevaPelicula = Pelicula(nombre, fechaEstreno, duracion, calificacion, basadaHistoriaReal)
        BaseDatosMemoria.arregloPeliculas.add(nuevaPelicula)
        // Devuelve un resultado indicando que se creó un nuevo actor
        setResult(RESULT_OK)
        finish()
    }
}

