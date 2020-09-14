/*
 * Copyright (c) Microsoft. All rights reserved. Licensed under the MIT license.
 * See LICENSE in the project root for license information.
 */
package com.gesdes.android.puebla.anahuac

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.gesdes.android.puebla.anahuac.Utilidades.AuthenticationManager
import com.gesdes.android.puebla.anahuac.Utilidades.Constants
import com.microsoft.aad.adal.AuthenticationCallback
import com.microsoft.aad.adal.AuthenticationResult
import kotlinx.android.synthetic.main.activity_login_activity.*
import org.json.JSONException
import org.json.JSONObject
import java.net.URI
import java.util.*

/**
 * Starting activity of the app. Handles the connection to Office 365.
 * When it first starts it only displays a button to Connect to Office 365.
 * If there are no cached tokens, the user is required to sign in to Office 365.
 * If there are cached tokens, the app tries to reuse them.
 * The activity redirects the user to the SendMailActivity upon successful connection.
 */
class ConnectActivity : AppCompatActivity() {
    var progressDialog:ProgressDialog? = null
    var t=""
    val id=""
    var sendMailIntent: Intent? =null
    var REQUEST_LOCATION = 1
    private var mConnectButton: Button? = null
    private var mTitleTextView: TextView? = null
    private var mConnectProgressBar: ProgressBar? = null
    private var mDescriptionTextView: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_activity)


        progressDialog = ProgressDialog(this,
                R.style.Theme_AppCompat_Light_Dialog)
        progressDialog!!.isIndeterminate = true
        progressDialog?.setMessage("Ingresando...")
        val btn = findViewById<View>(R.id.connectButton) as Button
        pedirPermisos()
        initializeViews()
    }

    /**
     * Event handler for the onclick event of the button.
     * @param v The view that sent the event.
     */
    fun onConnectButtonClick(v: View?) {
        //showConnectingInProgressUI();


        progressDialog?.show()
        mConnectButton!!.visibility = View.INVISIBLE
        //check that client id and redirect have been set correctly
        try {
            UUID.fromString(Constants.CLIENT_ID)
            URI.create(Constants.REDIRECT_URI)
        } catch (e: IllegalArgumentException) {
            mConnectButton!!.visibility = View.VISIBLE
            Toast.makeText(
                    this
                    , getString(R.string.warning_client_id_redirect_uri_incorrect)
                    , Toast.LENGTH_LONG).show()
            progressDialog?.dismiss()
            resetUIForConnect()
            //            return;
            val sendMailIntent = Intent(this, MainActivity::class.java)
            startActivity(sendMailIntent)
            return
        }
        sendMailIntent = Intent(this, MainActivity::class.java)
        AuthenticationManager.getInstance().setContextActivity(this)
        AuthenticationManager.getInstance().connect(
                object : AuthenticationCallback<AuthenticationResult> {
                    /**
                     * If the connection is successful, the activity extracts the username and
                     * displayableId values from the authentication result object and sends them
                     * to the SendMail activity.
                     * @param result The authentication result object that contains information about
                     * the user and the tokens.
                     */
                    override fun onSuccess(result: AuthenticationResult) {
                        Log.i(TAG, "onConnectButtonClick - Successfully connected to Office 365")
                        sendMailIntent!!.putExtra("givenName", result
                                .userInfo
                                .givenName)
                        sendMailIntent!!.putExtra("displayableId", result
                                .userInfo
                                .displayableId)

                        t = result.userInfo.displayableId
                        progressDialog?.dismiss()
                        sendMailIntent!!.putExtra("givenName", result
                                .userInfo
                                .givenName)
                        sendMailIntent!!.putExtra("displayableId", result
                                .userInfo
                                .displayableId)
                        resetUIForConnect()
                        permisosobtener()

                    }

                    override fun onError(e: Exception) {
                        Log.e(TAG, "onCreate - " + e.message)
                        var tt=e.message
                        showConnectErrorUI(tt!!)
                    }
                })
    }

    /**
     * This activity gets notified about the completion of the ADAL activity through this method.
     * @param requestCode The integer request code originally supplied to startActivityForResult(),
     * allowing you to identify who this result came from.
     * @param resultCode The integer result code returned by the child activity through its
     * setResult().
     * @param data An Intent, which can return result data to the caller (various data
     * can be attached to Intent "extras").
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.i(TAG, "onActivityResult - AuthenticationActivity has come back with results")
        super.onActivityResult(requestCode, resultCode, data)
        AuthenticationManager
                .getInstance()
                .authenticationContext
                .onActivityResult(requestCode, resultCode, data)
    }

    private fun initializeViews() {
        mConnectButton = findViewById<View>(R.id.connectButton) as Button
        mConnectProgressBar = findViewById<View>(R.id.connectProgressBar) as ProgressBar
        mTitleTextView = findViewById<View>(R.id.titleTextView) as TextView
        mDescriptionTextView = findViewById<View>(R.id.descriptionTextView) as TextView
    }

    private fun resetUIForConnect() {
        mTitleTextView!!.visibility = View.GONE
        mDescriptionTextView!!.visibility = View.GONE
        mConnectProgressBar!!.visibility = View.GONE
    }

    private fun showConnectingInProgressUI() {
        mConnectButton!!.visibility = View.GONE
        mTitleTextView!!.visibility = View.GONE
        mDescriptionTextView!!.visibility = View.GONE
    }

    private fun showConnectErrorUI(e:String) {
        mConnectProgressBar!!.visibility = View.GONE
        mTitleTextView!!.setText(R.string.title_text_error)
progressDialog?.dismiss()
        connectButton.visibility=View.VISIBLE
        var ee=e
        Toast.makeText(
                this@ConnectActivity,
                 ee,
                Toast.LENGTH_LONG).show()
    }

    fun pedirPermisos() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.CAMERA)) {

            } else {
                ActivityCompat.requestPermissions(
                        this, arrayOf(Manifest.permission.CAMERA),
                        REQUEST_LOCATION)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.size == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(this, "Por favor es necesario aceptar los permisos para usar ANAHUAC INVENTARIOS", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    companion object {
        private const val TAG = "ConnectActivity"
    }

    fun permisosobtener() {


        val datos = JSONObject()
        try {
            datos.put("Correo", t)

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
                                var iduser = guias.getString("id")


                                val preferencias = this.getSharedPreferences("VARIABLES", Context.MODE_PRIVATE)

                                val editor = preferencias.edit()
                                val costos = guias.getJSONArray("listaPermisos")
                                editor.putString("iduser", iduser)
                                editor.putString("correo", t)

                                for (i in 0 until costos.length()) {

                                    val producto = costos.getJSONObject(i)
                                    var id = producto.getString("id")

                                    var codigoPermiso = producto.getString("codigoPermiso")
                                    if(codigoPermiso.equals("PCAR")){
                                        editor.putBoolean("PCAR", true)

                                    }


                                    if(codigoPermiso.equals("PRAR")){
                                        editor.putBoolean("PRAR", true)

                                    }

                                    if(codigoPermiso.equals("PSCL")){
                                        editor.putBoolean("PSCL", true)

                                    }
                                    if(codigoPermiso.equals("PCPR")){
                                        editor.putBoolean("PCPR", true)

                                    }
                                    if(codigoPermiso.equals("PRPR")){
                                        editor.putBoolean("PRPR", true)

                                    }
                                    if(codigoPermiso.equals("PSPR")){
                                        editor.putBoolean("PSPR", true)

                                    }
                                    if(codigoPermiso.equals("PSARE")){
                                        editor.putBoolean("PSARE", true)

                                    }
                                    if(codigoPermiso.equals("PCUB")){
                                        editor.putBoolean("PCUB", true)

                                    }
                                    if(codigoPermiso.equals("PRUB")){
                                        editor.putBoolean("PRUB", true)

                                    }
                                    if(codigoPermiso.equals("PSUB")){
                                        editor.putBoolean("PSUB", true)

                                    }
                                    if(codigoPermiso.equals("PCUM")){
                                        editor.putBoolean("PCUM", true)

                                    }
                                    if(codigoPermiso.equals("PRUM")){
                                        editor.putBoolean("PRUM", true)

                                    }
                                    if(codigoPermiso.equals("PSUM")){
                                        editor.putBoolean("PSUM", true)

                                    }
                                    if(codigoPermiso.equals("PCUS")){
                                        editor.putBoolean("PCUS", true)

                                    }
                                    if(codigoPermiso.equals("PSCC")){
                                        editor.putBoolean("PSCC", true)

                                    }



                                    if(codigoPermiso.equals("PRUS")){
                                        editor.putBoolean("PRUS", true)

                                    }
                                    if(codigoPermiso.equals("PSUS")){
                                        editor.putBoolean("PSUS", true)

                                    }
                                    if(codigoPermiso.equals("PRCC")){
                                        editor.putBoolean("PRCC", true)

                                    }
                                    if(codigoPermiso.equals("PCARE")){
                                        editor.putBoolean("PCARE", true)

                                    }
                                    if(codigoPermiso.equals("PCCC")){
                                        editor.putBoolean("PCCC", true)

                                    }



                                    if(codigoPermiso.equals("PLNO")){
                                        editor.putBoolean("PLNO", true)

                                    }
                                    if(codigoPermiso.equals("PRARE")){
                                        editor.putBoolean("PRARE", true)

                                    }
                                    if(codigoPermiso.equals("PLAR")){
                                        editor.putBoolean("PLAR", true)

                                    }
                                    if(codigoPermiso.equals("PCCL")){
                                        editor.putBoolean("PCCL", true)

                                    }
                                    if(codigoPermiso.equals("PRCL")){
                                        editor.putBoolean("PRCL", true)

                                    }


                                    if(codigoPermiso.equals("PLMA")){
                                        editor.putBoolean("PLMA", true)

                                    }
                                    if(codigoPermiso.equals("PCMA")){
                                        editor.putBoolean("PCMA", true)

                                    }
                                    if(codigoPermiso.equals("PRMA")){
                                        editor.putBoolean("PRMA", true)

                                    }
                                    if(codigoPermiso.equals("PSMA")){
                                        editor.putBoolean("PSMA", true)

                                    }
                                    if(codigoPermiso.equals("PLCL")){
                                        editor.putBoolean("PLCL", true)

                                    }



                                    if(codigoPermiso.equals("PLPR")){
                                        editor.putBoolean("PLPR", true)

                                    }
                                    if(codigoPermiso.equals("PLARE")){
                                        editor.putBoolean("PLARE", true)

                                    }
                                    if(codigoPermiso.equals("PLUB")){
                                        editor.putBoolean("PLUB", true)

                                    }
                                    if(codigoPermiso.equals("PLUM")){
                                        editor.putBoolean("PLUM", true)

                                    }
                                    if(codigoPermiso.equals("PLUS")) {
                                        editor.putBoolean("PLUS", true)

                                    }

                                    if(codigoPermiso.equals("PLCC")){
                                        editor.putBoolean("PLCC", true)

                                    }
                                }

                                editor.putString("escaneo", "true")

                                intent.putExtra("folio", "")
                                editor.commit()

                                startActivity(sendMailIntent)
                                finish()

                            } catch (es: Exception) {
                                Log.d("sergio1", "" + es.toString())
                                finish()
                                progressDialog?.dismiss()
                            }

                        } else {

                            // We need to make sure that there is no data stored with the failed auth
                            AuthenticationManager.getInstance().disconnect()
                            Toast.makeText(this, "No puedes iniciar sesion no tienes los permisos.", Toast.LENGTH_SHORT).show()
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