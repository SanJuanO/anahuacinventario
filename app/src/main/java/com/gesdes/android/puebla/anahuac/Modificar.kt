package com.gesdes.android.puebla.anahuac

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_modificar.*
import org.json.JSONException
import org.json.JSONObject

class Modificar : AppCompatActivity() {

    var id = ""
    var idArea = ""
    var idSubarea = ""
    var idUbicacion = ""
    var area = ""
    var ubicacion = ""
    var subarea = ""
    var serie = ""
    var modelo = ""
    var nombre = ""
    var nota = ""
    var correo = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modificar)
        val i = intent

        id = i.getStringExtra("id")
       idArea= i.getStringExtra("idArea")
       idSubarea= i.getStringExtra("idSubarea")
       idUbicacion= i.getStringExtra("idUbicacion")
    area= i.getStringExtra("area")
        ubicacion= i.getStringExtra("ubicacion")
      subarea=  i.getStringExtra("subarea")
       serie= i.getStringExtra("serie")
       modelo= i.getStringExtra("modelo")
      nombre=  i.getStringExtra("nombre")
        nota= i.getStringExtra("nota")

        val preferences = getSharedPreferences("VARIABLES", Context.MODE_PRIVATE)
     correo = preferences.getString("correo", "")!!

        enombre.setText(nombre)
        eserie.setText(serie)
        emodelo.setText(modelo)
        enota.setText(nota)
        eubicacion.setText(ubicacion+"/"+area+"/"+subarea)

        btnubicacion.setOnClickListener { ubic() }
    }
    fun ubic() {
        val intent = Intent(this, Fabrica::class.java)
        intent.putExtra("config", "ubicacion")
        startActivityForResult(intent, 11)
    }
    fun btnmodificar(view: View) {
guardaDatos()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: android.content.Intent?) {
        super.onActivityResult(requestCode, resultCode, data)



        if (requestCode == 11) {

            val preferences =  getSharedPreferences("VARIABLES", Context.MODE_PRIVATE)
            var temp = preferences . getString ("UBICACIONAREA", "")
            var temp2 = preferences . getString ("SELECTSUB", "")

            if (temp2 != "") {

                val idarea = preferences.getString("SELECTAREAID", "0")!!
                val idubicacion = preferences.getString("SELECTUBICACIONID", "0")!!
                val sub = preferences.getString("SELECTSUBID", "0")!!
                if(idarea!="0" || idubicacion!="0" || sub!="0"){

                    idArea=idarea
                    idUbicacion=idubicacion
                    idSubarea=sub
                }

                eubicacion.setText(temp + "/" + temp2)

            }

        }
    }

    fun guardaDatos() {

        val progressDialog = ProgressDialog(this,
                R.style.Theme_AppCompat_Light_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Guardando datos...")
        progressDialog.show()

        val idubicacioni = idUbicacion!!.toInt()
        val idare = idArea!!.toInt()
        val subarea = idSubarea!!.toInt()
        val idd = id!!.toInt()

        val datos = JSONObject()
        try {
            datos.put("Idsubarea", subarea)
            datos.put("IdArea", idare)
            datos.put("IdUbicacion", idubicacioni)
            datos.put("Id", idd)
            datos.put("Correo", correo)


        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val requstQueue = Volley.newRequestQueue(this)
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, "https://uap-inventarios-wa.azurewebsites.net/api/ArticulosApi/saveArticuloMovil1", datos,
                Response.Listener { response ->
                    try {
                        progressDialog?.dismiss()
                        val result = response["resultado"] as Int
                        if (result == 1) {

articulos()
                        } else {
                            val error = response.getString("mensaje")
                            _ShowAlert("Error", error)
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error ->
                    progressDialog?.dismiss()
                    Log.e("Rest Response", error.toString())
                }
        ) { //here I want to post data to sever
        }
        val MY_SOCKET_TIMEOUT_MS = 15000
        val maxRetries = 2
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                maxRetries,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requstQueue.add(jsonObjectRequest)
    }

    fun articulos() {

        val progressDialog = ProgressDialog(this,
                R.style.Theme_AppCompat_Light_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Guardando datos...")
        progressDialog.show()
nombre= enombre.text.toString()
modelo= emodelo.text.toString()
nota= enota.text.toString()
serie= eserie.text.toString()
        val idd = id!!.toInt()

        val datos = JSONObject()
        try {
            datos.put("Nombre", nombre)
            datos.put("Modelo", modelo)
            datos.put("Nota", nota)
            datos.put("Serie", serie)
            datos.put("Id", idd)
            datos.put("Correo", correo)


        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val requstQueue = Volley.newRequestQueue(this)
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, "https://uap-inventarios-wa.azurewebsites.net/api/ArticulosApi/saveArticuloMovil0", datos,
                Response.Listener { response ->
                    try {
                        progressDialog?.dismiss()
                        val result = response["resultado"] as Int
                        if (result == 1) {
                            _ShowAlertexito("Datos modificados","exitosamente")


                        } else {
                            val error = response.getString("mensaje")
                            _ShowAlert("Error", error)
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error ->
                    progressDialog?.dismiss()
                    Log.e("Rest Response", error.toString())
                }
        ) { //here I want to post data to sever
        }
        val MY_SOCKET_TIMEOUT_MS = 15000
        val maxRetries = 2
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                maxRetries,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requstQueue.add(jsonObjectRequest)
    }


    private fun _ShowAlertexito(title: String, mensaje: String) {
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle(title)
        alertDialog.setMessage(mensaje)
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Aceptar"
        ) { dialog, which ->
            dialog.dismiss()
            finish()
        }
        alertDialog.show()
    }
    private fun _ShowAlert(title: String, mensaje: String) {
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle(title)
        alertDialog.setMessage(mensaje)
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Aceptar"
        ) { dialog, which ->
            dialog.dismiss()
        }
        alertDialog.show()
    }
}