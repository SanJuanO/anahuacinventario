package com.gesdes.android.puebla.anahuac

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.gesdes.android.puebla.anahuac.Fragment.AgregarProducto
import com.gesdes.android.puebla.anahuac.Fragment.Buscar
import com.gesdes.android.puebla.anahuac.Fragment.Configuracion
import com.gesdes.android.puebla.anahuac.Fragment.Incidencias
import com.gesdes.android.puebla.anahuac.Utilidades.AuthenticationManager
import com.gesdes.android.puebla.anahuac.Utilidades.ConexionSQLiteHelper
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class MainActivity : AppCompatActivity() {
    var TOKEN: String=""
    var id: String=""
    var correo= String()
    var toque:Boolean= false
    var permisos = ArrayList<String>()
    override fun onDestroy() {
        super.onDestroy()
        AuthenticationManager.getInstance().disconnect()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val preferences = getSharedPreferences("VARIABLES", Context.MODE_PRIVATE)
        val temp = preferences.getString("correo", "")
        val t = preferences.getString("iduser", "")!!
id=t
            correo = temp.toString()
        val conn = ConexionSQLiteHelper(this, "bd_usuarios", null, 1)
        agregaToken()
        alertas.setOnClickListener(View.OnClickListener {
            if (toque == false) {
                toque = false
                val intent = Intent(this, Notificaciones::class.java)

                // start your next activity
                startActivity(intent)
            }
        })

        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigationViewinicio)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val vent = Incidencias.newInstance()
        openFragment(vent)

    }
    override fun onBackPressed() { // Añade más funciones si fuese necesario
finish()
    }


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {

            R.id.incidencia -> {
                tituloma.text="Articulo QR Code"

                val inci = Incidencias.newInstance()
                openFragment(inci)
                return@OnNavigationItemSelectedListener true
            }
            R.id.agregar -> {
                tituloma.text="Agregar Artículo"
                //alertas.setImageResource(R.color.ARED)

                val inci = AgregarProducto.newInstance()
                openFragment(inci)
                return@OnNavigationItemSelectedListener true
            }
            R.id.configuracion -> {
                tituloma.text="Configuracion"
               // alertas.setImageResource(R.color.ARED)

                val inci = Configuracion.newInstance()
                openFragment(inci)
                return@OnNavigationItemSelectedListener true
            }
            R.id.articulos -> {
                tituloma.text="Artículos"
                //alertas.setBackgroundColor(R.drawable.btn_orange_light)
                val inci = Buscar.newInstance()
                openFragment(inci)
                return@OnNavigationItemSelectedListener true
            }

        }
        false
    }
    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction. commit()
    }
fun cerrarsesion(view: View){
    val preferencias = this.getSharedPreferences("variables", Context.MODE_PRIVATE)

    val editor = preferencias.edit()
        editor.putString("login", "sesioncerrada")
        editor.commit()
        val intent = Intent(this, ConnectActivity::class.java)

        // start your next activity
        startActivity(intent)
        this.finish();
    }
    fun agregaToken(){

        FirebaseApp.initializeApp(this)
        TOKEN = FirebaseInstanceId.getInstance().getToken().toString()
        RegisterToken()

    }

    fun RegisterToken() {


        val datos = JSONObject()
        try {
            val idd = id.toInt()
            datos.put("Id", idd)
            datos.put("TOKEN", TOKEN)

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val requestQueue = Volley.newRequestQueue(this)

        val jsonObjectRequest = object : JsonObjectRequest(Request.Method.POST,"https://uap-inventarios-wa.azurewebsites.net/api/UsuariosApi/saveTokenUsuario", datos,
                object : Response.Listener<JSONObject> {
                    override fun onResponse(response: JSONObject) {

                        try {

                            var mensaje = response.getInt("result")




                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }


                    }
                },
                object : Response.ErrorListener {
                    override fun onErrorResponse(error: VolleyError) {

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

        requestQueue.add(jsonObjectRequest)
    }

    fun permisosobtener() {


        val datos = JSONObject()
        try {
            datos.put("Correo", correo)

        } catch (e: Exception) {
            e.printStackTrace()
        }

        val requstQueue = Volley.newRequestQueue(this)
        val progressDialog = ProgressDialog(this,
                R.style.Theme_AppCompat_Light_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Cargando datos...")
        progressDialog.show()
        val jsonObjectRequest = object : JsonObjectRequest(
                Request.Method.POST, "https://uap-inventarios-wa.azurewebsites.net/api/UsuariosApi/getUsuarioByCorreo", datos,
                Response.Listener<JSONObject> { response ->
                    try {
                        progressDialog?.dismiss()
                        val result = response.get("resultado") as Int
                        progressDialog?.dismiss()
                        if (result == 1) {
                            try {


                                val guias = response.getJSONObject("data")
                                        id = guias.getString("id")


                                val preferencias = this.getSharedPreferences("variables", Context.MODE_PRIVATE)

                                val editor = preferencias.edit()
                                val costos = guias.getJSONArray("listaPermisos")
                                for (i in 0 until costos.length()) {
                                    val producto = costos.getJSONObject(i)
                                }
                                agregaToken()


                            } catch (es: Exception) {
                                Log.d("sergio1", "" + es.toString())
                                finish()
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



}
