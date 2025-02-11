package com.example.examen_iib

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.text.SimpleDateFormat

class ActorAdapter(
    private val context: Context,
    private var actorList: MutableList<Actor>
) : BaseAdapter() {

    override fun getCount(): Int {
        return actorList.size
    }


    override fun getItem(position: Int): Actor {
        return actorList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_actor, parent, false)

        val actor = getItem(position)

        val tvNombre = view.findViewById<TextView>(R.id.tv_nombre_actor)
        val tvFechaNacimiento = view.findViewById<TextView>(R.id.tv_fecha_nacimiento)
        val tvAltura = view.findViewById<TextView>(R.id.tv_altura)

        tvNombre.text = actor.nombre
        tvFechaNacimiento.text = SimpleDateFormat("dd/MM/yyyy").format(actor.fechaNacimiento)
        tvAltura.text = "IMDB: ${actor.altura}"

        return view
    }

    // Método para limpiar la lista de actores
    fun clear() {
        actorList.clear()
        notifyDataSetChanged()
    }

    // Método para añadir una lista de actores
    fun addAll(actores: List<Actor>) {
        actorList.addAll(actores)
        notifyDataSetChanged()
    }
    fun remove(position: Int) {
        actorList.removeAt(position)
        notifyDataSetChanged()
    }

}
