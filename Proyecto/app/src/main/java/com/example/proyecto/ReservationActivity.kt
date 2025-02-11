package com.example.proyecto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import java.text.SimpleDateFormat

class ReservationActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var etDate: EditText
    private lateinit var etTime: EditText
    private lateinit var btnSaveReservation: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation)

        dbHelper = DatabaseHelper(this)
        etDate = findViewById(R.id.etDate)
        etTime = findViewById(R.id.etTime)
        btnSaveReservation = findViewById(R.id.btnSaveReservation)

        btnSaveReservation.setOnClickListener {
            val date = etDate.text.toString()
            val time = etTime.text.toString()

            if (date.isNotEmpty() && time.isNotEmpty()) {
                // Aquí deberías obtener el ID del usuario logueado (por simplicidad, asumimos que es 1)
                val userId = 1
                dbHelper.addReservation(userId, date, time)
                Toast.makeText(this, "Reserva agendada", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}