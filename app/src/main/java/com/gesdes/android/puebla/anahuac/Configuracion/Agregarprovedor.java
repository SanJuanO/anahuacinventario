package com.gesdes.android.puebla.anahuac.Configuracion;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gesdes.android.puebla.anahuac.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.prefs.Preferences;

public class Agregarprovedor extends AppCompatActivity {
    private String URL;
    private String Id;
    private String CP;
    private String Calle;
    private String Ciudad;
    private String Correo;
    private String Estado;
    private String Nombre;
    private String NombreContacto;
    private String Notas;
    private String RFC;
    private String Telefono;
    private String Web;
        private String Col;
    String  idbuscar;
     EditText calle;
     EditText nombre ;
     EditText ciudad ;
     EditText email ;
     EditText estado ;
     EditText notas ;
     EditText persona;
     EditText web;
     EditText telefono ;
     EditText rfc ;
     EditText cp;
    EditText colonia;
    Button guard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregarprovedor);
       // ImageButton im = (ImageButton) findViewById(R.id.atras);
        //im.setOnClickListener(new View.OnClickListener() {

    //        @Override
        //      public void onClick(View view) {
          //      finish();
            //}
       // });
        final TextView tit = (TextView) findViewById(R.id.tit);
        SharedPreferences preferences =getSharedPreferences("VARIABLES", Context.MODE_PRIVATE);

        boolean temp = preferences.getBoolean("PSPR", false);

        URL= getString(R.string.URL_API_PROVEDORES) + "addProveedor";


         calle = (EditText) findViewById(R.id.npcalle);
         nombre = (EditText) findViewById(R.id.npnombre);
         ciudad = (EditText) findViewById(R.id.npciudad);
        email = (EditText) findViewById(R.id.npemail);
        estado = (EditText) findViewById(R.id.npestado);
    notas = (EditText) findViewById(R.id.npnotas);
         persona = (EditText) findViewById(R.id.nppersona);
         web = (EditText) findViewById(R.id.npweb);
         telefono = (EditText) findViewById(R.id.nptelefono);
         rfc = (EditText) findViewById(R.id.nprfc);
         cp = (EditText) findViewById(R.id.ncap);
         colonia = (EditText) findViewById(R.id.colonia);


         guard = (Button) findViewById(R.id.guardarprovedor);
        Intent i = getIntent();

        idbuscar = i.getStringExtra("modificar");

        if (!idbuscar.equals("")) {
            if (temp) {
                guard.setText("Modificar");
            }

            else {
                tit.setText("Detalle del Proveedor");
                guard.setVisibility(View.INVISIBLE);
            }
            consultar();
        }
        guard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Calle = calle.getText().toString();
                Nombre = nombre.getText().toString();
                NombreContacto = persona.getText().toString();
                Correo = email.getText().toString();
                Estado = estado.getText().toString();
                Notas = notas.getText().toString();
                Ciudad = ciudad.getText().toString();
                Web = web.getText().toString();
                Telefono = telefono.getText().toString();
                RFC = rfc.getText().toString();
                CP = cp.getText().toString();
                Col = colonia.getText().toString();
                if (!idbuscar.equals("")) {

                        if (temp) {
                            modificar();
                        }
                    }
                else {
                    guardaDatos();
                }
            }
        });
    }
    public void modificar(){

        final ProgressDialog progressDialog = new ProgressDialog(this,
                R.style.Theme_AppCompat_Light_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Guardando datos...");
        progressDialog.show();
        int i=0;
        i=Integer.parseInt(idbuscar);

        JSONObject datos = new JSONObject();
        try {
            datos.put("Calle",Calle);
            datos.put("Id",i);
            datos.put("Nombre",Nombre);
            datos.put("NombreContacto",NombreContacto);
            datos.put("Correo",Correo);

            datos.put("Estado",Estado);
            datos.put("Notas",Notas);
            datos.put("Ciudad",Ciudad);
            datos.put("Web",Web);

            datos.put("Telefono",Telefono);
            datos.put("RFC",RFC);
            datos.put("CP",CP);
            datos.put("Colonia",Col);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestQueue requstQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://uap-inventarios-wa.azurewebsites.net/api/ProveedoresApi/saveProveedor",datos,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(progressDialog!=null){
                                progressDialog.dismiss();
                            }

                            int result = (int) response.get("resultado");

                            if(result == 1){


                                _ShowAlert("Bien","¡Datos actualizados correctamente!");

                            }else{

                                String error=response.getString("mensaje");
                                _ShowAlert("Error",error);

                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if(progressDialog!=null){
                            progressDialog.dismiss();
                        }
                        Log.e("Rest Response",error.toString());
                    }
                }
        ){
            //here I want to post data to sever
        };

        int MY_SOCKET_TIMEOUT_MS = 15000;
        int maxRetries = 2;
        jsonObjectRequest.setRetryPolicy(new
                        DefaultRetryPolicy(
                        MY_SOCKET_TIMEOUT_MS,
                        maxRetries,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );

        requstQueue.add(jsonObjectRequest);

    }
    public void guardaDatos(){

        final ProgressDialog progressDialog = new ProgressDialog(this,
                R.style.Theme_AppCompat_Light_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Guardando datos...");
        progressDialog.show();


        JSONObject datos = new JSONObject();
        try {
            datos.put("Calle",Calle);
            datos.put("Nombre",Nombre);
            datos.put("NombreContacto",NombreContacto);
            datos.put("Correo",Correo);

            datos.put("Estado",Estado);
            datos.put("Notas",Notas);
            datos.put("Ciudad",Ciudad);
            datos.put("Web",Web);

            datos.put("Telefono",Telefono);
            datos.put("RFC",RFC);
            datos.put("CP",CP);
            datos.put("Colonia",Col);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestQueue requstQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL,datos,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(progressDialog!=null){
                                progressDialog.dismiss();
                            }

                            int result = (int) response.get("resultado");

                            if(result == 1){


                                _ShowAlert("Bien","¡Datos actualizados correctamente!");

                            }else{

                                String error=response.getString("mensaje");
                                _ShowAlert("Error",error);

                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if(progressDialog!=null){
                            progressDialog.dismiss();
                        }
                        Log.e("Rest Response",error.toString());
                    }
                }
        ){
            //here I want to post data to sever
        };

        int MY_SOCKET_TIMEOUT_MS = 15000;
        int maxRetries = 2;
        jsonObjectRequest.setRetryPolicy(new
                        DefaultRetryPolicy(
                        MY_SOCKET_TIMEOUT_MS,
                        maxRetries,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );

        requstQueue.add(jsonObjectRequest);

    }
    private void _ShowAlert(String title, String mensaje){

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(mensaje);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();


                    }
                });
        alertDialog.show();
    }


    public void consultar(){

        final ProgressDialog progressDialog = new ProgressDialog(this,
                R.style.Theme_AppCompat_Light_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Guardando datos...");
        progressDialog.show();

 int i=0;
 i= Integer.parseInt(idbuscar);
        JSONObject datos = new JSONObject();
        try {
            datos.put("Id",i);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestQueue requstQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://uap-inventarios-wa.azurewebsites.net/api/ProveedoresApi/getProveedorById",datos,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(progressDialog!=null){
                                progressDialog.dismiss();
                            }

                            int result = (int) response.get("resultado");

                            if(result == 1){
                                JSONObject costos = response.getJSONObject("data");
                                calle.setText(costos.getString("calle"));
                                nombre.setText(costos.getString("nombre"));
                                ciudad.setText(costos.getString("ciudad"));
                                email.setText(costos.getString("correo"));
                                estado.setText(costos.getString("estado"));
                                notas.setText(costos.getString("notas"));
                                persona.setText(costos.getString("nombre"));
                                web.setText(costos.getString("web"));
                                telefono.setText(costos.getString("telefono"));
                                rfc.setText(costos.getString("rfc"));
                                cp.setText(costos.getString("cp"));
                                colonia.setText(costos.getString("colonia"));

                            }else{
                                if(progressDialog!=null){
                                    progressDialog.dismiss();
                                }


                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if(progressDialog!=null){
                            progressDialog.dismiss();
                        }
                        Log.e("Rest Response",error.toString());
                    }
                }
        ){
            //here I want to post data to sever
        };

        int MY_SOCKET_TIMEOUT_MS = 15000;
        int maxRetries = 2;
        jsonObjectRequest.setRetryPolicy(new
                        DefaultRetryPolicy(
                        MY_SOCKET_TIMEOUT_MS,
                        maxRetries,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );

        requstQueue.add(jsonObjectRequest);

    }
}
