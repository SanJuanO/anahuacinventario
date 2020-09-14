package com.gesdes.android.puebla.anahuac

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.gesdes.android.puebla.anahuac.Manaer.PrintfManager
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detalle.*
import kotlinx.android.synthetic.main.activity_modificar.*
import net.glxn.qrgen.android.QRCode
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.util.*


class   Detalle : AppCompatActivity() {
    var imagenes = ArrayList<String>()
    var imagenessin = ArrayList<String>()
    var estado = ArrayList<String>()
    var idestado = ArrayList<String>()
    var ciudadano = ""
    var telefono = ""
    var imagen = String()
    var titulod = String()
    var id = String()
    var codigoqr = String()
    var serie = String()

    var URL=String()
    var URL2 =String()
    var codigo = String()


    var printfManager: PrintfManager? =null

    var nombre = String()
    var descripcion = String()
    var idClase = String()
    var clase = String()
    var modelo = String()
    var costo = String()
    var cantidad = String()
    var cantidadAviso = String()
    var idUnidadMedida = String()
    var unidadMedida = String()
    var nota = String()
    var idProveedor = String()
    var proveedor = String()

    var idUbicacion = String()
    var ubicacion = String()
    var imagenUbicacion = String()
    var idArea = String()
    var area = String()
    var idSubarea = String()
    var subarea = String()
    var imagenArea = String()
    var idCentroCostos = String()
    var centroCostos = String()
    var noFactura = String()
    var diasMantenimiento = String()
    var diasAviso = String()
    var fechaAlta = String()
    var fechaUltimoMantenimiento = String()
    var fechaMantenimiento = String()
    var fechaAviso = String()
    var idResponsableUbicacion = String()
    var correoResponsableUbicacion = String()
    var tokenResponsableUbicacion = String()
    var idResponsableArea = String()
    var correoResponsableArea = String()
    var tokenResponsableArea = String()
    var idEstadoArticulo = String()
var escaneo ="false"
    var estadot = String()
    var color = String()
    private var context: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imagenes.clear()
        imagenessin.clear()
        estado.clear()
        idestado.clear()
        setContentView(R.layout.activity_detalle)
        URL= getString(R.string.URL_API_ARTICULOS) + "getArticuloByCodigo"
        URL2 =getString(R.string.URL_API_ARTICULOS) + "getArticuloByCodigo  "
        progress_Bar2.visibility = View.VISIBLE
serie=""
        val i = intent.getStringExtra("folio")
         escaneo= intent.getStringExtra("escaneo")
        codigo=i

        progress_Bar2.visibility = View.INVISIBLE

detalle()
        context = this

