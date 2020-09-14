package com.gesdes.android.puebla.anahuac;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gesdes.android.puebla.anahuac.Model.Planet;
import com.gesdes.android.puebla.anahuac.adapter.CardAdapter_mantenimiento;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Mantenimiento extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CardAdapter_mantenimiento adapter;
    private ArrayList<Planet> planetArrayList;
    EditText texto;
String id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mantenimiento);
        Intent i = getIntent();
        texto=findViewById(R.id.motivo);
        Button btn=findViewById(R.id.btnagregar);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                agregar();

            }
        });

        id = i.getStringExtra("id");
        initView();

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle("Recycler View with Card View");
    }
    private void mantenimiento() {
    agregar();
    }
    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        planetArrayList = new ArrayList<>();
        adapter = new CardAdapter_mantenimiento(this, planetArrayList);
        recyclerView.setAdapter(adapter);
        // recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        obtenernotificaciones();
     //   Planet planet = new Planet("Mantenimiento", "21-05-20",  "122321433","Cambio de tintas", "", "#FF0000", "");

      //  planetArrayList.add(planet);
       // adapter.notifyDataSetChanged();

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
        int i = 0;
        i= Integer.parseInt(id);
        try {
            datos.put("IdArticulo", i);


        } catch ( JSONException e) {
            e.printStackTrace();
        }
        RequestQueue requstQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://uap-inventarios-wa.azurewebsites.net/api/MantenimientoApi/getMantenimientosByIdArticulo",datos,
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

                               // JSONObject costos = response.getJSONObject("data");
                                    JSONArray producto = response.getJSONArray("data");
                                    for (int a=0; a <= producto.length()-1;a++) {
                                        JSONObject artic = producto.getJSONObject(a);


                                                Planet planet = new Planet(artic.getString("articulo"), artic.getString("usuario"), artic.getString("codigo"),artic.getString("descripcion") , artic.getString("fechaMantenimientoFormato"), "#FF0000", im);

                                                planetArrayList.add(planet);





                                    }
                                adapter.notifyDataSetChanged();

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


    public void agregar() {
        final ProgressDialog progressDialog = new ProgressDialog(this,
                R.style.Theme_AppCompat_Light_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Cargando datos...");
        progressDialog.show();
        JSONObject datos =new JSONObject();
        SharedPreferences preferences =getSharedPreferences("VARIABLES", Context.MODE_PRIVATE);
        String temp= preferences.getString("correo","");
        int i = 0;
        i= Integer.parseInt(id);
        String text= texto.getText().toString();
        try {
            datos.put("IdArticulo", i);
            datos.put("Correo", temp);
            datos.put("Descripcion", text);


        } catch ( JSONException e) {
            e.printStackTrace();
        }
        RequestQueue requstQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://uap-inventarios-wa.azurewebsites.net/api/MantenimientoApi/addMantenimientoMovil",datos,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            int result = (int) response.get("resultado");

                            if(result == 1){

                                _ShowAlert("Bien","¡Datos actualizados correctamente!");

                            }else{

                                String error=response.getString("mensaje");
                                _ShowAlert("Faltan datos",error);

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




}