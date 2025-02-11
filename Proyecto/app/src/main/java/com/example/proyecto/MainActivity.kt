package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var lvReservations: ListView
    private lateinit var btnAddReservation: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)
        lvReservations = findViewById(R.id.lvReservations)
        btnAddReservation = findViewById(R.id.btnAddReservation)

        // Aquí deberías obtener el ID del usuario logueado (por simplicidad, asumimos que es 1)
        val userId = 1
        val reservations = dbHelper.getReservations(userId)
        val adapter = ReservationAdapter(this, reservations)
        lvReservations.adapter = adapter

        btnAddReservation.setOnClickListener {
            startActivity(Intent(this, ReservationActivity::class.java))
        }
    }
}