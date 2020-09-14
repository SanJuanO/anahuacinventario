package com.gesdes.android.puebla.anahuac.Configuracion;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;

public class Departamentos extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ArrayAdapter adapter;
    private String URL;
    private String ubicaciondepartamento;

    ArrayList<String> nombreubicacion = new ArrayList<>();
    ArrayList<String> idsubicacion = new ArrayList<>();
    ArrayList<String> nombrearea = new ArrayList<>();
    ArrayList<String> idarea = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fabricas);
        final ListView list = (ListView) findViewById(R.id.theList);
        EditText theFilter = (EditText) findViewById(R.id.searchFilter);
        TextView titulo = (TextView) findViewById(R.id.tituloscategoria);
        EditText search = (EditText) findViewById(R.id.searchFilter);
        ImageButton agregar = (ImageButton) findViewById(R.id.agregarn);
        URL= getString(R.string.URL_API_UBICACION) + "getUbicacionById";
         ubicaciondepartamento =getIntent().getStringExtra("ubicaciondepartamento");


        titulo.setText("Areas");
        agregar.setVisibility(View.INVISIBLE);

        guardaDatos();

        Intent i = new Intent(this, Agregarprovedor.class );
        Intent intent = new Intent(this, Subareasagregar.class );


        final Intent finalI = i;
        agregar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivityForResult(finalI, 80);

            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                // TODO Auto-generated method stub


                SharedPreferences preferencias = getSharedPreferences("VARIABLES", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferencias.edit();
                String ia = idarea.get(position);
                String a = nombrearea.get(position);
                String iu = idsubicacion.get(position);
                String u = nombreubicacion.get(position);
                String ua = u+"/"+a;


                editor.putString("SELECTUBICACION", u);
                editor.putString("SELECTUBICACIONID",iu);

                editor.putString("SELECTAREA", a);
                editor.putString("SELECTAREAID", ia);
                editor.putString("UBICACIONAREA", ua);


                editor.commit();

                intent.putExtra("ubicaciondepartamento", ia);
                startActivityForResult(intent, 14);


            }
        });        theFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                (Departamentos.this).adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void listo(){
        ListView list = (ListView) findViewById(R.id.theList);

        adapter = new ArrayAdapter(this, R.layout.list_item_layout_j, nombrearea);
        list.setAdapter(adapter);


    }


    public void guardaDatos(){

        final ProgressDialog progressDialog = new ProgressDialog(this,
                R.style.Theme_AppCompat_Light_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Guardando datos...");
        progressDialog.show();

        int numEntero = Integer.parseInt(ubicaciondepartamento);

        JSONObject datos = new JSONObject();
        try {
            datos.put("Id",numEntero);




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

                                JSONObject categorias=response.getJSONObject("data");

                                JSONArray categoria= categorias.getJSONArray("listaAreas");
                                for (int i=0;i<categoria.length();i++) {

                                    JSONObject pk =categoria.getJSONObject(i);
                                     idarea.add(pk.getString("idArea"));
                                    idsubicacion.add(pk.getString("idUbicacion"));
                                    nombrearea.add(pk.getString("area"));
                                    nombreubicacion.add(pk.getString("ubicacion"));

                                }
listo();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 14) {

finish();




        }




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
