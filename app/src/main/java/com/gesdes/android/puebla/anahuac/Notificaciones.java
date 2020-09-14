package com.gesdes.android.puebla.anahuac;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gesdes.android.puebla.anahuac.Configuracion.Agregarprovedor;
import com.gesdes.android.puebla.anahuac.Model.Planet;
import com.gesdes.android.puebla.anahuac.adapter.CardAdapter;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Notificaciones extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CardAdapter adapter;
    private ArrayList<Planet> planetArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);
        initView();

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle("Recycler View with Card View");
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        planetArrayList = new ArrayList<>();
        adapter = new CardAdapter(this, planetArrayList);
        recyclerView.setAdapter(adapter);
        // recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        obtenernotificaciones();
    }

    private void createListData() {
        Planet planet = new Planet("Silla", "EDIFICIO 1/SALA", "43242342", "El articulo no a llegado dfdsfds sdfdsfdsf sdfdsfds dsfdsfds sdfds", "12 junio 2020", "#FF0000", "ddasfds");
        planetArrayList.add(planet);

        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void obtenernotificaciones() {
        final ProgressDialog progressDialog = new ProgressDialog(this,
                R.style.Theme_AppCompat_Light_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Cargando datos...");
        progressDialog.show();
        JSONObject datos =new JSONObject();
        SharedPreferences preferences =getSharedPreferences("VARIABLES", Context.MODE_PRIVATE);
        String temp= preferences.getString("correo","");
        try {
            datos.put("Correo", temp);


        } catch ( JSONException e) {
            e.printStackTrace();
        }
        RequestQueue requstQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://uap-inventarios-wa.azurewebsites.net/api/NotificacionesApi/getNotificacionByCorreo",datos,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if(progressDialog!=null){
                                progressDialog.dismiss();
                            }
                            int result = (int) response.get("resultado");

                            if(result == 1){
                                String im="";

                                JSONObject costos = response.getJSONObject("data");
                                    JSONArray producto = costos.getJSONArray("lista");
                                    for (int a=0; a <= producto.length()-1;a++) {
                                        JSONObject not = producto.getJSONObject(a);


                                            //im = not.getString("imagen");



                                        JSONObject artic = not.getJSONObject("articulo");
try {
    JSONArray IMM = artic.getJSONArray("imagenes");


    for (int k = 0; k < 1; k++) {

        JSONObject imag = IMM.getJSONObject(k);
        im = imag.getString("imagen");


    }

}catch (JSONException e){

}

                                        Planet planet = new Planet(artic.getString("nombre"), artic.getString("ubicacion") + "/" + artic.getString("area"),  artic.getString("codigo"), not.getString("mensaje"), not.getString("fechaEnvio"), "#FF0000", im);

                                        planetArrayList.add(planet);
                                        adapter.notifyDataSetChanged();


                                }





                            }else{
                                if(progressDialog!=null){
                                    progressDialog.dismiss();
                                }

                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                            if(progressDialog!=null){
                                progressDialog.dismiss();
                            }
                            String config = getIntent().getStringExtra("config");
                            TextView texto = (TextView) findViewById(R.id.textonodisponible);



                        }

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {

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