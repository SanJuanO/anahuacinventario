package com.gesdes.android.puebla.anahuac.Configuracion

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.gesdes.android.puebla.anahuac.Areas
import com.gesdes.android.puebla.anahuac.Encargados
import com.gesdes.android.puebla.anahuac.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_agregarubicacion.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.*

class Agregarubicacion : AppCompatActivity() {
    private val REQUEST_IMAGE_CAPTURE = 101
    private var Imgbase64: String? = null
    var areasn = ArrayList<String>()
    var idareas = ArrayList<String>()
    var idencargado = String()
    var idbuscar=String()
    var bol:Boolean=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregarubicacion)
        val intent = Intent(this, Encargados::class.java)
        val are = Intent(this, Areas::class.java)
        val preferences = this.getSharedPreferences("VARIABLES", Context.MODE_PRIVATE)

        val temp = preferences.getBoolean("PSUB", false)


        encargados.setOnClickListener(View.OnClickListener {
            intent.putExtra("donde", "");
            startActivityForResult(intent, 16) })


        areas.setOnClickListener(View.OnClickListener {
            if(bol==false){
                bol=true
                idareas.clear()
            }
            startActivityForResult(are, 17) })

        fotoubic.setOnClickListener(View.OnClickListener { tomarFoto() })

        crarub.setOnClickListener(View.OnClickListener {

            if (idbuscar != "") {
                if (temp) {


                    modificar()
                }
                } else {

                    guardaDatos()
                }
            }
        )
        val i = getIntent()

        idbuscar = i.getStringExtra("modificar")

        if (idbuscar != "") {
            if (temp) {
                crarub.setText("Modificar")
            }else {
                crarub.setVisibility(View.INVISIBLE);

            }
            tit.setText("Detalle de Ubicación");

            consultar();
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: android.content.Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 16) {
            val preferences = getSharedPreferences("VARIABLES", Context.MODE_PRIVATE)
            val temp = preferences.getString("endcargado", "")

            eencargados.setText(temp)
        }
        if (requestCode == 17) {
            val preferences = getSharedPreferences("VARIABLES", Context.MODE_PRIVATE)
            val temp = preferences.getString("areas", "")
            val temp2 = preferences.getString("idareas", "")
var t=""
            areasn.add(temp.toString())

            idareas.add(temp2.toString())

            for (a in 0..areasn.size-1){

t+=areasn.get(a)
                eareas.setText(t)
                t+="/"
            }


        }

        // if(resultCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val extras = data!!.extras
            val imageBitmap = extras!!["data"] as Bitmap?
            val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(resources, imageBitmap)
            roundedBitmapDrawable.isCircular = false
            fotoubic.setImageDrawable(roundedBitmapDrawable)
            var encodedImage: String? = encodeImage(imageBitmap!!)
            Imgbase64 = encodedImage
        }
    }
    fun tomarFoto() {

        val imageTakeIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (imageTakeIntent.resolveActivity(this.packageManager) != null) {
            startActivityForResult(imageTakeIntent, REQUEST_IMAGE_CAPTURE)
        }
    }
    private fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }
    fun guardaDatos() {

        val progressDialog = ProgressDialog(this,
                R.style.Theme_AppCompat_Light_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Guardando datos...")
        progressDialog.show()
        val preferences = getSharedPreferences("VARIABLES", Context.MODE_PRIVATE)
        val temp = preferences.getString("idencargado", "")
        val nomb = nombubic.text
        val idsareas = JSONArray()
        var idenc = 0
        if (temp != null) {
            idenc = temp.toInt()
        }

        for (a in 0..idareas.size-1) {

            val imagen = JSONObject()
            var id = 0
            if (temp != null) {
                id = idareas.get(a).toInt()
            }
            imagen.put("IdArea",id )
            idsareas.put(imagen)
        }

        val datos = JSONObject()
        try {
            datos.put("Ubicacion", nomb)
            datos.put("IdResponsable", idenc)
            datos.put("Imagen", Imgbase64)
            datos.put("ListaAreas", idsareas)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val requstQueue = Volley.newRequestQueue(this)
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, "https://uap-inventarios-wa.azurewebsites.net/api/UbicacionesApi/addUbicacionMovil", datos,
                Response.Listener { response ->
                    try {
                        progressDialog?.dismiss()
                        val result = response["resultado"] as Int
                        if (result == 1) {

                            preferences.edit().remove("endcargado").commit();
                            preferences.edit().remove("areas").commit();
                            preferences.edit().remove("idareas").commit();

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


    fun modificar() {

        val progressDialog = ProgressDialog(this,
                R.style.Theme_AppCompat_Light_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Guardando datos...")
        progressDialog.show()
        val preferences = getSharedPreferences("VARIABLES", Context.MODE_PRIVATE)
        val temp = preferences.getString("idencargado", "")
        val nomb = nombubic.text
        val idsareas = JSONArray()
        var idenc = 0
        if (temp != null) {
            if (!temp.isEmpty()) {
                idenc = temp.toInt()
            }else{
                idenc = idencargado.toInt()
            }
        }

        for (a in 0..idareas.size-1) {

            val imagen = JSONObject()
            var id = 0

                id = idareas.get(a).toInt()

            imagen.put("IdArea",id )
            idsareas.put(imagen)
        }

        val datos = JSONObject()
        try {
            datos.put("Ubicacion", nomb)
            datos.put("Id", idbuscar.toInt())
            datos.put("IdResponsable", idenc)
            datos.put("Imagen", Imgbase64)
            datos.put("ListaAreas", idsareas)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val requstQueue = Volley.newRequestQueue(this)
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, "https://uap-inventarios-wa.azurewebsites.net/api/UbicacionesApi/saveUbicacion", datos,
                Response.Listener { response ->
                    try {
                        progressDialog?.dismiss()
                        val result = response["resultado"] as Int
                        if (result == 1) {
                            preferences.edit().remove("endcargado").commit();
                            preferences.edit().remove("areas").commit();
                            preferences.edit().remove("idareas").commit();
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



    fun consultar() {

        val progressDialog = ProgressDialog(this,
                R.style.Theme_AppCompat_Light_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Guardando datos...")
        progressDialog.show()

        val temp = idbuscar
        val nomb = nombubic.text
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
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, "https://uap-inventarios-wa.azurewebsites.net/api/UbicacionesApi/getUbicacionById", datos,
                Response.Listener { response ->
                    try {
                        progressDialog?.dismiss()
                        val result = response["resultado"] as Int
                        if (result == 1) {
                            val costos = response.getJSONObject("data")

                            val dos = costos.getJSONArray("listaAreas")
                            var areas =""
                            for (i in 0 until dos.length()) {
                                val producto = dos.getJSONObject(i)
                                var area = producto.getString("area")
                                areas=areas+area
                                eareas.setText(areas)
                                 idareas.add( producto.getString("idArea"))
areas=areas+"/"
                            }
                            nombubic.setText(costos.getString("ubicacion"))
                            eencargados.setText(costos.getString("responsable"))

                            idencargado=(costos.getString("idResponsable"))
                            var im = costos.getString("imagen")
                            Imgbase64=im
                            im = "https://uap-inventarios-wa.azurewebsites.net/" + im
                            Picasso
                                    .with(this) // give it the context
                                    .load(im) // load the image
                                    .into(fotoubic) // select the ImageView to load it into


                        } else {
                            val error = response.getString("mensaje")
                            _ShowAlert("Intente mas tarde", error)
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

}

