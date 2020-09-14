package com.gesdes.android.puebla.anahuac

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.activity_informe.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class Informe : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informe)
        consultar()


    }

    fun consultar() {

        val progressDialog = ProgressDialog(this,
                R.style.Theme_AppCompat_Light_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Guardando datos...")
        progressDialog.show()

        val preferences = getSharedPreferences("VARIABLES", Context.MODE_PRIVATE)
        val correo = preferences.getString("correo", "")

        val datos = JSONObject()
        try {
            datos.put("Correo", correo)


        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val requstQueue = Volley.newRequestQueue(this)
        val jsonObjectRequest: JsonObjectRequest = @RequiresApi(Build.VERSION_CODES.O)
        object : JsonObjectRequest(Method.POST, "https://uap-inventarios-wa.azurewebsites.net/api/DashboardApi/Estadisticas", datos,
                Response.Listener { response ->
                    try {
                        progressDialog?.dismiss()
                        val result = response["resultado"] as Int
                        if (result == 1) {
                            val costos = response.getJSONObject("data")
                            atrazados.setText(costos.getString("atrazados"))
                            avisos.setText(costos.getString("avisos"))
                            articulosNuevos.setText(costos.getString("articulosNuevos"))
                            articulosBuenos.setText(costos.getString("articulosBuenos"))
                            articulosRegulares.setText(costos.getString("articulosRegulares"))
                            articulosMalos.setText(costos.getString("articulosMalos"))

                            pie.setUsePercentValues(true)
                        val atrazados=  (costos.getString("atrazados"))
                        val avisos=   (costos.getString("avisos"))
                        val articulosNuevos=  (costos.getString("articulosNuevos"))
                        val articulosBuenos=  (costos.getString("articulosBuenos"))
                        val articulosRegulares=  (costos.getString("articulosRegulares"))
                        val articulosMalos=  (costos.getString("articulosMalos"))

                            val atrazadosb=atrazados.toFloat()
                            val avisosb=avisos.toFloat()
                            val articulosNuevosb=articulosNuevos.toFloat()
                            val articulosBuenosb=articulosBuenos.toFloat()
                            val articulosRegularesb=articulosRegulares.toFloat()
                            val articulosMalosb=articulosMalos.toFloat()

                            val data: MutableList<PieEntry> = ArrayList<PieEntry>()
                            data.add(PieEntry(atrazadosb, "Atrazados"))
                            data.add(PieEntry(avisosb, "Avisos"))
                            data.add(PieEntry(articulosNuevosb, "Nuevos"))
                            data.add(PieEntry(articulosBuenosb, "Buenos"))
                            data.add(PieEntry(articulosRegularesb, "Regulares"))
                            data.add(PieEntry(articulosMalosb, "Malos"))

                            val  piedataset= PieDataSet(data,"Articulos")
                            piedataset.setColors(Color.RED, Color.GREEN, Color.BLUE,Color.YELLOW,Color.MAGENTA,Color.CYAN);

                            val piedata= PieData(piedataset)
                            pie.setData(piedata)


                        } else {
                            val error = response.getString("mensaje")

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