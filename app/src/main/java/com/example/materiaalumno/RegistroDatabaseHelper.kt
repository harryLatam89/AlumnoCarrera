package com.example.materiaalumno

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import kotlinx.coroutines.selects.select

class RegistroDatabaseHelper (context: Context):SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION)   {

    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME=   "Registro.db"

        private const val TABLE_CARRERA= "Carreras"
        private const val COLUMN_CARRERA_ID= "id"
        private const val COLUMN_CARRERA_NOMBRE="nombreC"

        private const val TABLE_ALUMNO="Alumnos"
        private const val COLUMN_ALUMNO_ID="id"
        private const val COLUMN_ALUMNO_NOMBRE="nombreA"
        private const val COLUMN_ALUMNO_ID_CARRERA="id_carrera"
    }

    override fun onCreate(db: SQLiteDatabase) {
       val CREATE_CARRERAS_TABLE=("CREATE TABLE $TABLE_CARRERA ("+
               "$COLUMN_CARRERA_ID INTEGER PRIMARY KEY AUTOINCREMENT, "+
               "$COLUMN_CARRERA_NOMBRE TEXT NOT NULL)")
        db.execSQL(CREATE_CARRERAS_TABLE)

        val CREATE_ALUMNOS_TABLE = ("CREATE TABLE $TABLE_ALUMNO (" +
                "$COLUMN_ALUMNO_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_ALUMNO_NOMBRE TEXT NOT NULL, " +
                "$COLUMN_ALUMNO_ID_CARRERA INTEGER, " +
                "FOREIGN KEY($COLUMN_ALUMNO_ID_CARRERA) REFERENCES $TABLE_CARRERA($COLUMN_CARRERA_ID))")
        db.execSQL(CREATE_ALUMNOS_TABLE)
    }

    fun addCarrera (nombre:String){
        val values=ContentValues()
        values.put(COLUMN_CARRERA_NOMBRE,nombre)
        val db=writableDatabase

        db.insert(TABLE_CARRERA, null, values)
       // db.close()
    }

    fun addAlumno(nombre: String,idcarrera:Int){
        val values=ContentValues()
        values.put(COLUMN_ALUMNO_NOMBRE,nombre)
        values.put(COLUMN_ALUMNO_ID_CARRERA,idcarrera)
        val db=writableDatabase

        db.insert(TABLE_ALUMNO,null, values)
      //  db.close()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }


    fun getAllCarreras(): List<Carrera> {
        val carrerasList = mutableListOf<Carrera>()
        val selectQuery = "SELECT $COLUMN_CARRERA_ID, $COLUMN_CARRERA_NOMBRE FROM $TABLE_CARRERA"

        readableDatabase.use { db ->
            db.rawQuery(selectQuery, null).use { cursor ->
                while (cursor.moveToNext()) {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CARRERA_ID))
                    val nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CARRERA_NOMBRE))
                    carrerasList.add(Carrera(id, nombre))
                }
            }
        }
        return carrerasList
    }

    fun getAllAlumnosCarrera():ArrayList<Pair<String,String>>{
        val alumnoCarreraList=ArrayList<Pair<String,String>>()
        val selectQuery = """
        SELECT ${TABLE_ALUMNO}.${COLUMN_ALUMNO_NOMBRE}, ${TABLE_CARRERA}.${COLUMN_CARRERA_NOMBRE}
        FROM $TABLE_ALUMNO 
        INNER JOIN $TABLE_CARRERA
        ON ${TABLE_ALUMNO}.${COLUMN_ALUMNO_ID_CARRERA} = ${TABLE_CARRERA}.${COLUMN_CARRERA_ID}
    """

//        val db=this.readableDatabase
//        val cursor=db.rawQuery(selectQuery,null)
//        while(cursor.moveToNext()){
//            val nombreAlumno= cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ALUMNO_NOMBRE))
//            val nombreCarrera=cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CARRERA_NOMBRE))
//            val alumnoCarrera=Pair(nombreAlumno,nombreCarrera)
//            alumnoCarraraList.add(alumnoCarrera)
//        }
 //       cursor.close()
      //  db.close()

        try {
            val db = readableDatabase
            db.rawQuery(selectQuery, null).use { cursor ->
                val alumnoIndex = cursor.getColumnIndexOrThrow(COLUMN_ALUMNO_NOMBRE)
                val carreraIndex = cursor.getColumnIndexOrThrow(COLUMN_CARRERA_NOMBRE)
                while (cursor.moveToNext()) {
                    val nombreAlumno = cursor.getString(alumnoIndex)
                    val nombreCarrera = cursor.getString(carreraIndex)
                    alumnoCarreraList.add(Pair(nombreAlumno, nombreCarrera))
                }
            }
        } catch (e: Exception) {
            Log.e("DatabaseError", "Error al obtener alumnos y carreras", e)
        }





        return alumnoCarreraList
    }
}