        printfManager = PrintfManager.getInstance(this)
        printfManager?.defaultConnection()

    }
    fun withEditText(view: View) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setMessage("¿Deseas modificar el estatus del articulo?")
                .setPositiveButton("Aceptar") { dialog, id ->

                    modificar()
                }
                .setNegativeButton("Caancelar") { dialog, id ->
                    // User cancelled the dialog
                }
        // Create the AlertDialog object and return it
        // Create the AlertDialog object and return it
        builder.create()
        builder.show()
    }
    fun mantenimiento(view: View) {
        val intent = Intent(this, Mantenimiento::class.java)
        intent.putExtra("id", id)

        startActivity(intent)
    }
    fun modificar(view: View) {
        val intent = Intent(this, Modificar::class.java)
        intent.putExtra("id", id)
        intent.putExtra("idArea", idArea)
        intent.putExtra("idSubarea", idSubarea)
        intent.putExtra("idUbicacion", idUbicacion)
        intent.putExtra("area", area)
        intent.putExtra("ubicacion", ubicacion)
        intent.putExtra("subarea", subarea)
        intent.putExtra("serie", serie)
        intent.putExtra("modelo", modelo)
        intent.putExtra("nombre", nombre)
        intent.putExtra("nota", nota)


        startActivityForResult(intent,90)
    }
    override fun onBackPressed() {
        // super.onBackPressed();
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        this.finish();
    }
    fun detalle() {
        val preferences = getSharedPreferences("VARIABLES", Context.MODE_PRIVATE)
        val temp = preferences.getString("correo", "")


        val datos = JSONObject()
        try {
            datos.put("Codigo", codigo)
            datos.put("Correo", temp)
            if(escaneo=="true") {

                datos.put("Escaneado", true)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val requstQueue = Volley.newRequestQueue(this@Detalle)
        val progressDialog = ProgressDialog(this,
                R.style.Theme_AppCompat_Light_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Cargando datos...")
        progressDialog.show()
        val jsonObjectRequest = object : JsonObjectRequest(
                Request.Method.POST, URL2, datos,
                Response.Listener<JSONObject> { response ->
                               try {
                                   progressDialog?.dismiss()
                        val result = response.get("resultado") as Int
                                   progressDialog?.dismiss()
                        if (result == 1) {
                            try  {


                                val guias = response.getJSONObject("data")
                                codigofolio.text = guias.getString("codigo")
                                val texto = codigofolio.text.toString()
                                val bitmap: Bitmap = QRCode.from(texto).bitmap()

                                var d = guias.getString("id")
id=d.toString()

                                 descripcion = guias.getString("descripcion")
                                 idClase = guias.getString("idClase")
                                 clase = guias.getString("clase")
                                 modelo = guias.getString("modelo")
                                 costo = guias.getString("costo")
                                 cantidad = guias.getString("cantidad")
                                 cantidadAviso = guias.getString("cantidadAviso")
                                 idUnidadMedida = guias.getString("idUnidadMedida")
                                 unidadMedida = guias.getString("unidadMedida")
                                 nota = guias.getString("nota")
                                 idProveedor = guias.getString("idProveedor")
                                 proveedor = guias.getString("proveedor")

                                 idUbicacion = guias.getString("idUbicacion")
                                 ubicacion = guias.getString("ubicacion")
                                 imagenUbicacion = guias.getString("imagenUbicacion")
                                 idArea = guias.getString("idArea")
                                 area = guias.getString("area")
                                idSubarea = guias.getString("idSubarea")
                                subarea = guias.getString("subarea")
                                 imagenArea = guias.getString("imagenArea")
                                 idCentroCostos = guias.getString("idCentroCostos")
                                 centroCostos = guias.getString("centroCostos")
                                 noFactura = guias.getString("noFactura")
                                 diasMantenimiento = guias.getString("diasMantenimiento")
                                 diasAviso = guias.getString("diasAviso")
                                 fechaAlta = guias.getString("fechaAlta")
                                 fechaUltimoMantenimiento = guias.getString("fechaUltimoMantenimiento")
                                 fechaMantenimiento = guias.getString("fechaMantenimiento")
                                 fechaAviso = guias.getString("fechaAviso")
                                 idResponsableUbicacion = guias.getString("idResponsableUbicacion")
                                 correoResponsableUbicacion = guias.getString("correoResponsableUbicacion")
                                 tokenResponsableUbicacion = guias.getString("tokenResponsableUbicacion")
                                 idResponsableArea = guias.getString("idResponsableArea")
                                 correoResponsableArea = guias.getString("correoResponsableArea")
                                 tokenResponsableArea = guias.getString("tokenResponsableArea")
                                 idEstadoArticulo = guias.getString("idEstadoArticulo")
                                 codigoqr = guias.getString("codigoQr")

                                 estadot = guias.getString("estado")
                                 color = guias.getString("color")
                                imagenfolio.setImageBitmap(bitmap)
                                dnombre.text   = guias.getString("nombre")
                                nombre = guias.getString("nombre")
                                serie = guias.getString("serie")

                                ddescrip.text = guias.getString("descripcion")
                                dserie.text = guias.getString("serie")

                                ddescrip.text = guias.getString("descripcion")
                                idestado.add( guias.getString("idEstadoArticulo"))
                                estado.add( guias.getString("estado"))

                                dclase.text = guias.getString("clase")
                                dmodelo.text = guias.getString("modelo")
                                dcosto.text = guias.getString("costo")
                                dcantidad.text = guias.getString("cantidad")
                                //dtitulo.text = guias.getString("cantidadAviso")
                                dunidad.text = guias.getString("unidadMedida")
                                dnota.text = guias.getString("nota")

                                dprovedor.text = guias.getString("proveedor")
                                dubicacion.text = ubicacion+"/"+area+"/"+subarea
                                dcostos.text = guias.getString("centroCostos")
                                dfactura.text = guias.getString("noFactura")
                                dmantenimieno.text = guias.getString("diasMantenimiento")
                                davisomantenimiento.text = guias.getString("diasAviso")
                                var im2 = guias.getString("imagenUbicacion")
                                im2 = "https://uap-inventarios-wa.azurewebsites.net/"+ im2
                                Picasso
                                        .with(this) // give it the context
                                        .load(im2) // load the image
                                        .networkPolicy(NetworkPolicy.NO_CACHE)
                                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                                        .into(fotoubc) // select the ImageView to load it into

                               titulod = guias.getString("nombre")

                                val costos = guias.getJSONArray("imagenes")
                                for (i in 0 until costos.length()) {
                                    val producto = costos.getJSONObject(i)
                                    var im = producto.getString("imagen")
                                    imagenessin.add(im)

                                    im = "https://uap-inventarios-wa.azurewebsites.net/" + im
                                    Picasso
                                            .with(this) // give it the context
                                            .load(im) // load the image
                                            .networkPolicy(NetworkPolicy.NO_CACHE)
                                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                                            .into(adinciona) // select the ImageView to load it into
                                    imagenes.add(im)

                                }

                                estado()


                            } catch (es: Exception) {
                                Log.d("sergio1", "" + es.toString())
                                progressDialog?.dismiss()
                            }

                        } else {
                            Toast.makeText(this, "Articulo no encotrado", Toast.LENGTH_SHORT).show()
                        }

                    } catch (e: JSONException) {
                        e.printStackTrace()
                                   progressDialog?.dismiss()
                    }
                },
                object : Response.ErrorListener {
                    override fun onErrorResponse(error: VolleyError) {
                        Toast.makeText(this@Detalle, "falta login  on error:" + error, Toast.LENGTH_LONG).show()

                    }
                }
        ) {

            //here I want to post data to sever
        }

        val MY_SOCKET_TIMEOUT_MS = 15000
        val maxRetries = 2
        jsonObjectRequest.setRetryPolicy(
                DefaultRetryPolicy(
                        MY_SOCKET_TIMEOUT_MS,
                        maxRetries,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        )

        requstQueue.add(jsonObjectRequest)


    }



fun imagengrande(view: View){

    val intent = Intent(this, Main2Activity::class.java)
    val imageUrls: Array<String>
    intent.putExtra("imagenes", imagenes)
    intent.putExtra("titulod", titulod)

    startActivity(intent)


}
    fun imprimirqr(view: View){

        val texto = codigofolio.text.toString()
        val bitmapqr = QRCode.from(texto).withSize(220 ,220) .bitmap()
        //dimensiones de la imagen
        //dimensiones de la imagen
        val ancho = 400
        val alto = 240

        //se define el mapa de bits

        //se define el mapa de bits
        val bitmap = Bitmap.createBitmap(ancho, alto, Bitmap.Config.ARGB_8888)
        //se define el lienzo
        //se define el lienzo
        val canvas = Canvas(bitmap)
        //estilos
        //estilos
        val paint = Paint()
        val paint2 = Paint()
        val paintt = Paint()

        paint.setColor(Color.parseColor("#ff950b"))
        canvas.drawPaint(paint)



        //pintando imagen desde la carpeta de recursos

        canvas.drawBitmap(bitmapqr,-30f, 20f, paint)

        //pintando texto
        paint2.setAntiAlias(true)
        paint2.setTextSize(16f)
        paint2.setTextAlign(Paint.Align.CENTER)
        paint2.setColor(Color.parseColor("#0B0B61"))
        //pintando texto
        paint.setAntiAlias(true)
        paint.setTextSize(14f)
        paint.setTextAlign(Paint.Align.CENTER)
        paint.setColor(Color.parseColor("#0B0B61"))
        paintt.setAntiAlias(true)
        paintt.setTextSize(12f)
        paintt.setTextAlign(Paint.Align.CENTER)
        paintt.setColor(Color.parseColor("#0B0B61"))
        canvas.drawText(descripcion,  185f, 30f, paint)
         canvas.drawText("FOLIO:",  270f, 50f, paint)
         canvas.drawText(codigo,  270f, 70f, paint2)

         canvas.drawText("UBICACIÓN:",  270f, 90f, paint)
         canvas.drawText(ubicacion,  270f, 110f, paint2)
         canvas.drawText("AREA:",  270f, 130f, paint)
         canvas.drawText(area,  270f, 150f, paint2)
         canvas.drawText("SUBAREA:",  270f, 170f, paint)
         canvas.drawText(subarea,  270f, 190f, paint)
        canvas.drawText("ARTICULO:",  270f,     210f, paint)
        canvas.drawText("Universidad Anáhuac Puebla",  270f, 230f, paint)
        //agrega el bitmap al ImageView


        printfManager?.printf(50, 30, bitmap, this@Detalle)


    }
    fun getBitmapFromURL(src: String?): Bitmap? {
        return try {
            val url = java.net.URL(src)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.setDoInput(true)
            connection.connect()
            val input: InputStream = connection.getInputStream()
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun editarimagengrande(view: View){

        val intent = Intent(this, Fotoadd::class.java)
        val imageUrls: Array<String>
        intent.putExtra("imagenes", imagenessin)
        intent.putExtra("id", id)

        startActivityForResult(intent,80)


    }
    fun estado() {

        val requstQueue = Volley.newRequestQueue(this)
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.GET, "https://uap-inventarios-wa.azurewebsites.net/api/EstadosArticulosApi/getEstadosArticulos", null,
                Response.Listener { response ->
                    try {
                        val result = response["resultado"] as Int
                        if (result == 1) {


                            val costos = response.getJSONArray("data")
                            for (i in 0 until costos.length()) {
                                val producto = costos.getJSONObject(i)

                                if(idestado.get(0)!=producto.getString("id")) {
                                    idestado.add(producto.getString("id"))
                                    estado.add(producto.getString("estado"))
                                }
                            }

                            val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, estado)
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
                            sproveedor.setAdapter(adapter)


                        } else {
                            val error = response.getString("mensaje")
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()

                    }
                },
                Response.ErrorListener { error -> Log.e("Rest Response", error.toString()) }
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
        progressDialog.setMessage("Cargando datos...")
        progressDialog.show()
        val preferences = getSharedPreferences("VARIABLES", Context.MODE_PRIVATE)
        val temp = preferences.getString("correo", "")
        var i= 0
        i = id.toInt()
        var idu= sproveedor.selectedItemPosition
        var idarticulo=idestado.get(idu)
        val datos = JSONObject()
        try {
            datos.put("Id", i)
            datos.put("IdEstadoArticulo", idarticulo.toInt())
            datos.put("Correo", temp)


        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val requstQueue = Volley.newRequestQueue(this)
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, "https://uap-inventarios-wa.azurewebsites.net/api/articulosapi/saveEstadoArticulo", datos,
                Response.Listener { response ->
                    try {
                        progressDialog?.dismiss()
                        val result = response["resultado"] as Int
                        if (result == 1) {
                            _ShowAlert("Bien", "¡Estatus actualizado correctamente!")
                        } else {
                            val error = response.getString("mensaje")
                            _ShowAlert("Error", error)
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error ->
                    Log.e("Rest Response", error.toString())
                    progressDialog?.dismiss()

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
   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

       if (requestCode == 80) {
            imagenes.clear()
            imagenessin.clear()
            estado.clear()
            idestado.clear()

     detalle()
       }
       if (requestCode == 90) {
           imagenes.clear()
           imagenessin.clear()
           estado.clear()
           idestado.clear()

           detalle()
       }
    }
}
