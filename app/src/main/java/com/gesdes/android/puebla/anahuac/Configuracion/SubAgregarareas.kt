package com.gesdes.android.puebla.anahuac.Configuracion

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.gesdes.android.puebla.anahuac.Areas
import com.gesdes.android.puebla.anahuac.Encargados
import com.gesdes.android.puebla.anahuac.R
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_agregarubicacion.*
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class SubAgregarareas : AppCompatActivity() {
    private val REQUEST_IMAGE_CAPTURE = 101
    private var Imgbase64: String? = null
    var areas = ArrayList<String>()
    var idareas = ArrayList<String>()
    var idbuscar=String()
    var idarea=String()
    var responsable=String()
    var idresponsable=String()
    var imagen=String()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregarsubareas)
        val intent = Intent(this, Encargados::class.java)
        val are = Intent(this, Areas::class.java)
        val preferences = this.getSharedPreferences("VARIABLES", Context.MODE_PRIVATE)

        val temp = preferences.getBoolean("PSARE", false)
        val i = getIntent()

        idbuscar = i.getStringExtra("modificar")
            encargados.setOnClickListener(View.OnClickListener { startActivityForResult(intent, 14) })

        fotoubic.setOnClickListener(View.OnClickListener {

            tomarFoto()

        })
        crarub.setOnClickListener(View.OnClickListener {


            if (idbuscar != "") {
                if(temp) {
                    modificar()
                }

            }else {

                guardaDatos()
            }
        })


        if (idbuscar != "") {

            if (temp) {
                crarub.setText("Modificar")
            }else {
                tit.setText("Detalle de Area");
            }
            consultar();
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: android.content.Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 14) {
            val preferences = getSharedPreferences("VARIABLES", Context.MODE_PRIVATE)
            val temp = preferences.getString("endcargado", "")
            eencargados.setText(temp)
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
        val temp = preferences.getString("idencargado", "0")
        val nomb = nombubic.text
        var id = 0
        if (temp != null) {
            id = temp.toInt()
        }
        val datos = JSONObject()
        try {
            datos.put("Subarea", nomb)
            datos.put("IdResponsable", id)
            datos.put("Imagen", Imgbase64)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val requstQueue = Volley.newRequestQueue(this)
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, "https://uap-inventarios-wa.azurewebsites.net/api/SubareasApi/addSubareaMovil", datos,
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
    fun modificar() {

        val progressDialog = ProgressDialog(this,
                R.style.Theme_AppCompat_Light_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Guardando datos...")
        progressDialog.show()
        val preferences = getSharedPreferences("VARIABLES", Context.MODE_PRIVATE)
        val temp = preferences.getString("idencargado", "")
        val nomb = nombubic.text
        var id = 0
var i=idbuscar.toInt()

            if (temp !="") {
                if (temp != null) {
                    id = temp.toInt()
                }

            }
        else {
                id = idresponsable.toInt()
            }
        val datos = JSONObject()
        try {
            datos.put("Id", i)
            datos.put("Subarea",nomb )
            datos.put("IdResponsable", id)

            datos.put("Imagen", Imgbase64)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val requstQueue = Volley.newRequestQueue(this)
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, "https://uap-inventarios-wa.azurewebsites.net/api/SubareasApi/saveSubarea", datos,
                Response.Listener { response ->
                    try {
                        progressDialog?.dismiss()
                        val result = response["resultado"] as Int
                        if (result == 1) {
                            preferences.edit().remove("idencargado").commit();
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
        val jsonObjectRequest: JsonObjectRequest = @RequiresApi(Build.VERSION_CODES.O)
        object : JsonObjectRequest(Method.POST, "https://uap-inventarios-wa.azurewebsites.net/api/SubareasApi/getSubareaById", datos,
                Response.Listener { response ->
                    try {
                        progressDialog?.dismiss()
                        val result = response["resultado"] as Int
                        if (result == 1) {
                            val costos = response.getJSONObject("data")
                                nombubic.setText(costos.getString("subarea"))
                              idarea = costos.getString("id")


                                var im = costos.getString("imagen")
                            imagen=im
                            Imgbase64=im

                            eencargados.setText(costos.getString("responsable"))
                            idresponsable = costos.getString("idResponsable")

                                im = "https://uap-inventarios-wa.azurewebsites.net/" + im
                                Picasso
                                        .with(this) // give it the context
                                        .load(im) // load the image
                                        .networkPolicy(NetworkPolicy.NO_CACHE)
                                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                                        .into(fotoubic) // select the ImageView to load it into





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
    fun getBitmapFromURL(src: String?): Bitmap? {
        return try {
            val url = URL(src)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.setDoInput(true)
            connection.connect()
            val input: InputStream = connection.getInputStream();
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            // Log exception
            null
        }
    }



}



