package com.example.examen_iib

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.snackbar.Snackbar

class GoogleMapsActivity : AppCompatActivity() {
    private lateinit var mapa: GoogleMap
    var permisos = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_google_maps_activiry)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_googleMaps)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        solicitarPermisos()
        iniciarLogicaMapa()

    }
    fun solicitarPermisos(){
        val contexto = this.applicationContext
        val nombrePermisoFine = android.Manifest.permission.ACCESS_FINE_LOCATION
        val nombrePermisoCoarse = android.Manifest.permission.ACCESS_COARSE_LOCATION
        val permisoFine = ContextCompat.checkSelfPermission(contexto, nombrePermisoFine)
        val permisoCoarse = ContextCompat.checkSelfPermission(contexto, nombrePermisoCoarse)
        val tienePermisos = permisoFine == PackageManager.PERMISSION_GRANTED &&
                permisoCoarse == PackageManager.PERMISSION_GRANTED
        if(tienePermisos){
            permisos = true
        }else{
            ActivityCompat.requestPermissions(
                this, arrayOf(nombrePermisoFine,nombrePermisoCoarse), 1
            )
        }
    }
    fun iniciarLogicaMapa(){
        val fragmentoMapa = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        fragmentoMapa.getMapAsync{ googleMap ->
            with(googleMap){
                mapa = googleMap
                establecerConfiguracionMapa()
                val latitud = intent.getDoubleExtra("latitud", -0.20873040876115068)
                val longitud = intent.getDoubleExtra("longitud", -78.48696779291207)
                moverCamaraConUbicacion(latitud, longitud)
                escucharListeners()
            }
        }
    }
    fun moverCamaraConUbicacion(latitud: Double, longitud: Double) {
        val zoom = 17f
        val ubicacion = LatLng(latitud, longitud)
        val titulo = "Ubicación recibida"
        val marcador = anadirMarcador(ubicacion, titulo)
        marcador.tag = titulo
        moverCamaraConZoom(ubicacion, zoom)
    }
    fun establecerConfiguracionMapa(){
        val contexto = this.applicationContext
        with(mapa){
            val nombrePermisoFine = android.Manifest.permission.ACCESS_FINE_LOCATION
            val nombrePermisoCoarse = android.Manifest.permission.ACCESS_COARSE_LOCATION
            val permisoFine = ContextCompat.checkSelfPermission(contexto, nombrePermisoFine)
            val permisoCoarse = ContextCompat.checkSelfPermission(contexto, nombrePermisoCoarse)
            val tienePermisos = permisoFine == PackageManager.PERMISSION_GRANTED &&
                    permisoCoarse == PackageManager.PERMISSION_GRANTED
            if(tienePermisos){
                mapa.isMyLocationEnabled = true
                uiSettings.isMyLocationButtonEnabled = true
            }
            uiSettings.isZoomControlsEnabled = true
        }
    }
    fun moverCamaraConZoom(latLang: LatLng, zoom: Float = 10f){
        mapa.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                latLang, zoom
            ))
    }
    fun anadirMarcador(latLang: LatLng, title:String): Marker {
        return mapa.addMarker(
            MarkerOptions().position(latLang)
                .title(title)
        )!!
    }
    fun mostrarSnackbar(texto:String){
        val snack = Snackbar.make(
            findViewById(R.id.layout_googleMaps),
            texto,
            Snackbar.LENGTH_INDEFINITE
        )
        snack.show()
    } // cl_google_maps

    fun  escucharListeners(){
        mapa.setOnPolygonClickListener {
            mostrarSnackbar("setOnPolygonClickListener $it.tag")
        }
        mapa.setOnPolylineClickListener {
            mostrarSnackbar("setOnPolylineClickListener $it.tag")
        }
        mapa.setOnMarkerClickListener {
            mostrarSnackbar("setOnMarkerClickListener $it.tag")
            return@setOnMarkerClickListener true
        }
        mapa.setOnCameraMoveListener {
            mostrarSnackbar("setOnCameraMoveListener")
        }
        mapa.setOnCameraMoveStartedListener {
            mostrarSnackbar("setOnCameraMoveStartedListener")
        }
        mapa.setOnCameraIdleListener {
            mostrarSnackbar("setOnCameraIdleListener")
        }
    }
}