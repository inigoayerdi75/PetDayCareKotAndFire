package com.example.petdaycarekotandfire

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class AltaMascota : AppCompatActivity() {
    lateinit var editTextNombreAlta : EditText
    lateinit var editTextEspecieAlta : EditText
    lateinit var editTextRazaAlta : EditText
    lateinit var editTextNumberPesoAlta : EditText
    lateinit var editTextDuennoAlta : EditText
    lateinit var spinnerSexoAlta : Spinner
    val db = Firebase.firestore

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alta_mascota)
        editTextNombreAlta = findViewById(R.id.editTextNombreAlta)
        editTextEspecieAlta = findViewById(R.id.editTextEspecieAlta)
        editTextRazaAlta = findViewById(R.id.editTextRazaAlta)
        editTextNumberPesoAlta = findViewById(R.id.editTextNumberPesoAlta)
        editTextDuennoAlta = findViewById(R.id.editTextDuennoAlta)
        spinnerSexoAlta = findViewById(R.id.spinnerSexoAlta)
        var buttonAnnadir = findViewById<Button>(R.id.buttonAnnadir)


        var arraySexo = ArrayList<String>()
        arraySexo.add("- Seleccionar -")
        arraySexo.add("Macho")
        arraySexo.add("Hembra")
        val adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,arraySexo)
        spinnerSexoAlta.adapter = adapter
        spinnerSexoAlta.textAlignment = View.TEXT_ALIGNMENT_CENTER
        buttonAnnadir.setOnClickListener {
            if(camposCompletos()){
                annadirMascota()
            }else{
                alert("Hay campos sin completar")
            }
        }
    }

    fun annadirMascota(){
        val mascota = crearMascota()
        db.collection("Mascotas")
            .add(mascota)
            .addOnSuccessListener { documentReference ->
                var toast = Toast.makeText(applicationContext, "Se ha añadido la mascota correctamente",Toast.LENGTH_SHORT)
                toast.show()
                var i = Intent(applicationContext,ListadoDeMascotas::class.java)
                startActivity(i)
            }
            .addOnFailureListener { e ->
                alert("Error al añadir el documento:\n $e")
            }
    }

    fun camposCompletos() : Boolean{
       return editTextCompletado(editTextNombreAlta) && editTextCompletado(editTextEspecieAlta)
           && editTextCompletado(editTextRazaAlta) && editTextCompletado(editTextNumberPesoAlta)
           && editTextCompletado(editTextDuennoAlta)  && spinnerSeleccionado(spinnerSexoAlta)
    }

    fun editTextCompletado(e : EditText) : Boolean{
        return !(e.text.toString().trim().isNullOrEmpty() || e.text.toString().trim().isBlank())
    }

    fun spinnerSeleccionado(s : Spinner) : Boolean{
        return s.selectedItemPosition != -1 && s.selectedItemPosition != 0
    }

    fun crearMascota() : Mascota{
        val sexoAlta = spinnerSexoAlta.selectedItem.toString()
        val nombre = editTextNombreAlta.text.toString().trim()
        val especie = editTextEspecieAlta.text.toString().trim()
        val raza = editTextRazaAlta.text.toString().trim()
        val peso = editTextNumberPesoAlta.text.toString().trim().toDouble()
        val duenno = editTextDuennoAlta.text.toString().trim()
        val mascota = Mascota(nombre,especie,raza,peso,sexoAlta,duenno)
        return mascota
    }

    fun alert (mensaje : String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar",null)
        val alerta = builder.create()
        alerta.show()
    }
}