package com.gesdes.android.puebla.anahuac;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.gesdes.android.puebla.anahuac.Configuracion.Agregarcentro;
import com.gesdes.android.puebla.anahuac.Configuracion.Agregarmedida;
import com.gesdes.android.puebla.anahuac.Configuracion.Agregarprovedor;
import com.gesdes.android.puebla.anahuac.Configuracion.Agregarubicacion;
import com.gesdes.android.puebla.anahuac.Configuracion.Categorias;
import com.gesdes.android.puebla.anahuac.Configuracion.Departamentos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Encargados extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ArrayAdapter adapter;
    private String URL;
    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> ids = new ArrayList<>();
    EditText encargado,areas;
    ImageButton btnencargado,btnareas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encargados);
        final ListView list = (ListView) findViewById(R.id.theList);
    EditText theFilter = (EditText) findViewById(R.id.searchFilter);
    EditText search = (EditText) findViewById(R.id.searchFilter);


        Intent intent = new Intent(this, Encargados.class);
        Intent are = new Intent(this, Encargados.class);


    Intent i = new Intent(this, Agregarprovedor.class );
        URL=  "https://uap-inventarios-wa.azurewebsites.net/api/UsuariosApi/getUsuariosEncargadosApp";
        fabricante();
        btnencargado=(ImageButton) findViewById(R.id.encargados);
        encargado=(EditText) findViewById(R.id.eencargados);
        btnareas=(ImageButton) findViewById(R.id.areas);
        areas=(EditText) findViewById(R.id.eareas);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position,
        long arg3) {
            // TODO Auto-generated method stub
            String config = getIntent().getStringExtra("config");
            String i = ids.get(position);
            SharedPreferences preferencias = getSharedPreferences("VARIABLES", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferencias.edit();


            String po = names.get(position);
            editor.putString("idencargado", i);

            editor.putString("endcargado", po);


            editor.commit();
finish();


        }
    });        theFilter.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            (Encargados.this).adapter.getFilter().filter(charSequence);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    });

}
    public void fabricante() {
        final ProgressDialog progressDialog = new ProgressDialog(this,
                R.style.Theme_AppCompat_Light_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Cargando datos...");
        progressDialog.show();

        RequestQueue requstQueue = Volley.newRequestQueue(this);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if(progressDialog!=null){
                                progressDialog.dismiss();
                            }
                            int result = (int) response.get("resultado");

                            if(result == 1){
                                JSONArray costos=response.getJSONArray("data");
                                for (int i=0;i<costos.length();i++) {
                                    JSONObject producto= costos.getJSONObject(i);
                                    ids.add(producto.getString("id"));
                                    names.add(producto.getString("nombre"));

                                }




listo();
                            }else{

                                String error=response.getString("mensaje");
                                String config = getIntent().getStringExtra("config");
                                TextView texto = (TextView) findViewById(R.id.textonodisponible);



                            }

                        }catch (JSONException e){
                            if(progressDialog!=null){
                                progressDialog.dismiss();
                            }
                            e.printStackTrace();
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
    private void listo(){
        ListView list = (ListView) findViewById(R.id.theList);

        adapter = new ArrayAdapter(this, R.layout.list_item_layout_j, names);
        list.setAdapter(adapter);

    }





    }
