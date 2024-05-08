package com.example.petdaycarekotandfire

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class MascotaArrayAdapter(context: Context, vista: Int, val listaMascotas : ArrayList<Mascota>) :
    ArrayAdapter<Mascota>(context , vista, listaMascotas ){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var inflater = LayoutInflater.from(context)
        var currentMascota = inflater.inflate(R.layout.mascota, null)

        var nombre = currentMascota.findViewById<TextView>(R.id.textViewNombre)
        var raza = currentMascota.findViewById<TextView>(R.id.textViewRaza)
        var duenno = currentMascota.findViewById<TextView>(R.id.textViewDueño)
        var especie = currentMascota.findViewById<ImageView>(R.id.imageViewMascota)

        nombre.text = listaMascotas.get(position).nombre
        raza.text = listaMascotas.get(position).raza
        duenno.text = listaMascotas.get(position).duenno

        when (listaMascotas.get(position).especie.toLowerCase()){
            "perro"-> especie.setImageResource(R.drawable.perro)
            "gato" -> especie.setImageResource(R.drawable.gato)
            "reptil" -> especie.setImageResource(R.drawable.anaconda)
        }

        return currentMascota
    }
}