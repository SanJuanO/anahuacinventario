package com.gesdes.android.puebla.anahuac;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gesdes.android.puebla.anahuac.Utilidades.ConexionSQLiteHelper;
import com.gesdes.android.puebla.anahuac.Utilidades.Item;
import com.gesdes.android.puebla.anahuac.Utilidades.Utilidades;
import com.gesdes.android.puebla.anahuac.adapter.HomeAdapter;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class Foto extends AppCompatActivity implements HomeAdapter.ItemListener{
    private RecyclerView recyclerView;
    private ArrayList<Item> arrayList;
    private Set<String> imgenesarray;
    ArrayList<String> imagenes;

    private int i =0;
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    ConexionSQLiteHelper conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fotografias);
        ImageButton btnagregar = (ImageButton) findViewById(R.id.agregarimagen);
        Button btnim = (Button) findViewById(R.id.btnacep);

         conn=new ConexionSQLiteHelper(this,"bd_usuarios",null,1);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView11);
        arrayList = new ArrayList<>();

        //val decodedString1 = Base64.decode(imag, Base64.DEFAULT)
        //val decodedByte = BitmapFactory.decodeByteArray(decodedString1, 0, decodedString1.size)
        // val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(resources, decodedByte)
        // roundedBitmapDrawable.isCircular = true
        //fotouser.setImageDrawable(roundedBitmapDrawable)


consultarSql();
        HomeAdapter adapter = new HomeAdapter(this, arrayList, this);
        recyclerView.setAdapter(adapter);
        btnim.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
finish();

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
                        SQLiteDatabase db=conn.getWritableDatabase();
                        String[] parametros={item.text};

                        db.delete(Utilidades.TABLA_USUARIO,Utilidades.CAMPO_TELEFONO+"=?",parametros);
                       //  Toast.makeText(this,"Ya se Eliminó el usuario",Toast.LENGTH_LONG).show();

                        db.close();

                    consultarSql2();
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
            ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"bd_usuarios",null,1);
            SQLiteDatabase db=conn.getWritableDatabase();
            String insert="INSERT INTO "+Utilidades.TABLA_USUARIO
                    +" ("+Utilidades.CAMPO_NOMBRE+","+Utilidades.CAMPO_TELEFONO+")" +
                    " VALUES ('"+"dos"+"','"
                    +encodedImage+"')";
            db.execSQL(insert);
            db.close();
            String[] parametros={encodedImage};
            SQLiteDatabase dbb=conn.getReadableDatabase();

            try {
                Cursor cursor=dbb   .rawQuery("SELECT "+Utilidades.CAMPO_NOMBRE+","+Utilidades.CAMPO_TELEFONO+
                        " FROM "+Utilidades.TABLA_USUARIO+" WHERE "+Utilidades.CAMPO_TELEFONO+"=? ",parametros);
                List<String> tables = new ArrayList<>();

                Integer t = cursor.getCount();
                while (cursor.moveToNext()) {

                    String temp= cursor.getString(1);
                    arrayList.add(new Item( temp, roundedBitmapDrawable, "#09A9FF"));
                    HomeAdapter adapter = new HomeAdapter(this, arrayList, this);
                    recyclerView.setAdapter(adapter);

                }



            }catch (Exception e){
                Toast.makeText(getApplicationContext(),"El documento no existe",Toast.LENGTH_LONG).show();
            }





        }
    }
    private void consultarSql() {
        SQLiteDatabase db=conn.getReadableDatabase();
        String[] parametros={"dos"};
arrayList.clear();
        try {
            //select nombre,telefono from usuario where codigo=?
            Cursor cursor=db.rawQuery("SELECT "+Utilidades.CAMPO_NOMBRE+","+Utilidades.CAMPO_TELEFONO+
                    " FROM "+Utilidades.TABLA_USUARIO+" WHERE "+Utilidades.CAMPO_NOMBRE+"=? ",parametros);
            List<String> tables = new ArrayList<>();

            Integer t = cursor.getCount();
            while (cursor.moveToNext()) {
                tables.add(cursor.getString(0));
String temp= cursor.getString(1);
                byte[] decodedString = Base64.decode(cursor.getString(1), Base64.DEFAULT);
                Bitmap imageBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                roundedBitmapDrawable.setCircular(false);
                arrayList.add(new Item(temp, roundedBitmapDrawable, "#09A9FF"));

                HomeAdapter adapter = new HomeAdapter(this, arrayList, this);
                recyclerView.setAdapter(adapter);

            }

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG).show();
        }

    }
    private void consultarSql2() {
        SQLiteDatabase db=conn.getReadableDatabase();
        String[] parametros={"dos"};
        arrayList.clear();
        try {
            //select nombre,telefono from usuario where codigo=?
            Cursor cursor=db.rawQuery("SELECT "+Utilidades.CAMPO_NOMBRE+","+Utilidades.CAMPO_TELEFONO+
                    " FROM "+Utilidades.TABLA_USUARIO+" WHERE "+Utilidades.CAMPO_NOMBRE+"=? ",parametros);
            List<String> tables = new ArrayList<>();

            Integer t = cursor.getCount();
            while (cursor.moveToNext()) {
                tables.add(cursor.getString(0));
                String temp= cursor.getString(1);
                byte[] decodedString = Base64.decode(cursor.getString(1), Base64.DEFAULT);
                Bitmap imageBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                roundedBitmapDrawable.setCircular(false);
                arrayList.add(new Item(temp, roundedBitmapDrawable, "#09A9FF"));

                HomeAdapter adapter = new HomeAdapter(this, arrayList, this);
                recyclerView.setAdapter(adapter);

            }
if(arrayList.size()==0){
    HomeAdapter adapter = new HomeAdapter(this, arrayList, this);
    recyclerView.setAdapter(adapter);
}
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG).show();
        }

    }

}
