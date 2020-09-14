package com.gesdes.android.puebla.anahuac;

import android.annotation.SuppressLint;
import android.app.IntentService;
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
import com.gesdes.android.puebla.anahuac.Configuracion.Agregarareas;
import com.gesdes.android.puebla.anahuac.Configuracion.Agregarprovedor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.prefs.Preferences;

public class Areas extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ArrayAdapter adapter;
    private String URL;
    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> ids = new ArrayList<>();
    EditText encargado,areas;
    ImageButton btnencargado,btnareas;
String donde="";
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_areas);
        final ListView list = (ListView) findViewById(R.id.theList);
    EditText theFilter = (EditText) findViewById(R.id.searchFilter);
    EditText search = (EditText) findViewById(R.id.searchFilter);
        SharedPreferences preferencias = getSharedPreferences("VARIABLES", Context.MODE_PRIVATE);
        final  Intent go = new Intent(this, Agregarareas.class);
        Intent i = getIntent();

         donde = i.getStringExtra("donde");



    Intent ii = new Intent(this, Agregarareas.class );
        URL=  "https://uap-inventarios-wa.azurewebsites.net/api/AreasApi/getAreas";
        fabricante();
        btnencargado=(ImageButton) findViewById(R.id.encargados);
        encargado=(EditText) findViewById(R.id.eencargados);
        btnareas=(ImageButton) findViewById(R.id.areas);
        areas=(EditText) findViewById(R.id.eareas);
        ImageButton agregar=(ImageButton) findViewById(R.id.agregarn);

        agregar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ii.putExtra("modificar", "");

                startActivityForResult(ii, 80);

            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position,
        long arg3) {

            // TODO Auto-generated method stub
            String i = "";
            TextView c = (TextView) arg1; //<--this one
            String text = c.getText().toString();
            for(int q=0;q<names.size();q++) {
                if (names.get(q).equals(text)) {
                    i = ids.get(q);
                }
            }
            String po = text;
            SharedPreferences preferencias = getSharedPreferences("VARIABLES", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferencias.edit();
            if(donde!=null) {
                if (donde.equals("config")) {
                    go.putExtra("modificar", i);


                    startActivity(go);
                } else {


                    editor.putString("idareas", i);

                    editor.putString("areas", po);


                    editor.commit();
                    finish();
                }
            }else {


                editor.putString("idareas", i);

                editor.putString("areas", po);


                editor.commit();
                finish();
            }

        }
    });        theFilter.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            (Areas.this).adapter.getFilter().filter(charSequence);
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
                                    names.add(producto.getString("area"));

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


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 80) {
            ids.clear();
            names.clear();
            fabricante();

        }
    }


    }
