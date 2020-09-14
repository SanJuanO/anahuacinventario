package com.gesdes.android.puebla.anahuac;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gesdes.android.puebla.anahuac.Utilidades.ConexionSQLiteHelper;
import com.gesdes.android.puebla.anahuac.Utilidades.Item;
import com.gesdes.android.puebla.anahuac.Utilidades.Utilidades;
import com.gesdes.android.puebla.anahuac.adapter.HomeAdapter;
import com.google.android.gms.common.util.IOUtils;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class Fotoadd extends AppCompatActivity implements HomeAdapter.ItemListener{
    private RecyclerView recyclerView;
    private ArrayList<Item> arrayList;
    private Set<String> imgenesarray;
    ArrayList<String> imagenes;
    String id;

    private int i =0;
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    ConexionSQLiteHelper conn;
    ArrayList<String> imageUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fotografias);
        ImageButton btnagregar = (ImageButton) findViewById(R.id.agregarimagen);
        Button btnim = (Button) findViewById(R.id.btnacep);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView11);
        Intent i = getIntent();
        imageUrls = i.getStringArrayListExtra("imagenes");
        id = i.getStringExtra("id");
        btnim.setText("Guardar");
        arrayList = new ArrayList<>();

         consultarSql();
        btnim.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    guardaDatos();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        btnagregar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


tomarFoto();

            }
        });
        /**
         AutoFitGridLayoutManager that auto fits the cells by the column width defined.
         **/

        //AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(this, 500);
        //recyclerView.setLayoutManager(layoutManager);


        /**
         Simple GridLayoutManager that spans two columns
         **/
        GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
    }

    @Override
    public void onItemClick(Item item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Deseas borrar esta imagen?")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    String t = item.text;
                    int a= Integer.parseInt(t);
Log.v("","");

                        imageUrls.remove(a);
                    consultarSql();
                    }
                })
                .setNegativeButton("Caancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
         builder.create();
         builder.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    //    getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //https://www.journaldev.com/13792/android-gridlayoutmanager-example
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }


    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100  ,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    public void tomarFoto(){

        Intent imageTakeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(imageTakeIntent.resolveActivity(this.getPackageManager())!=null){

            startActivityForResult(imageTakeIntent,REQUEST_IMAGE_CAPTURE);
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // if(resultCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
            roundedBitmapDrawable.setCircular(false);


            String encodedImage = encodeImage(imageBitmap);

            imageUrls.add(encodedImage);

            arrayList.clear();
            consultarSql();

        }
    }
    private void consultarSql() {

        try {
            arrayList.clear();

        for (i=0;i<imageUrls.size();i++) {
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    StrictMode.setThreadPolicy(policy);
if(imageUrls.get(i).length()>100){
    String temp = String.valueOf(i);

    byte[] decodedString = Base64.decode(imageUrls.get(i), Base64.DEFAULT);
    Bitmap imageBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
    roundedBitmapDrawable.setCircular(false);
    arrayList.add(new Item(temp, roundedBitmapDrawable, "#09A9FF"));
}
else {

    URL url = new URL("https://uap-inventarios-wa.azurewebsites.net/" + imageUrls.get(i));

    Bitmap imm = BitmapFactory.decodeStream((InputStream) url.getContent());
    String temp = String.valueOf(i);

    Bitmap imageBitmap = imm;
    RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
    roundedBitmapDrawable.setCircular(false);
    arrayList.add(new Item(temp, roundedBitmapDrawable, "#09A9FF"));
}
}

                HomeAdapter adapter = new HomeAdapter(this, arrayList, this);
                recyclerView.setAdapter(adapter);

            } catch (IOException ex) {
Log.v("ex",ex.toString());

        }

    }

    public void guardaDatos() throws JSONException {

        final ProgressDialog progressDialog = new ProgressDialog(this,
                R.style.Theme_AppCompat_Light_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Guardando datos...");
        progressDialog.show();
    JSONArray idsareas = new JSONArray();

        int numEntero = Integer.parseInt(id);
        for (int a =0;a<imageUrls.size(); a++) {

            JSONObject imagen = new JSONObject();
            String img = "";

            img = imageUrls.get(a);

            imagen.put("Imagen",img );
            idsareas.put(imagen);
        }
        SharedPreferences preferences =getSharedPreferences("VARIABLES", Context.MODE_PRIVATE);
        String temp= preferences.getString("correo","");
        JSONObject datos = new JSONObject();
        try {
            datos.put("Id",numEntero);
             datos.put("Imagenes",idsareas);
            datos.put("Correo",temp);




        } catch (JSONException e) {
            e.printStackTrace();

        }


        RequestQueue requstQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://uap-inventarios-wa.azurewebsites.net/api/ArticulosApi/saveImagenesArticuloMovil",datos,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(progressDialog!=null){
                                progressDialog.dismiss();
                            }

                            int result = (int) response.get("resultado");

                            if(result == 1){
finish();

                            }else{

                                String error=response.getString("mensaje");

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
