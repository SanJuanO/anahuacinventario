package com.gesdes.android.puebla.anahuac.Fragment;

import com.gesdes.android.puebla.anahuac.Escanearqr;
import com.gesdes.android.puebla.anahuac.Fabrica;
import com.gesdes.android.puebla.anahuac.Foto;
import com.gesdes.android.puebla.anahuac.R;

import android.app.AlertDialog;
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

import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gesdes.android.puebla.anahuac.Utilidades.ConexionSQLiteHelper;
import com.gesdes.android.puebla.anahuac.Utilidades.Utilidades;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AgregarProducto extends Fragment {

    private String Imgbase64;
    private ImageView mimageView;
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    List<String> tables = new ArrayList<>();

    private TextView ti;

    ConexionSQLiteHelper conn;
TextView circulo;
    EditText etNombre,etApellidos,etEmail,codigotext,fab,ecategorian,eubicacionn,centrocostosn,unidadn,enombren,edescripn,efactuan,diasnm,avisonm,ecantidadn,ecoston,notasn,modelon,stock,eserie;
    String Nombre,Apellidos,Email,Celular,URL_API,pkCliente;
    ImageButton fabb,btnclase,btnubicacion,btncentro,btnunidad;
    MenuItem menuItem,menuItem1;

    JSONObject datos2 = new JSONObject();
    JSONArray imagenes = new JSONArray();


    public AgregarProducto() {
        // Required empty public constructor
        Log.d("hola","");

    }

    public static AgregarProducto newInstance() {
        AgregarProducto fragment = new AgregarProducto();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        conn=new ConexionSQLiteHelper(getActivity(),"bd_usuarios",null,1);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment AE:C7:67:84:BC:94:58:77:1E:EA:CF:6B:8A:85:1B:29:F0:3F:C6:29
        byte[] sha1 = {
                (byte)0xAE, (byte)0xC7, (byte)0x67, (byte)0x84, (byte)0xBC,(byte) 0x94, 0x58, 0x77, 0x1E, (byte)0xEA, (byte)0xCF, (byte)0x6B, (byte)0x8A, (byte)0x85, 0x1B, 0x29, (byte)0xF0, (byte)0x3F, (byte)0xC6, (byte)0x29
        };
        String v= Base64.encodeToString(sha1, Base64.NO_WRAP);
        Log.e("keyhash", v);
        View view=inflater.inflate(R.layout.fragment_perfil2, container, false);
        mimageView=view.findViewById(R.id.foton);
        fabb=view.findViewById(R.id.selectprovedor);
        fab=view.findViewById(R.id.efabricanten);
        eserie=view.findViewById(R.id.eserie);

        ecategorian =view.findViewById(R.id.ecategorian);
        btnclase=view.findViewById(R.id.btnclase);
        eubicacionn=view.findViewById(R.id.eubicacionn);
        btnubicacion=view.findViewById(R.id.btnubicacion);

        centrocostosn=view.findViewById(R.id.centrocostosn);
        btncentro=view.findViewById(R.id.btncentro);
        unidadn=view.findViewById(R.id.unidadn);
        btnunidad=view.findViewById(R.id.btnunidad);

         enombren=view.findViewById(R.id.enombren);
         edescripn=view.findViewById(R.id.edescripn);
         efactuan=view.findViewById(R.id.efactuan);
         diasnm=view.findViewById(R.id.diasnm);
         avisonm=view.findViewById(R.id.avisonm);
         ecantidadn=view.findViewById(R.id.ecantidadn);
         ecoston=view.findViewById(R.id.ecoston);
         notasn=view.findViewById(R.id.enotasn);
        modelon=view.findViewById(R.id.emodelo);
        stock=view.findViewById(R.id.stock);
        circulo=view.findViewById(R.id.circulored);
        Button articulonuevo=view.findViewById(R.id.btnarticulo);

        SharedPreferences preferences =getActivity().getSharedPreferences("VARIABLES", Context.MODE_PRIVATE);
        boolean temp= preferences.getBoolean("PCAR",false);

if(!temp){
    articulonuevo.setVisibility(View.INVISIBLE);
}

        articulonuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                consultarSql();
                Boolean p = true;
   String provedorc=  fab.getText().toString();
                String categoriae=  ecategorian.getText().toString();
                String ubicacione= eubicacionn.getText().toString();
                String costose=  centrocostosn.getText().toString();
                String undade=  unidadn.getText().toString();

                String nombree=  enombren.getText().toString();
                String desce=  edescripn.getText().toString();
                String efacte=  efactuan.getText().toString();
                String diase= diasnm.getText().toString();
                String avisoe=  avisonm.getText().toString();
                String ecante=  ecantidadn.getText().toString();
                String ecoste=  ecoston.getText().toString();
                String notase=  notasn.getText().toString();
                String modelo=  modelon.getText().toString();
                String stoc=  stock.getText().toString();

                if (nombree.isEmpty()) {
                    Toast.makeText(getActivity(),  "Escribe el nombre del producto", Toast.LENGTH_SHORT).show();
                    p=false;
                }
               else if (desce.isEmpty()) {
                    p=false;

                    Toast.makeText(getActivity(),  "Escribe una descripción", Toast.LENGTH_SHORT).show();
                }
                else if (modelo.isEmpty()) {
                    Toast.makeText(getActivity(),  "Escribe el modelo", Toast.LENGTH_SHORT).show();
                    p=false;

                }
                else if (efacte.isEmpty()) {
                    Toast.makeText(getActivity(),  "Escribe una factura", Toast.LENGTH_SHORT).show();
                    p=false;
                }
                else if (diase.isEmpty()) {
                    Toast.makeText(getActivity(),  "Cada cuanto dias debe tener mantenimiento", Toast.LENGTH_SHORT).show();
                    p=false;
                }
                else if (avisoe.isEmpty()) {
                    Toast.makeText(getActivity(),  "Dias de entacipo de notificación", Toast.LENGTH_SHORT).show();
                    p=false;
                }
                else if (ecante.isEmpty()) {
                    Toast.makeText(getActivity(),  "Falta una cantidad", Toast.LENGTH_SHORT).show();
                    p=false;
                }
                else if (ecoste.isEmpty()) {
                    Toast.makeText(getActivity(),  "Pon el costo", Toast.LENGTH_SHORT).show();
                    p=false;
                }
                else if (notase.isEmpty()) {
                    Toast.makeText(getActivity(),  "Escriba una nota", Toast.LENGTH_SHORT).show();
                    p=false;
                }

                else if (provedorc.isEmpty()) {
                    Toast.makeText(getActivity(),  "Seleccione un provedor", Toast.LENGTH_SHORT).show();
                    p=false;
                }






                 else if (categoriae.isEmpty()) {
                    Toast.makeText(getActivity(),  "Seleccione una Clase", Toast.LENGTH_SHORT).show();
                    p=false;
                }
                else if (ubicacione.isEmpty()) {
                    Toast.makeText(getActivity(),  "Seleccione una ubicación", Toast.LENGTH_SHORT).show();
                    p=false;
                }
                else if (costose.isEmpty()) {
                    Toast.makeText(getActivity(),  "Seleccione un Costo ", Toast.LENGTH_SHORT).show();
                    p=false;

                }
                if (stoc.isEmpty()) {
                    Toast.makeText(getActivity(),  "Escribe minimo de stock", Toast.LENGTH_SHORT).show();
                    p=false;
                }
                else if (undade.isEmpty()) {
                    p=false;
                    Toast.makeText(getActivity(),  "Seleccione una Unidad de medida", Toast.LENGTH_SHORT).show();
                }

                if (p) {
                    consultarSql();

                    nuevoarticulo();

                }

            }
        });


        mimageView=view.findViewById(R.id.foton);

        fabb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fabr();

            }
        });

        btnclase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clase();

            }
        });
        btnubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ubic();

            }
        });
        btncentro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                centro();

            }
        });
        btnunidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                unidad();

            }
        });


        mimageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tomarFoto();
            }
        });






        return view;
    }

    public void guardaDatos(){

        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                R.style.Theme_AppCompat_Light_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Guardando datos...");
        progressDialog.show();

        Nombre=etNombre.getText().toString();
        Apellidos=etApellidos.getText().toString();
        Email=etEmail.getText().toString();

        if(!validate()){
            return;
        }

        JSONObject datos = new JSONObject();
        try {
            datos.put("PK",pkCliente);
            datos.put("NOMBRE",Nombre);
            datos.put("APELLIDOS",Apellidos);
            datos.put("CORREO",Email);
            if(!Imgbase64.isEmpty()){
                datos.put("FOTO",Imgbase64);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestQueue requstQueue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL_API,datos,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(progressDialog!=null){
                                progressDialog.dismiss();
                            }

                            int result = (int) response.get("resultado");

                            if(result == 1){

                                SharedPreferences preferencias = getActivity().getSharedPreferences("VARIABLES", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferencias.edit();
                                editor.putString("FOTO",Imgbase64);
                                editor.commit();
                                _ShowAlert("Bien","¡Datos actualizados correctamente!");

                            }else{

                                String error=response.getString("mensaje");
                                _ShowAlert("Faltan datos",error);

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

    public boolean validate(){
        if(Nombre.isEmpty()){
            etNombre.setError("El nombre no puede estar vacìo");
            return false;
        }
        if(Apellidos.isEmpty()){
            etApellidos.setError("El campo apellidos no puede estar vacìo");
            return false;
        }
        if(Email.isEmpty()){
            etEmail.setError("El campo email no puede estar vacìo");
            return false;
        }

        return true;
    }



    public void tomarFoto(){

        Intent intent = new Intent(getActivity(), Foto.class);
        startActivityForResult(intent,99);

    }
    public void escanearcodigo(){
        Intent intent = new Intent(getActivity(), Escanearqr.class);
        startActivityForResult(intent, this.REQUEST_IMAGE_CAPTURE );


    }
    public void ubic(){
        Intent intent = new Intent(getActivity(), Fabrica.class);
        intent.putExtra("config", "ubicacion");
        startActivityForResult(intent, 11);


    }
    public void clase(){
        Intent intent = new Intent(getActivity(), Fabrica.class);
        intent.putExtra("config", "categoria");
        startActivityForResult(intent, 12);


    }
    public void centro(){
        Intent intent = new Intent(getActivity(), Fabrica.class);
        intent.putExtra("config", "centro");
        startActivityForResult(intent, 13);


    }
    public void unidad(){
        Intent intent = new Intent(getActivity(), Fabrica.class);
        intent.putExtra("config", "unidad");
        startActivityForResult(intent, 14);


    }
    public void fabr(){
        Intent intent = new Intent(getActivity(), Fabrica.class);
        intent.putExtra("config", "fabrica");
        startActivityForResult(intent, 1);


    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


             if(requestCode == 11) {

                 SharedPreferences preferences =getActivity().getSharedPreferences("VARIABLES", Context.MODE_PRIVATE);
                 String temp= preferences.getString("UBICACIONAREA","");
                 String temp2= preferences.getString("SELECTSUB","");

if(temp2!="") {


    eubicacionn.setText(temp + "/" + temp2);

}
             }

            if(requestCode == 12) {

                SharedPreferences preferences =getActivity().getSharedPreferences("VARIABLES", Context.MODE_PRIVATE);
                String temp= preferences.getString("PROVEDORNNAME","");
                String idarea= preferences.getString("PROVEDORNID","");

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("nuevocategoria",idarea);
                editor.commit();

                ecategorian.setText(temp);


            }


               if(requestCode == 13) {

                   SharedPreferences preferences =getActivity().getSharedPreferences("VARIABLES", Context.MODE_PRIVATE);
                   String temp= preferences.getString("PROVEDORNNAME","");
                   centrocostosn.setText(temp);

                   String idarea= preferences.getString("PROVEDORNID","");

                   SharedPreferences.Editor editor = preferences.edit();

                   editor.putString("nuevocentro",idarea);
                   editor.commit();

               }




        if(requestCode == 1) {

            SharedPreferences preferences =getActivity().getSharedPreferences("VARIABLES", Context.MODE_PRIVATE);
            String temp= preferences.getString("PROVEDORNNAME","");
            fab.setText(temp);

            String idarea= preferences.getString("PROVEDORNID","");

            SharedPreferences.Editor editor = preferences.edit();

            editor.putString("nuevoprobedor",idarea);
            editor.commit();

        }


        if(requestCode == 14) {

            SharedPreferences preferences =getActivity().getSharedPreferences("VARIABLES", Context.MODE_PRIVATE);
            String temp= preferences.getString("PROVEDORNNAME","");
            unidadn.setText(temp);
            String idarea= preferences.getString("PROVEDORNID","");

            SharedPreferences.Editor editor = preferences.edit();

            editor.putString("nuevounidad",idarea);
            editor.commit();

        }
        if(requestCode == 99) {

            consultarSqltemporal();

        }


        
          SharedPreferences preferencias = getActivity().getSharedPreferences("VARIABLES", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferencias.edit();


                            editor.putString("PROVEDORNID","" );

                            editor.putString("PROVEDORNNAME","" );

                               editor.putString("escaneadon","" );



                        editor.commit();


    }

    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100  ,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    private void _ShowAlert(String title, String mensaje){

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(mensaje);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
    private void nuevoarticulo(){
        SharedPreferences preferences =getActivity().getSharedPreferences("VARIABLES", Context.MODE_PRIVATE);
        String idarea= preferences.getString("SELECTAREAID","");
        String idubicacion= preferences.getString("SELECTUBICACIONID","");
        String sub= preferences.getString("SELECTSUBID","");

        String idcategoria= preferences.getString("nuevocategoria","");
        String idcentro= preferences.getString("nuevocentro","");
        String idprobedor= preferences.getString("nuevoprobedor","");
        String idunidad= preferences.getString("nuevounidad","");

            final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                    R.style.Theme_AppCompat_Light_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Guardando datos...");
            progressDialog.show();
        String nombree=  enombren.getText().toString();
        String desce=  edescripn.getText().toString();
        String efacte=  efactuan.getText().toString();
        String diase= diasnm.getText().toString();
        String avisoe=  avisonm.getText().toString();
        String ecante=  ecantidadn.getText().toString();
        String ecoste=  ecoston.getText().toString();
        String notase=  notasn.getText().toString();
        String modelo=  modelon.getText().toString();
        String sto=  stock.getText().toString();
        String serie=  eserie.getText().toString();

        int cantidad = 0;
        Double costo = 0.0;
        int diasaviso = 0;
        int diasmantenimiento = 0;
        int idare = 0;
        int s = 0;

        int idcentroi = 0;
        int idclasei = 0;
        int idprovedori = 0;
        int idubicacioni = 0;
        int idunidadi = 0;
        cantidad = Integer.parseInt(ecante);
        costo = Double.parseDouble(ecoste);
        diasaviso = Integer.parseInt(avisoe);
        diasmantenimiento = Integer.parseInt(diase);
        s = Integer.parseInt(sto);
        String temp = preferences.getString("correo", "");
        idcentroi = Integer.parseInt(idcentro);
        idclasei = Integer.parseInt(idcategoria);
        idprovedori = Integer.parseInt(idprobedor);
        idubicacioni = Integer.parseInt(idubicacion);
        idunidadi = Integer.parseInt(idunidad);
        idare = Integer.parseInt(idarea);
        int subarea=Integer.parseInt(sub);

        diasaviso = diasaviso - diasaviso*2;
        JSONObject datos = new JSONObject();
            try {
                datos.put("Cantidad",cantidad);
                datos.put("CantidadAviso",s);
                datos.put("Costo",costo);
                datos.put("DiasAviso",diasaviso);
                datos.put("DiasMantenimiento",diasmantenimiento);
                datos.put("IdCentroCostos",idcentroi);
                datos.put("IdClase",idclasei);
                datos.put("IdProveedor",idprovedori);
                datos.put("IdUbicacion",idubicacioni);
                datos.put("IdUnidadMedida",idunidadi);
                datos.put("IdArea",idare);
                datos.put("IdEstadoArticulo",1);

                datos.put("Modelo",modelo);
                datos.put("NoFactura",efacte);
                datos.put("Nombre",nombree);
                datos.put("Nota",notase);
                datos.put("Descripcion",desce);
                datos.put("Imagenes",imagenes);
                datos.put("Correo",temp);
                datos.put("Idsubarea",subarea);
                datos.put("serie",serie);




            } catch (JSONException e) {
                e.printStackTrace();
            }


            RequestQueue requstQueue = Volley.newRequestQueue(getActivity());

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://uap-inventarios-wa.azurewebsites.net/api/ArticulosApi/addArticuloMovil",datos,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                          if(progressDialog!=null){
                                    progressDialog.dismiss();
                                }

                                int result = (int) response.get("resultado");

                                if(result == 1){


                                    _ShowAlert("Bien","¡Articulo agregado  correctamente!");

                                    ecategorian.setText("");
                                    eubicacionn.setText("");
                                    centrocostosn.setText("");
                                    unidadn.setText("");
                                    enombren.setText("");
                                    edescripn.setText("");
                                    efactuan.setText("");
                                    diasnm.setText("");
                                    avisonm.setText("");
                                    ecantidadn.setText("");
                                    ecoston.setText("");
                                    notasn.setText("");
                                    modelon.setText("");
                                    ecategorian.setText("");
                                    centrocostosn.setText("");
                                    fab.setText("");
                                    unidadn.setText("");
                                    eubicacionn.setText("");
                                    preferences.edit().remove("UBICACIONAREA").commit();
                                    preferences.edit().remove("SELECTSUB").commit();
                                    preferences.edit().remove("PROVEDORNID").commit();
                                    preferences.edit().remove("PROVEDORNNAME").commit();
                                    preferences.edit().remove("escaneadon").commit();

eliminarfotos();


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
    private void consultarSql() {
        SQLiteDatabase db=conn.getReadableDatabase();
        String[] parametros={"dos"};

        try {
            //select nombre,telefono from usuario where codigo=?
            Cursor cursor=db.rawQuery("SELECT "+ Utilidades.CAMPO_NOMBRE+","+Utilidades.CAMPO_TELEFONO+
                    " FROM "+Utilidades.TABLA_USUARIO+" WHERE "+Utilidades.CAMPO_NOMBRE+"=? ",parametros);

            Integer t = cursor.getCount();
            while (cursor.moveToNext()) {
                JSONObject imagen = new JSONObject();

                imagen.put("Imagen",cursor.getString(1));
                imagenes.put(imagen);

            }



        }catch (Exception e){
        }

    }
    private void consultarSqltemporal() {
        SQLiteDatabase db=conn.getReadableDatabase();
        String[] parametros={"dos"};

        try {
            //select nombre,telefono from usuario where codigo=?
            Cursor cursor=db.rawQuery("SELECT "+ Utilidades.CAMPO_NOMBRE+","+Utilidades.CAMPO_TELEFONO+
                    " FROM "+Utilidades.TABLA_USUARIO+" WHERE "+Utilidades.CAMPO_NOMBRE+"=? ",parametros);

            Integer t = cursor.getCount();
            int i=0;
            String img="";
            while (cursor.moveToNext()) {
                i=i+1;
                img = cursor.getString(1);

            }
            byte[] decodedString = Base64.decode(img, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);


            mimageView.setImageBitmap(decodedByte);

String temporal = String.valueOf(i);
            circulo.setText(temporal);

        }catch (Exception e){
        }

    }

    private void eliminarfotos() {
        SQLiteDatabase db=conn.getWritableDatabase();
        String[] parametros={"dos"};

        db.delete(Utilidades.TABLA_USUARIO,Utilidades.CAMPO_NOMBRE+"=?",parametros);
       // Toast.makeText(this,"Ya se Eliminó el usuario",Toast.LENGTH_LONG).show();

        db.close();
    }

}
