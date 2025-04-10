package com.example.materiaalumno

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: RegistroDatabaseHelper
    private lateinit var  adapter: ArrayAdapter<String>
    private lateinit var spinnerCarreras: Spinner
    private lateinit var editTextNombre: EditText
    private lateinit var listViewAlumnos: ListView
    private lateinit var carreras: List<Carrera>

      override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)
          setContentView(R.layout.activity_main)

           dbHelper = RegistroDatabaseHelper(this)
          spinnerCarreras= findViewById(R.id.spinnerCarreras)
          editTextNombre=findViewById(R.id.editTextNombre)
          listViewAlumnos=findViewById(R.id.listViewAlumnos)

       // llenarTablaCarreras()
         configurarSpinnerCarreras()
          actualizarListAlumnos()

//          val btnAgregarAlumno:Button=findViewById(R.id.btnAgregarAlumno)
//          btnAgregarAlumno.setOnClickListener {
//              val nombre=editTextNombre.text.toString()
//              val idCarrera=spinnerCarreras.selectedItemId.toInt()
//
//              dbHelper.addAlumno(nombre,idCarrera)
//              Toast.makeText(this, "Alumno Agregado",Toast.LENGTH_SHORT).show()
//              actualizarListAlumnos()
//          }

          val btnAgregarAlumno: Button = findViewById(R.id.btnAgregarAlumno)
          btnAgregarAlumno.setOnClickListener {
              val nombre = editTextNombre.text.toString().trim()
              if (nombre.isEmpty()) {
                  editTextNombre.error = "El nombre es requerido"
                  return@setOnClickListener
              }

              val posicionSeleccionada = spinnerCarreras.selectedItemPosition
              if (posicionSeleccionada != Spinner.INVALID_POSITION && posicionSeleccionada < carreras.size) {
                  val idCarrera = carreras[posicionSeleccionada].id

                  try {
                      dbHelper.addAlumno(nombre, idCarrera)
                      Toast.makeText(this, "Alumno Agregado", Toast.LENGTH_SHORT).show()
                      editTextNombre.text.clear()
                      actualizarListAlumnos()
                  } catch (e: Exception) {
                      Log.e("MainActivity", "Error al agregar alumno", e)
                      Toast.makeText(this, "Error al agregar alumno", Toast.LENGTH_SHORT).show()
                  }
              } else {
                  Toast.makeText(this, "Por favor, selecciona una carrera válida", Toast.LENGTH_SHORT).show()
              }
          }
       }


    private fun llenarTablaCarreras(){
        val nombresCarreras= listOf("Computacion","Arquitectura","Psicologia")
        for(nombreCarrera in nombresCarreras){
            dbHelper.addCarrera(nombreCarrera)
            Toast.makeText(this, "Carreras Agregadas", Toast.LENGTH_SHORT).show()
        }
    }


    private fun configurarSpinnerCarreras() {
        carreras = dbHelper.getAllCarreras()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, carreras.map { it.nombreC })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCarreras.adapter = adapter
    }

//    private fun configurarSpinnerCarreras(){
//        val nombreCarreras: List<String> = dbHelper.getAllNombresCarreras()
//        Toast.makeText(this, "funcion get all nombres carreras", Toast.LENGTH_SHORT).show()
//        val adapter= ArrayAdapter(this, android.R.layout.simple_spinner_item,nombreCarreras)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinnerCarreras.adapter=adapter
//    }

//    private fun actualizarListAlumnos(){
//        val alumnosList:ArrayList<Pair<String,String>> = dbHelper.getAllAlumnosCarrera()
//        Toast.makeText(this, "funcion actualizar lista", Toast.LENGTH_SHORT).show()
//        val nombresAlumnosCarrera = alumnosList.map { "${it.first} --> ${it.second}" }
//        adapter= ArrayAdapter(this,android.R.layout.simple_list_item_1,nombresAlumnosCarrera)
//        listViewAlumnos.adapter=adapter
//    }
private fun actualizarListAlumnos() {
    try {
        val alumnosList = dbHelper.getAllAlumnosCarrera()

        if (alumnosList.isEmpty()) {
            // Manejar el caso de lista vacía
            listViewAlumnos.adapter = null
            Toast.makeText(this, "No hay alumnos registrados", Toast.LENGTH_SHORT).show()
            return
        }

        // Transformar los datos
        val nombresAlumnosCarrera = alumnosList.map { (alumno, carrera) ->
            "$alumno --> $carrera"
        }

        // Crear y asignar el adaptador
        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            nombresAlumnosCarrera
        )

        listViewAlumnos.adapter = adapter

    } catch (e: Exception) {
        Log.e("ActualizarLista", "Error al actualizar la lista de alumnos", e)
        Toast.makeText(
            this,
            "Error al cargar la lista de alumnos",
            Toast.LENGTH_SHORT
        ).show()
    }
}

}