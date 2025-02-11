package com.example.proyecto

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ReservationAdapter(context: Context, private val reservations: List<Reservation>) :
    ArrayAdapter<Reservation>(context, 0, reservations) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val reservation = reservations[position]
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false)

        view.findViewById<TextView>(android.R.id.text1).text = "Fecha: ${reservation.date}"
        view.findViewById<TextView>(android.R.id.text2).text = "Hora: ${reservation.time}"

        return view
    }
}