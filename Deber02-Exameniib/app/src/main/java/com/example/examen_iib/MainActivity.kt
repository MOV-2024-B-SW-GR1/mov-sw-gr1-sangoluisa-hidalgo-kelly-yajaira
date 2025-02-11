package com.example.examen_iib

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.PopupMenu
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MainActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var actorAdapter: ActorAdapter
    private lateinit var peliculaAdapter: PeliculaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseHelper = DatabaseHelper(this)
        listView = findViewById(R.id.lv_list_view)
        val btnCrearArtista: Button = findViewById(R.id.btn_crearArtista)

        // Obtener la lista de actores de la base de datos
        val actorList = databaseHelper.getAllActors()

        // Inicializar el ActorAdapter con la lista de actores
        actorAdapter = ActorAdapter(this, actorList.toMutableList())
        listView.adapter = actorAdapter

        // Configurar el botón para crear un nuevo actor
        btnCrearArtista.setOnClickListener {
            val intent = Intent(this, CrearActor::class.java)
            startActivity(intent)
        }

        // Configurar el listener para el clic largo en un actor
        listView.setOnItemLongClickListener { _, view, position, _ ->
            showPopupMenu(view, position)
            true
        }

        // Configurar el listener para el clic en un actor
        listView.setOnItemClickListener { _, _, position, _ ->
            val actorSeleccionado = actorAdapter.getItem(position)

            // Crear un Intent para navegar a la actividad PeliculaList
            val intent = Intent(this, PeliculaList::class.java).apply {
                // Pasar datos del actor seleccionado a la actividad PeliculaList
                putExtra("actorId", actorSeleccionado?.idActor)
                putExtra("actorNombre", actorSeleccionado?.nombre)
            }
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()

        // Actualizar la lista de actores al regresar a la MainActivity
        val actoresActualizados = databaseHelper.getAllActors()
        actorAdapter.clear()
        actorAdapter.addAll(actoresActualizados)
        actorAdapter.notifyDataSetChanged()
    }

    private fun showPopupMenu(view: View, position: Int) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(R.menu.menu2)
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.mi_editar -> {
                    editarActor(position)
                    true
                }
                R.id.mi_eliminar -> {
                    eliminarActor(position)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun editarActor(position: Int) {
        val actor = actorAdapter.getItem(position)

        val intent = Intent(this, CrearActor::class.java).apply {
            putExtra("actorId", actor?.idActor)
            putExtra("actorNombre", actor?.nombre)
            putExtra("actorFechaNacimiento", actor?.fechaNacimiento?.time)
            putExtra("actorEdad", actor?.edad)
            putExtra("actorAltura", actor?.altura)
            putExtra("actorDobleAccion", actor?.tieneDobleAccion)
        }

        startActivity(intent)
    }

    private fun eliminarActor(position: Int) {
        val actor = actorAdapter.getItem(position)

        // Eliminar el actor de la base de datos
        actor?.let {
            databaseHelper.deleteActor(it.idActor)
        }

        // Remover el actor de la lista en el adaptador
        actorAdapter.remove(position)
        actorAdapter.notifyDataSetChanged()
    }



    private fun anadirMarcador(ubicacion: LatLng, titulo: String, googleMap: GoogleMap) {
        googleMap.addMarker(
            MarkerOptions()
                .position(ubicacion)
                .title(titulo)
        )
    }

    private fun moverCamaraConZoom(ubicacion: LatLng, zoom: Float, googleMap: GoogleMap) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, zoom))
    }

}
