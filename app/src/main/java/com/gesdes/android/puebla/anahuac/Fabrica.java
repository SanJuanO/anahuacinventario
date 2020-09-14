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

public class Fabrica extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ArrayAdapter adapter;
    private Boolean toque=false;
    private String URL;
    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> ids = new ArrayList<>();
int t = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fabricas);
        final ListView list = (ListView) findViewById(R.id.theList);
    EditText theFilter = (EditText) findViewById(R.id.searchFilter);
    TextView titulo = (TextView) findViewById(R.id.tituloscategoria);
    EditText search = (EditText) findViewById(R.id.searchFilter);
    ImageButton agregar = (ImageButton) findViewById(R.id.agregarn);
        SharedPreferences preferences =getSharedPreferences("VARIABLES", Context.MODE_PRIVATE);

    Intent intent = new Intent(this, Departamentos.class );

        Intent intent2 = new Intent(this, Agregarubicacion.class );
        Intent clases = new Intent(this, Categorias.class );
        Intent proveedor = new Intent(this, Agregarprovedor.class );
        Intent costos = new Intent(this, Agregarcentro.class );
        Intent unidad = new Intent(this, Agregarmedida.class );
        t = getIntent().getIntExtra("donde",0);


    Intent i = new Intent(this, Agregarprovedor.class );

        if(names.isEmpty()){

        String config = getIntent().getStringExtra("config");
        if(config.equals("fabrica")){
            URL= getString(R.string.URL_API_PROVEDORES) + "getProveedores";
            fabricante();
            boolean temp= preferences.getBoolean("PCPR",false);

            if(!temp){
                agregar.setVisibility(View.INVISIBLE);
            }
            i = new Intent(this, Agregarprovedor.class );
            titulo.setText("Provedores");
        }
        else if(config.equals("ubicacion")) {
            URL= getString(R.string.URL_API_UBICACION) + "getUbicaciones";
            ubicacion();
            i = new Intent(this, Agregarubicacion.class );
            boolean temp= preferences.getBoolean("PCUB",false);

            if(!temp){
                agregar.setVisibility(View.INVISIBLE);
            }
            titulo.setText("Ubicacion");

        }
        else if(config.equals("vendedor")){
            i = new Intent(this, Agregarprovedor.class );

            titulo.setText("Vendedores");

        }
        else if(config.equals("categoria")){
            i = new Intent(this, Categorias.class );
            URL= getString(R.string.URL_API_CLASES) + "getClases";
            clases();
            titulo.setText("Clases");
            boolean temp= preferences.getBoolean("PCCL",false);

            if(!temp){
                agregar.setVisibility(View.INVISIBLE);
            }
        }
        else if(config.equals("unidad")){
            i = new Intent(this, Agregarmedida.class );
            URL= getString(R.string.URL_API_UNIDADES) + "getUnidadesMedidas";
            unidades();
            titulo.setText("Unidades Medidas");
            boolean temp= preferences.getBoolean("PCUM",false);

            if(!temp){
                agregar.setVisibility(View.INVISIBLE);
            }
        }
        else if(config.equals("centro")){
            URL= getString(R.string.URL_API_CENTRO) + "getCentrosCostos";
            centro();
            i = new Intent(this, Agregarcentro.class );

            titulo.setText("Centro de costos");
            boolean temp= preferences.getBoolean("PCCC",false);

            if(!temp){
                agregar.setVisibility(View.INVISIBLE);
            }
        }


    }


    final Intent finalI = i;
        agregar.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View view) {

                finalI.putExtra("modificar", "");

                startActivityForResult(finalI, 80);

        }
    });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position,
        long arg3) {
            if(toque==false) {
                toque=true;
            // TODO Auto-generated method stub
            String config = getIntent().getStringExtra("config");
            String i = "";
            TextView c = (TextView) arg1; //<--this one
            String text = c.getText().toString();
for(int q=0;q<names.size();q++){
    if(names.get(q).equals(text)){
      i=ids.get(q);

            if(config.equals("ubicacion")){





                if(t !=0){
                    intent2.putExtra("modificar", i);
                    startActivityForResult(intent2, 88);
                }
                else {
                        intent.putExtra("ubicaciondepartamento", i);
                    startActivityForResult(intent, 14);
                }
            }else {
                String po = ids.get(position);


                if(t !=0){

                    if(config.equals("fabrica")){
                        proveedor.putExtra("modificar", i);
                        startActivityForResult(proveedor, 88);

                    }


                    else if(config.equals("categoria")){
                        clases.putExtra("modificar", i);
                        startActivityForResult(clases, 88);

                    }
                    else if(config.equals("unidad")){
                        unidad.putExtra("modificar", i);
                        startActivityForResult(unidad, 88);

                    }
                    else if(config.equals("centro")){
                        costos.putExtra("modificar", i);
                        startActivityForResult(costos, 88);

                    }





                }
else {
                    String cursor = (String) list.getItemAtPosition(position);
                    SharedPreferences preferencias = getSharedPreferences("VARIABLES", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferencias.edit();

                    editor.putString("PROVEDORNID", i);

                    editor.putString("PROVEDORNNAME", text);


                    editor.commit();


                    finish();
                    Log.d(cursor, "");
                }
            }
        }
}}
        }
    });        theFilter.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            (Fabrica.this).adapter.getFilter().filter(charSequence);


        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    });

}
    public void clases() {


        RequestQueue requstQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            int result = (int) response.get("resultado");

                            if(result == 1){
                                ids.clear();
                                names.clear();
                                JSONArray costos=response.getJSONArray("data");
                                for (int i=0;i<costos.length();i++) {
                                    JSONObject producto= costos.getJSONObject(i);
                                    ids.add(producto.getString("id"));
                                    String tt=producto.getString("clase");
                                    String tt2=producto.getString("descripcion");
                                    names.add(tt+"  "+tt2);

                                }




                                listo();
                            }else{

                                String error=response.getString("mensaje");
                                String config = getIntent().getStringExtra("config");
                                TextView texto = (TextView) findViewById(R.id.textonodisponible);

                                if(config.equals("fabrica")){

                                    texto.setText("No existen provedores aun.");
                                }
                                else if(config.equals("ubicacion")) {

                                    texto.setText("No existen ubicaciones aun.");

                                }
                                else if(config.equals("vendedor")){

                                    texto.setText("No existen vendedores aun.");

                                }
                                else if(config.equals("categoria")){

                                    texto.setText("No existen categorias aun.");

                                }
                                else if(config.equals("ordenes")){

                                    texto.setText("No existen ordenes aun.");

                                }
                                else if(config.equals("reporte")){

                                    texto.setText("No existen reportes aun.");

                                }
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                            String config = getIntent().getStringExtra("config");
                            TextView texto = (TextView) findViewById(R.id.textonodisponible);

                            if(config.equals("fabrica")){

                                texto.setText("No existen provedores aun.");
                            }
                            else if(config.equals("ubicacion")) {

                                texto.setText("No existen ubicaciones aun.");

                            }
                            else if(config.equals("vendedor")){

                                texto.setText("No existen vendedores aun.");

                            }
                            else if(config.equals("categoria")){

                                texto.setText("No existen categorias aun.");

                            }
                            else if(config.equals("ordenes")){

                                texto.setText("No existen ordenes aun.");

                            }
                            else if(config.equals("reporte")){

                                texto.setText("No existen reportes aun.");

                            }

                        }

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String config = getIntent().getStringExtra("config");
                        TextView texto = (TextView) findViewById(R.id.textonodisponible);

                        if(config.equals("fabrica")){

                            texto.setText("No existen provedores aun.");
                        }
                        else if(config.equals("ubicacion")) {

                            texto.setText("No existen ubicaciones aun.");

                        }
                        else if(config.equals("vendedor")){

                            texto.setText("No existen vendedores aun.");

                        }
                        else if(config.equals("categoria")){

                            texto.setText("No existen categorias aun.");

                        }
                        else if(config.equals("ordenes")){

                            texto.setText("No existen ordenes aun.");

                        }
                        else if(config.equals("reporte")){

                            texto.setText("No existen reportes aun.");

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
ids.clear();
names.clear();
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

                                if(config.equals("fabrica")){

                                    texto.setText("No existen provedores aun.");
                                }
                                else if(config.equals("ubicacion")) {

                                    texto.setText("No existen ubicaciones aun.");

                                }
                                else if(config.equals("vendedor")){

                                    texto.setText("No existen vendedores aun.");

                                }
                                else if(config.equals("categoria")){

                                    texto.setText("No existen categorias aun.");

                                }
                                else if(config.equals("ordenes")){

                                    texto.setText("No existen ordenes aun.");

                                }
                                else if(config.equals("reporte")){

                                    texto.setText("No existen reportes aun.");

                                }
                            }

                        }catch (JSONException e){
                            if(progressDialog!=null){
                                progressDialog.dismiss();
                            }
                            e.printStackTrace();
                            String config = getIntent().getStringExtra("config");
                            TextView texto = (TextView) findViewById(R.id.textonodisponible);

                            if(config.equals("fabrica")){

                                texto.setText("No existen provedores aun.");
                            }
                            else if(config.equals("ubicacion")) {

                                texto.setText("No existen ubicaciones aun.");

                            }
                            else if(config.equals("vendedor")){

                                texto.setText("No existen vendedores aun.");

                            }
                            else if(config.equals("categoria")){

                                texto.setText("No existen categorias aun.");

                            }
                            else if(config.equals("ordenes")){

                                texto.setText("No existen ordenes aun.");

                            }
                            else if(config.equals("reporte")){

                                texto.setText("No existen reportes aun.");

                            }

                        }

                    }

                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String config = getIntent().getStringExtra("config");
                        TextView texto = (TextView) findViewById(R.id.textonodisponible);

                        if(config.equals("fabrica")){

                            texto.setText("No existen provedores aun.");
                        }
                        else if(config.equals("ubicacion")) {

                            texto.setText("No existen ubicaciones aun.");

                        }
                        else if(config.equals("vendedor")){

                            texto.setText("No existen vendedores aun.");

                        }
                        else if(config.equals("categoria")){

                            texto.setText("No existen categorias aun.");

                        }
                        else if(config.equals("ordenes")){

                            texto.setText("No existen ordenes aun.");

                        }
                        else if(config.equals("reporte")){

                            texto.setText("No existen reportes aun.");

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
    private void listo(){
        ListView list = (ListView) findViewById(R.id.theList);

        adapter = new ArrayAdapter(this, R.layout.list_item_layout_j, names);
        list.setAdapter(adapter);
        if(names.isEmpty()){
            String config = getIntent().getStringExtra("config");
            TextView texto = (TextView) findViewById(R.id.textonodisponible);

            if(config.equals("fabrica")){

                texto.setText("No existen provedores aun.");
            }
            else if(config.equals("ubicacion")) {

                texto.setText("No existen ubicaciones aun.");

            }
            else if(config.equals("vendedor")){

                texto.setText("No existen vendedores aun.");

            }
            else if(config.equals("categoria")){

                texto.setText("No existen categorias aun.");

            }
            else if(config.equals("ordenes")){

                texto.setText("No existen ordenes aun.");

            }
            else if(config.equals("reporte")){

                texto.setText("No existen reportes aun.");

            }
        }
    }
    public void unidades() {
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
                                ids.clear();
                                names.clear();
                                JSONArray costos=response.getJSONArray("data");
                                for (int i=0;i<costos.length();i++) {
                                    JSONObject producto= costos.getJSONObject(i);
                                    ids.add(producto.getString("id"));
                                    names.add(producto.getString("unidadMedida"));

                                }




                                listo();
                            }else{

                                String error=response.getString("mensaje");
                                String config = getIntent().getStringExtra("config");
                                TextView texto = (TextView) findViewById(R.id.textonodisponible);

                                if(config.equals("fabrica")){

                                    texto.setText("No existen provedores aun.");
                                }
                                else if(config.equals("ubicacion")) {

                                    texto.setText("No existen ubicaciones aun.");

                                }
                                else if(config.equals("vendedor")){

                                    texto.setText("No existen vendedores aun.");

                                }
                                else if(config.equals("categoria")){

                                    texto.setText("No existen categorias aun.");

                                }
                                else if(config.equals("ordenes")){

                                    texto.setText("No existen ordenes aun.");

                                }
                                else if(config.equals("reporte")){

                                    texto.setText("No existen reportes aun.");

                                }
                            }

                        }catch (JSONException e){
                            e.printStackTrace();

                            String config = getIntent().getStringExtra("config");
                            TextView texto = (TextView) findViewById(R.id.textonodisponible);

                            if(config.equals("fabrica")){

                                texto.setText("No existen provedores aun.");
                            }
                            else if(config.equals("ubicacion")) {

                                texto.setText("No existen ubicaciones aun.");

                            }
                            else if(config.equals("vendedor")){

                                texto.setText("No existen vendedores aun.");

                            }
                            else if(config.equals("categoria")){

                                texto.setText("No existen categorias aun.");

                            }
                            else if(config.equals("ordenes")){

                                texto.setText("No existen ordenes aun.");

                            }
                            else if(config.equals("reporte")){

                                texto.setText("No existen reportes aun.");

                            }

                        }

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String config = getIntent().getStringExtra("config");
                        TextView texto = (TextView) findViewById(R.id.textonodisponible);

                        if(config.equals("fabrica")){

                            texto.setText("No existen provedores aun.");
                        }
                        else if(config.equals("ubicacion")) {

                            texto.setText("No existen ubicaciones aun.");

                        }
                        else if(config.equals("vendedor")){

                            texto.setText("No existen vendedores aun.");

                        }
                        else if(config.equals("categoria")){

                            texto.setText("No existen categorias aun.");

                        }
                        else if(config.equals("ordenes")){

                            texto.setText("No existen ordenes aun.");

                        }
                        else if(config.equals("reporte")){

                            texto.setText("No existen reportes aun.");

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
    public void centro() {
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
                                ids.clear();
                                names.clear();
                                JSONArray costos=response.getJSONArray("data");
                                for (int i=0;i<costos.length();i++) {
                                    JSONObject producto= costos.getJSONObject(i);
                                    ids.add(producto.getString("id"));
                                    names.add(producto.getString("centroCostos"));

                                }




                                listo();
                            }else{

                                String error=response.getString("mensaje");
                                String config = getIntent().getStringExtra("config");
                                TextView texto = (TextView) findViewById(R.id.textonodisponible);

                                if(config.equals("fabrica")){

                                    texto.setText("No existen provedores aun.");
                                }
                                else if(config.equals("ubicacion")) {

                                    texto.setText("No existen ubicaciones aun.");

                                }
                                else if(config.equals("vendedor")){

                                    texto.setText("No existen vendedores aun.");

                                }
                                else if(config.equals("categoria")){

                                    texto.setText("No existen categorias aun.");

                                }
                                else if(config.equals("ordenes")){

                                    texto.setText("No existen ordenes aun.");

                                }
                                else if(config.equals("reporte")){

                                    texto.setText("No existen reportes aun.");

                                }
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                            if(progressDialog!=null){
                                progressDialog.dismiss();
                            }
                            String config = getIntent().getStringExtra("config");
                            TextView texto = (TextView) findViewById(R.id.textonodisponible);

                            if(config.equals("fabrica")){

                                texto.setText("No existen provedores aun.");
                            }
                            else if(config.equals("ubicacion")) {

                                texto.setText("No existen ubicaciones aun.");

                            }
                            else if(config.equals("vendedor")){

                                texto.setText("No existen vendedores aun.");

                            }
                            else if(config.equals("categoria")){

                                texto.setText("No existen categorias aun.");

                            }
                            else if(config.equals("ordenes")){

                                texto.setText("No existen ordenes aun.");

                            }
                            else if(config.equals("reporte")){

                                texto.setText("No existen reportes aun.");

                            }

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
    public void ubicacion() {
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
                                ids.clear();
                                names.clear();
                                JSONArray costos=response.getJSONArray("data");
                                for (int i=0;i<costos.length();i++) {
                                    JSONObject producto= costos.getJSONObject(i);
                                    ids.add(producto.getString("id"));
                                    names.add(producto.getString("ubicacion"));

                                }


listo();
                            }else{

                                String error=response.getString("mensaje");
                                String config = getIntent().getStringExtra("config");
                                TextView texto = (TextView) findViewById(R.id.textonodisponible);

                                if(config.equals("fabrica")){

                                    texto.setText("No existen provedores aun.");
                                }
                                else if(config.equals("ubicacion")) {

                                    texto.setText("No existen ubicaciones aun.");

                                }
                                else if(config.equals("vendedor")){

                                    texto.setText("No existen vendedores aun.");

                                }
                                else if(config.equals("categoria")){

                                    texto.setText("No existen categorias aun.");

                                }
                                else if(config.equals("ordenes")){

                                    texto.setText("No existen ordenes aun.");

                                }
                                else if(config.equals("reporte")){

                                    texto.setText("No existen reportes aun.");

                                }
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                            if(progressDialog!=null){
                                progressDialog.dismiss();
                            }
                            String config = getIntent().getStringExtra("config");
                            TextView texto = (TextView) findViewById(R.id.textonodisponible);

                            if(config.equals("fabrica")){

                                texto.setText("No existen provedores aun.");
                            }
                            else if(config.equals("ubicacion")) {

                                texto.setText("No existen ubicaciones aun.");

                            }
                            else if(config.equals("vendedor")){

                                texto.setText("No existen vendedores aun.");

                            }
                            else if(config.equals("categoria")){

                                texto.setText("No existen clases aun.");

                            }
                            else if(config.equals("ordenes")){

                                texto.setText("No existen ordenes aun.");

                            }
                            else if(config.equals("reporte")){

                                texto.setText("No existen reportes aun.");

                            }

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




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        toque=false;
        if(requestCode == 14) {

            finish();
        }
     else{

                String config = getIntent().getStringExtra("config");
                if(config.equals("fabrica")){
                    URL= getString(R.string.URL_API_PROVEDORES) + "getProveedores";
                    fabricante();

                }
                else if(config.equals("ubicacion")) {
                    URL= getString(R.string.URL_API_UBICACION) + "getUbicaciones";
                    ubicacion();


                }
                else if(config.equals("vendedor")){


                }
                else if(config.equals("categoria")){
                    URL= getString(R.string.URL_API_CLASES) + "getClases";
                    clases();

                }
                else if(config.equals("unidad")){
                    URL= getString(R.string.URL_API_UNIDADES) + "getUnidadesMedidas";
                    unidades();

                }
                else if(config.equals("centro")){
                    URL= getString(R.string.URL_API_CENTRO) + "getCentrosCostos";
                    centro();


                }




        }




    }


    private void departamento(){


    }




    }
