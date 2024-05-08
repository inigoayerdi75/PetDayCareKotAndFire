package com.example.petdaycarekotandfire

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class ListadoDeMascotas : AppCompatActivity() {
    lateinit var listViewMascotas : ListView
    lateinit var textViewListaVacia : TextView
    var listMascotas = ArrayList<Mascota>()
    var clavesID = ArrayList<String>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado_de_mascotas)

        var imageButtonAddPet : ImageButton = findViewById(R.id.imageButtonAddMascota)
        listViewMascotas = findViewById(R.id.listViewMascotas)
        textViewListaVacia = findViewById(R.id.textViewListaVacia)

        listadoMascotas()

        listViewMascotas.setOnItemClickListener { parent, view, position, id ->
            var mascota = listMascotas.get(position)
            var i = Intent(applicationContext,EditarMascota::class.java).putExtra("mascota",mascota)
                .putExtra("id", clavesID[position])
            startActivity(i)
        }

        imageButtonAddPet.setOnClickListener {
            var i = Intent(applicationContext,AltaMascota::class.java)
            startActivity(i)
        }
    }

    fun listadoMascotas(){
        val db = Firebase.firestore
        db.collection("Mascotas")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    var nombre = document.data["nombre"].toString()
                    var especie = document.data["especie"].toString()
                    var raza = document.data["raza"].toString()
                    var peso = document.data["peso"].toString().toDouble()
                    var sexo = document.data["sexo"].toString()
                    var duenno = document.data["duenno"].toString()
                    var mascota = Mascota(nombre,especie,raza,peso,sexo,duenno)
                    listMascotas.add(mascota)
                    clavesID.add(document.id)

                    //Log.d(TAG, "${document.id} => ${document.data}")
                }
                val adaptador = MascotaArrayAdapter(applicationContext,R.layout.mascota,listMascotas)
                listViewMascotas.adapter = adaptador
                listViewMascotas.emptyView = textViewListaVacia
            }
            .addOnFailureListener { exception ->
                //Log.d(TAG, "Error getting documents: ", exception)
            }
    }
}