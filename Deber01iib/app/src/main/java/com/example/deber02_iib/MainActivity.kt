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
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    val arreglo = BaseDatosMemoria.arregloActor
    private lateinit var adaptador: ArrayAdapter<Actor>
    private var posicionItemSeleccionado = -1 // Declara la variable aquí
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val listView = findViewById<ListView>(R.id.lv_list_view)
        adaptador = ArrayAdapter(
            this, // contexto
            android.R.layout.simple_list_item_1, // layout xml a usar
            arreglo
        )
        listView.adapter = adaptador
        val botonAnadirListView = findViewById<Button>(R.id.btn_crearActor)
        botonAnadirListView.setOnClickListener {
            irActividad(CrearActor::class.java, REQUEST_CODE_ADD_OR_EDIT)
        }
        registerForContextMenu(listView) // NUEVA LINEA
    }
    private fun irActividad(clase: Class<*>, requestCode: Int) {
        val intent = Intent(this, clase)
        startActivityForResult(intent, requestCode)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_OR_EDIT && resultCode == RESULT_OK) {
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
        inflater.inflate(R.menu.menu, menu)
        // Obtener id
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val posicion = info.position
        posicionItemSeleccionado = posicion
    }
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_editar -> {
                // Obtener el actor seleccionado
                val actorSeleccionado = arreglo[posicionItemSeleccionado]
                // Crear el Intent y pasar el actor como extra
                val intent = Intent(this, CrearActor::class.java)
                intent.putExtra("actor", actorSeleccionado)
                startActivityForResult(intent, REQUEST_CODE_ADD_OR_EDIT)
                adaptador.notifyDataSetChanged()
                true
            }
            R.id.mi_eliminar -> {
                val actorSeleccionado = arreglo[posicionItemSeleccionado]
                BaseDatosMemoria.arregloActor.remove(actorSeleccionado)
                adaptador.notifyDataSetChanged()
                true
            }
            R.id.mi_verPeliculas -> {
                val actorSeleccionado = arreglo[posicionItemSeleccionado]
                // Crear el Intent y pasar el actor como extra
                val intent = Intent(this, Pelicula::class.java)
                intent.putExtra("actor", actorSeleccionado)
                startActivityForResult(intent, REQUEST_CODE_ADD_OR_EDIT)
                adaptador.notifyDataSetChanged()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }
    companion object {
        const val REQUEST_CODE_ADD_OR_EDIT = 1
    }
}
