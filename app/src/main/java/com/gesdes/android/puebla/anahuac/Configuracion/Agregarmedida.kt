package com.gesdes.android.puebla.anahuac.Configuracion

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.gesdes.android.puebla.anahuac.R
import kotlinx.android.synthetic.main.activity_agregarubicacion.*
import kotlinx.android.synthetic.main.activity_agregarubicacion.tit
import kotlinx.android.synthetic.main.activity_categorias.*
import org.json.JSONException
import org.json.JSONObject

class Agregarmedida : AppCompatActivity() {
    private var Nombre=String()
    private var URL=String()
    var idbuscar=String()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_medida)
       // val im = findViewById<View>(R.id.atras) as ImageButton
        //im.setOnClickListener { finish() }
        URL = getString(R.string.URL_API_UNIDADES) + "addUnidadMedida"
        val preferences = this.getSharedPreferences("VARIABLES", Context.MODE_PRIVATE)

        val temp = preferences.getBoolean("PSUM", false)


        val i = getIntent()

        idbuscar = i.getStringExtra("modificar")

        if (idbuscar != "") {
            if (temp) {
                btnagregarcat.setText("Modificar")
            }

            else {
                tit.setText("Detalle de Unidad de Medida");
                btnagregarcat.setVisibility(View.INVISIBLE);
            }
            consultar();
        }

        val guard = findViewById<View>(R.id.btnagregarcat) as Button

        guard.setOnClickListener {
            Nombre = nomcosto.text.toString()
            if (idbuscar != "") {
                if (temp) {

                    modificar()
                }
            }
            else {
                guardaDatos()
            }
        }
    }
    fun modificar() {
        val progressDialog = ProgressDialog(this,
                R.style.Theme_AppCompat_Light_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Guardando datos...")
        progressDialog.show()
        val datos = JSONObject()
        var i =0
        i=idbuscar.toInt()
        try {
            datos.put("UnidadMedida", Nombre)
            datos.put("Id", i)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val requstQueue = Volley.newRequestQueue(this)
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, "https://uap-inventarios-wa.azurewebsites.net/api/UnidadesMedidasApi/saveUnidadMedida", datos,
                Response.Listener { response ->
                    try {
                        progressDialog?.dismiss()
                        val result = response["resultado"] as Int
                        if (result == 1) {
                            _ShowAlert("Bien", "¡Datos actualizados correctamente!")
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

    fun guardaDatos() {
        val progressDialog = ProgressDialog(this,
                R.style.Theme_AppCompat_Light_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Guardando datos...")
        progressDialog.show()
        val datos = JSONObject()
        try {
            datos.put("UnidadMedida", Nombre)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val requstQueue = Volley.newRequestQueue(this)
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, URL, datos,
                Response.Listener { response ->
                    try {
                        progressDialog?.dismiss()
                        val result = response["resultado"] as Int
                        if (result == 1) {
                            _ShowAlert("Bien", "¡Datos actualizados correctamente!")
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

    private fun _ShowAlert(title: String, mensaje: String) {
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle(title)
        alertDialog.setMessage(mensaje)
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK"
        ) { dialog, which ->
            dialog.dismiss()
            finish()
        }
        alertDialog.show()
    }
    fun consultar() {

        val progressDialog = ProgressDialog(this,
                R.style.Theme_AppCompat_Light_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Guardando datos...")
        progressDialog.show()

        val temp = idbuscar
        var id = 0
        if (temp != null) {
            id = temp.toInt()
        }
        val datos = JSONObject()
        try {
            datos.put("Id", id)


        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val requstQueue = Volley.newRequestQueue(this)

        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, getString(R.string.URL_API_UNIDADES) +"getUnidadMedidaById", datos,
                Response.Listener { response ->
                    try {
                        progressDialog?.dismiss()
                        val result = response["resultado"] as Int
                        if (result == 1) {
                            val costos = response.getJSONObject("data")
                            nomcosto.setText(costos.getString("unidadMedida"))


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


}
