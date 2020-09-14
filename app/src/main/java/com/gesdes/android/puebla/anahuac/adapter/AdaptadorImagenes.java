package com.gesdes.android.puebla.anahuac.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gesdes.android.puebla.anahuac.R;

public class AdaptadorImagenes extends BaseAdapter {
    //--Declaramos las variables
    private Context context;
    //--Arreglo de filas
    private final String[] rowValues;
    //--Constructor de variables recibidas
    public AdaptadorImagenes(Context context, String[] rowValues) {
        this.context = context;
        this.rowValues = rowValues;
    }
    //--Muestra imagenes y texto
    public View getView(int position, View convertView, ViewGroup parent) {
        //--Variable de tipo LayoutInflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //--Variable visual de gridview
        View gridView;
        //--Si la función es nulla
        if (convertView == null) {
            //--Asigan el context visual
            gridView = new View(context);

            //--Asigna el layout de filas
            assert inflater != null;
            gridView = inflater.inflate(R.layout.gridrows, null);

            //--Agrega el texto recibido al TextView
            TextView textView = (TextView) gridView.findViewById(R.id.grid_text_row);
            textView.setText(rowValues[position]);

            //--Agrega la imagen recibida en la ImageView
            ImageView imageView = (ImageView) gridView
                    .findViewById(R.id.grid_img_row);
            //--Posicion de la fila en la que se asigna el valor
            String row = rowValues[position];
            //--Dependiendo del tipo de nombre asigna la imagen
            if (row.equals("microsoft")) {
                imageView.setImageResource(R.drawable.appi);
            } else if (row.equals("apple")) {
                imageView.setImageResource(R.drawable.appi2);
            } else if (row.equals("facebook")) {
                imageView.setImageResource(R.drawable.fondochat1);}
            else if (row.equals("twitter")) {
                imageView.setImageResource(R.drawable.logo);}
            else if (row.equals("youtube")) {
                imageView.setImageResource(R.drawable.logoanahuac);
            } else {
                imageView.setImageResource(R.drawable.ic_group_add_black_24dp);
            }

        } else {
            gridView = (View) convertView;
        }
        //--Devuelve la visualización construida
        return gridView;
    }

    @Override
    public int getCount() {
        return rowValues.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
