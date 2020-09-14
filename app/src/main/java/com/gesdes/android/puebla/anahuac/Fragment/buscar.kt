package com.gesdes.android.puebla.anahuac.Fragment


import BuscarModel
import android.app.Dialog
import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.gesdes.android.puebla.anahuac.Model.Planet
import com.gesdes.android.puebla.anahuac.R
import com.gesdes.android.puebla.anahuac.adapter.CardAdapter_busqueda
import kotlinx.android.synthetic.main.custompopup.*
import kotlinx.android.synthetic.main.fragment_buscar.view.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class Buscar : Fragment() {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private val ARG_PARAM1 = "param1"

    private val URL: String? = null
    var names = ArrayList<String>()
    var names2 = ArrayList<String>()
    lateinit var list:ListView;
    var myDialog: Dialog? = null
lateinit var progressDialog:ProgressDialog
    var busqueda = String()
    var ids = ArrayList<String>()
        var idu=0
        var ida=0
        var idp=0
        var idc=0
        var ide=0
    private var recyclerView: RecyclerView? = null
    private var adapter: CardAdapter_busqueda? = null
    private var planetArrayList: ArrayList<Planet>? = null

    var idubicacion = ArrayList<String>()
    var ubicacion = ArrayList<String>()
    var idarea = ArrayList<String>()
    var areas = ArrayList<String>()
    var idproveedor = ArrayList<String>()
    var proveedor = ArrayList<String>()
    var idestado = ArrayList<String>()
    var estado = ArrayList<String>()
    var idclase = ArrayList<String>()
    var clase = ArrayList<String>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {



        // Inflate the layout for this fragment
        var rootView =inflater.inflate(R.layout.fragment_buscar, container, false)
        val theFilter = rootView.findViewById<EditText>(R.id.etBuscarProductosProductosList) as EditText

        myDialog =  Dialog(requireActivity());
        areas()
        recyclerView = rootView.findViewById<View>(R.id.recyclerViewb) as RecyclerView
        recyclerView!!.layoutManager = LinearLayoutManager(requireActivity())
        planetArrayList = ArrayList()
        adapter = CardAdapter_busqueda(requireActivity(), planetArrayList)
        recyclerView!!.adapter = adapter


        rootView.busqudedaconfg.setOnClickListener(){


            myDialog!!.setContentView(R.layout.custompopup);


            val adapter: ArrayAdapter<String> = ArrayAdapter<String>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, ubicacion)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
            myDialog!!.setContentView(R.layout.custompopup)
            myDialog!!.subicacion.setAdapter(adapter)
            val adapter2: ArrayAdapter<String> = ArrayAdapter<String>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, areas)
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_item)
            myDialog!!.sareas.setAdapter(adapter2)
            val adapter3: ArrayAdapter<String> = ArrayAdapter<String>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, proveedor)
            adapter3.setDropDownViewResource(android.R.layout.simple_spinner_item)
            myDialog!!.sproveedor.setAdapter(adapter3)
            val adapter4: ArrayAdapter<String> = ArrayAdapter<String>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, clase)
            adapter4.setDropDownViewResource(android.R.layout.simple_spinner_item)
            myDialog!!.sclase.setAdapter(adapter4)
            val adapter5: ArrayAdapter<String> = ArrayAdapter<String>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, estado)
            adapter5.setDropDownViewResource(android.R.layout.simple_spinner_item)
            myDialog!!.sestado.setAdapter(adapter5)
            myDialog!!.ocultarmodal.setOnClickListener {


                myDialog!!.dismiss()
            }
            myDialog!!.aceptarmodal.setOnClickListener {


                idu=  myDialog!!.subicacion.selectedItemPosition
                ida=  myDialog!!.sareas.selectedItemPosition
                idp=  myDialog!!.sproveedor.selectedItemPosition
                idc=  myDialog!!.sclase.selectedItemPosition
                ide = myDialog!!.sestado.selectedItemPosition
                idu = idubicacion.get(idu).toInt()
                ida = idarea.get(ida).toInt()
                idc = idclase.get(idc).toInt()
                idp = idproveedor.get(idp).toInt()
                ide = idestado.get(ide).toInt()




                myDialog!!.dismiss()
            }

            myDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            myDialog!!.show()


        }
        theFilter.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                busqueda = charSequence.toString()




            }


            override fun afterTextChanged(editable: Editable) {


            }
        })

        theFilter.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE) {
                    guardaDatos()

                    return true
                }
                return false
            }
        })
        return rootView
    }

    companion object {

        @JvmStatic
        fun newInstance() =Buscar()
    }



    fun guardaDatos() {

        val progressDialog = ProgressDialog(requireActivity(),
                R.style.Theme_AppCompat_Light_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Cargando datos...")
        progressDialog.show()

        val datos = JSONObject()
        try {
            datos.put("IdProveedor", idp)
            datos.put("IdArea", ida)
            datos.put("IdUbicacion", idu)
            datos.put("IdClase", idc)
            datos.put("Codigo", busqueda)
            datos.put("Nombre", busqueda)
            datos.put("IdEstadoArticulo", ide)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val requstQueue = Volley.newRequestQueue(activity)
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, "https://uap-inventarios-wa.azurewebsites.net/api/ArticulosApi/findArticulos", datos,
                Response.Listener { response ->
                    try {
                        val result = response["resultado"] as Int
                        progressDialog.dismiss()
                        if (result == 1) {
                            names2.clear()
                          ids.clear()
                            planetArrayList?.clear()

                            val costos = response.getJSONArray("data")
                            for (i in 0 until costos.length()) {
                                val producto = costos.getJSONObject(i)
                               var im =""
                                val costos2 = producto.getJSONArray("imagenes")

                                for (a in 0 until costos2.length()) {
                                    val producto2 = costos2.getJSONObject(a)

                                im = producto2.getString("imagen")
                                }
                                var planet = Planet(producto.getString("nombre"), producto.getString("ubicacion")+"/"+producto.getString("area")+"/"+producto.getString("subarea"),
                                        producto.getString("codigo"), "", producto.getString("fechaMantenimiento"), producto.getString("color"),im)
                                planetArrayList!!.add(planet)
                            }

                            adapter!!.notifyDataSetChanged()



                        } else {
                            progressDialog.dismiss()

                            val error = response.getString("mensaje")
                        }
                    } catch (e: JSONException) {
                        progressDialog.dismiss()

                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error ->
                    progressDialog.dismiss()

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


    fun ubicacion() {

        val requstQueue = Volley.newRequestQueue(requireActivity())
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.GET, getString(R.string.URL_API_UBICACION) + "getUbicaciones", null,
                Response.Listener { response ->
                    try {
                        val result = response["resultado"] as Int
                        if (result == 1) {
                            ubicacion.clear()
                            idubicacion.clear()
                            ubicacion.add("Ubicación")
                            idubicacion.add("0")
                            val contacts: ArrayList<BuscarModel> = ArrayList()

                            val costos = response.getJSONArray("data")
                            for (i in 0 until costos.length()) {
                                val producto = costos.getJSONObject(i)

                                idubicacion.add(producto.getString("id"))
                                ubicacion.add(producto.getString("ubicacion"))
                            }


                            progressDialog?.dismiss()

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

    fun areas() {
         progressDialog = ProgressDialog(requireActivity(),
                R.style.Theme_AppCompat_Light_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Cargando datos...")
        progressDialog.show()
        val requstQueue = Volley.newRequestQueue(requireActivity())
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.GET, "https://uap-inventarios-wa.azurewebsites.net/api/AreasApi/getAreas", null,
                Response.Listener { response ->
                    try {
                        val result = response["resultado"] as Int
                        if (result == 1) {
                            areas.clear()
                            idarea.clear()
                            idarea.add("0")
                            areas.add("Areas")
                            val contacts: ArrayList<BuscarModel> = ArrayList()

                            val costos = response.getJSONArray("data")
                            for (i in 0 until costos.length()) {
                                val producto = costos.getJSONObject(i)

                                idarea.add(producto.getString("id"))
                                areas.add(producto.getString("area"))
                            }

                            proveedor()



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
    fun proveedor() {

        val requstQueue = Volley.newRequestQueue(requireActivity())
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.GET, getString(R.string.URL_API_PROVEDORES) + "getProveedores", null,
                Response.Listener { response ->
                    try {
                        val result = response["resultado"] as Int
                        if (result == 1) {
                            proveedor.clear()
                            idproveedor.clear()
                            proveedor.add("Proveedor")
                            idproveedor.add("0")
                            val contacts: ArrayList<BuscarModel> = ArrayList()

                            val costos = response.getJSONArray("data")
                            for (i in 0 until costos.length()) {
                                val producto = costos.getJSONObject(i)

                                idproveedor.add(producto.getString("id"))
                                proveedor.add(producto.getString("nombre"))
                            }


                            clase()


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
    fun clase() {

        val requstQueue = Volley.newRequestQueue(requireActivity())
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.GET, getString(R.string.URL_API_CLASES) + "getClases", null,
                Response.Listener { response ->
                    try {
                        val result = response["resultado"] as Int
                        if (result == 1) {
                            clase.clear()
                            idclase.clear()
                            clase.add("Clase")
                            idclase.add("0")
                            val contacts: ArrayList<BuscarModel> = ArrayList()

                            val costos = response.getJSONArray("data")
                            for (i in 0 until costos.length()) {
                                val producto = costos.getJSONObject(i)

                                idclase.add(producto.getString("id"))
                                clase.add(producto.getString("clase"))
                            }

                            estado()


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
    fun estado() {

        val requstQueue = Volley.newRequestQueue(requireActivity())
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.GET, "https://uap-inventarios-wa.azurewebsites.net/api/EstadosArticulosApi/getEstadosArticulos", null,
                Response.Listener { response ->
                    try {
                        val result = response["resultado"] as Int
                        if (result == 1) {
                            estado.clear()
                            idestado.clear()
                            estado.add("Estado")
                            idestado.add("0")
                            val contacts: ArrayList<BuscarModel> = ArrayList()

                            val costos = response.getJSONArray("data")
                            for (i in 0 until costos.length()) {
                                val producto = costos.getJSONObject(i)

                                idestado.add(producto.getString("id"))
                                estado.add(producto.getString("estado"))
                            }


ubicacion()

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

}


