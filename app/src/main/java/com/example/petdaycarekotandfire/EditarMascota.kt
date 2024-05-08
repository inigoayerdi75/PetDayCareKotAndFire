package com.example.petdaycarekotandfire

import android.annotation.SuppressLint
import android.content.DialogInterface
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

class EditarMascota : AppCompatActivity() {
    lateinit var editTextNombre : EditText
    lateinit var editTextEspecie: EditText
    lateinit var editTextRaza : EditText
    lateinit var editTextDuenno : EditText
    lateinit var editTextNumberPeso : EditText
    lateinit var spinnerSexo : Spinner
    var arraySexo = ArrayList<String>()
    val db = Firebase.firestore
    @SuppressLint("MissingInflatedId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_mascota)

        editTextNombre = findViewById(R.id.editTextNombreAlta)
        editTextEspecie = findViewById(R.id.editTextEspecieAlta)
        editTextRaza = findViewById(R.id.editTextRazaAlta)
        editTextDuenno = findViewById(R.id.editTextDuennoAlta)
        editTextNumberPeso = findViewById(R.id.editTextNumberPesoAlta)
        spinnerSexo = findViewById(R.id.spinnerSexoAlta)

        val intent = intent
        val mascotaRecibida = intent.getSerializableExtra("mascota") as Mascota
        val idMascota = intent.getStringExtra("id") as String
        editarMascota(mascotaRecibida)

        var buttonModificar = findViewById<Button>(R.id.buttonModificar)
        var buttonEliminar = findViewById<Button>(R.id.buttonEliminar)

        buttonModificar.setOnClickListener {
            if(camposCompletos()){
                if(hayCambios(mascotaRecibida)){
                    modificarMascota(idMascota)
                }else{
                    alert("No se puede modificar.\nNo se ha modificado ningún dato de la mascota")
                }
            }else{
                alert("No se puede modificar. Hay campos vacíos.")
            }
        }

        buttonEliminar.setOnClickListener {
            eliminarMascota(idMascota)
        }
    }

    fun editarMascota(mascotaRecibida : Mascota){
        if(mascotaRecibida.sexo == "Macho"){
            arraySexo.add("Macho")
            arraySexo.add("Hembra")
        }else{
            arraySexo.add("Hembra")
            arraySexo.add("Macho")
        }
        val adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,arraySexo)
        spinnerSexo.adapter = adapter
        spinnerSexo.textAlignment = View.TEXT_ALIGNMENT_CENTER
        editTextNombre.text.append(mascotaRecibida.nombre)
        editTextEspecie.text.append(mascotaRecibida.especie)
        editTextRaza.text.append(mascotaRecibida.raza)
        editTextDuenno.text.append(mascotaRecibida.duenno)
        editTextNumberPeso.text.append(mascotaRecibida.peso.toString())
    }

    fun alert (mensaje : String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar",null)
        val alerta = builder.create()
        alerta.show()
    }

    fun eliminarMascota (id : String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Eliminar")
        builder.setMessage("¿Estás seguro de eliminar la mascota?")
        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, which ->
            eliminar(id)
        })
        builder.setNegativeButton("Cancelar",null)
        val alerta = builder.create()
        alerta.show()
    }

    fun modificarMascota (id : String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Modificar")
        builder.setMessage("¿Estás seguro de modificar los datos de la mascota?")
        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, which ->
            modificar(id)
        })
        builder.setNegativeButton("Cancelar",null)
        val alerta = builder.create()
        alerta.show()
    }

    fun eliminar(id : String){
        db.collection("Mascotas").document(id)
            .delete().addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(applicationContext,"Se ha eliminado la mascota correctamente", Toast.LENGTH_SHORT).show()
                    var i = Intent(applicationContext,ListadoDeMascotas::class.java)
                    startActivity(i)
                }else{
                    alert("No ha sido posible eliminar la mascota.")
                }
            }
    }

    fun modificar(id : String){
        db.collection("Mascotas").document(id)
            .update("nombre",editTextNombre.text.toString(),"especie", editTextEspecie.text.toString())
            .addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(applicationContext,"Se ha modificado correctamente", Toast.LENGTH_SHORT).show()
                    var i = Intent(applicationContext,ListadoDeMascotas::class.java)
                    startActivity(i)
                }else{
                    alert("No ha sido posible modificar la mascota.")
                }
            }
    }

    fun editTextCompletado(e : EditText) : Boolean{
        return !(e.text.toString().trim().isNullOrEmpty() || e.text.toString().trim().isBlank())
    }

    fun camposCompletos() : Boolean{
        return (editTextCompletado(editTextNombre) && editTextCompletado(editTextEspecie)
                && editTextCompletado(editTextRaza) && editTextCompletado(editTextNumberPeso)
                && editTextCompletado(editTextDuenno))
    }

    fun hayCambios(mascota : Mascota) : Boolean{
        return (spinnerSexo.selectedItemPosition !=-1 && spinnerSexo.selectedItemPosition != 0)
            || mascota.nombre != editTextNombre.text.toString().trim()
            || mascota.especie != editTextEspecie.text.toString().trim()
            || mascota.duenno != editTextDuenno.text.toString().trim()
            || mascota.peso != editTextNumberPeso.text.toString().trim().toDouble()
            || mascota.raza != editTextRaza.text.toString().trim()

    }
}