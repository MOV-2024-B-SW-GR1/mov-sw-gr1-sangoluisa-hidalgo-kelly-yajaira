package com.example.examen_iib

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PeliculaList : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var peliculaAdapter: PeliculaAdapter
    private var actorId: Int = 0
    private var selectedPeliculaId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pelicula_list)

        databaseHelper = DatabaseHelper(this)
        listView = findViewById(R.id.lv_listt_view_peliculas)

        val btnCrearPelicula: Button = findViewById(R.id.btn_crearPelicula)
        val actorNombre = intent.getStringExtra("actorNombre")
        findViewById<TextView>(R.id.txt_actor).text = actorNombre
        actorId = intent.getIntExtra("actorId", -1)

        if (actorId != -1) {
            val peliculaList = databaseHelper.getPeliculasByActor(actorId)
            peliculaAdapter = PeliculaAdapter(this, peliculaList.toMutableList())
            listView.adapter = peliculaAdapter

            // Registrar ListView para el menú contextual
            registerForContextMenu(listView)
        }

        btnCrearPelicula.setOnClickListener {
            val intent = Intent(this, CrearPelicula::class.java).apply {
                putExtra("actorId", actorId)
            }
            startActivity(intent)
        }
        val btnIrUbi: Button = findViewById(R.id.btn_irGoogleMaps)
        btnIrUbi.setOnClickListener {
            val intent = Intent(this, GoogleMapsActivity::class.java)
            startActivity(intent)
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val peliculaSeleccionada = peliculaAdapter.getItem(position)
            val intent = Intent(this, CrearPelicula::class.java).apply {
                putExtra("actorId", actorId)
                putExtra("peliculaId", peliculaSeleccionada?.idPelicula) // Pasar el ID de la película
            }
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        // Actualizar la lista de películas al regresar a la PeliculaList
        val peliculasActualizadas = databaseHelper.getPeliculasByActor(actorId)
        peliculaAdapter.clear()
        peliculaAdapter.addAll(peliculasActualizadas)
    }

    // Crear el menú contextual para la lista de películas
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu2, menu)

        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        selectedPeliculaId = peliculaAdapter.getItem(info.position).idPelicula
    }

    // Manejar la selección de elementos en el menú contextual
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_editar -> {
                editarPelicula(selectedPeliculaId)
                true
            }
            R.id.mi_eliminar -> {
                databaseHelper.deletePelicula(selectedPeliculaId)
                onResume()
                true
            }R.id.menu_abrir_mapa -> {
                val intent = Intent(this, GoogleMapsActivity::class.java)
                startActivity(intent)
                true
            }

            else -> super.onContextItemSelected(item)
        }
    }

    private fun editarPelicula(peliculaId: Int) {
        val pelicula = databaseHelper.getPeliculaById(peliculaId)

        val intent = Intent(this, CrearPelicula::class.java).apply {
            putExtra("peliculaId", pelicula?.idPelicula)
            putExtra("actorId", pelicula?.id_Actor)
            putExtra("peliculaNombre", pelicula?.nombre)
            putExtra("peliculaFechaEstreno", pelicula?.fechaEstreno?.time)
            putExtra("peliculaCantidadActores", pelicula?.duracion)
            putExtra("peliculaDuracion", pelicula?.calificacion)
            putExtra("peliculaEnCartelera", pelicula?.basadoHistoriaReal)
        }

        startActivity(intent)
    }
}
