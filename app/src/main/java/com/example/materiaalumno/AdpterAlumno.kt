package com.example.materiaalumno

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class AdpterAlumno( private val context: Activity,
                    private val arrayList: ArrayList<AlumnoData>,
                   ): ArrayAdapter<AlumnoData>(context,R.layout.list_item,arrayList )
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater=LayoutInflater.from(context)
        val view: View= inflater.inflate(R.layout.list_item, null)
        val txtLable: TextView= view.findViewById(R.id.txtPersona)
        txtLable.text= arrayList[position].nombre + "\n"+ arrayList[position].nombreCarrera
        return view
    }

}