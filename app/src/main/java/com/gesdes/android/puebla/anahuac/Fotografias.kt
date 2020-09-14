package com.gesdes.android.puebla.anahuac

import android.os.Bundle
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Fotografias : AppCompatActivity() {

    //--Declaramos la variable de tipo gridview
    var gridView: GridView? = null

    //--Arreglo de nombres
    val Empresas = arrayOf(
            "google", "youtube", "microsoft", "facebook", "twitter", "apple")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fotografias)

    }
}
