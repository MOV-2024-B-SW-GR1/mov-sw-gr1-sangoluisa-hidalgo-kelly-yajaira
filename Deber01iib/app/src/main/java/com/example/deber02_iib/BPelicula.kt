package com.example.deber02_iib

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class BPelicula : AppCompatActivity() {
    val arreglo = BaseDatosMemoria.arregloPeliculas
    private lateinit var adaptador: ArrayAdapter<Pelicula>
    private var posicionItemSeleccionado = -1 // Declara la variable aquí
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pelicula)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_peliculas)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val listView = findViewById<ListView>(R.id.lv_list_view_peliculas)
        adaptador = ArrayAdapter(
            this, // contexto
            android.R.layout.simple_list_item_1, // layout xml a usar
            arreglo
        )
        listView.adapter = adaptador
        val botonAnadirListView = findViewById<Button>(R.id.btn_crearPelicula)
        botonAnadirListView.setOnClickListener {
            irActividad(CrearPelicula::class.java, MainActivity.REQUEST_CODE_ADD_OR_EDIT)
        }
        registerForContextMenu(listView) // NUEVA LINEA
        val actor = intent.getParcelableExtra<Actor>("actor")
        val textoActor = findViewById<TextView>(R.id.txt_actor)
        textoActor.text = actor?.nombre ?: "Nombre no disponible"
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MainActivity.REQUEST_CODE_ADD_OR_EDIT && resultCode == RESULT_OK) {
            adaptador.notifyDataSetChanged() // ACTUALIZA UI
        }
    }
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ){
        super.onCreateContextMenu(menu,v,menuInfo)
        // llenamos opciones del menu
        val inflater = menuInflater
        inflater.inflate(R.menu.menu2, menu)
        // Obtener id
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val posicion = info.position
        posicionItemSeleccionado = posicion
    }
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_editar -> {
                val peliculaSeleccionada = arreglo[posicionItemSeleccionado]
                val intent = Intent(this, CrearPelicula::class.java)
                intent.putExtra("pelicula", peliculaSeleccionada)
                startActivityForResult(intent, MainActivity.REQUEST_CODE_ADD_OR_EDIT)
                adaptador.notifyDataSetChanged()
                true
            }
            R.id.mi_eliminar -> {
                val peliculaSeleccionada = arreglo[posicionItemSeleccionado]
                BaseDatosMemoria.arregloPeliculas.remove(peliculaSeleccionada)
                adaptador.notifyDataSetChanged()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }
    private fun irActividad(clase: Class<*>, requestCode: Int) {
        val intent = Intent(this, clase)
        startActivityForResult(intent, requestCode)
    }
}

