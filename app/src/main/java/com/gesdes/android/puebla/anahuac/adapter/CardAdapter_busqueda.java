package com.gesdes.android.puebla.anahuac.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gesdes.android.puebla.anahuac.Detalle;
import com.gesdes.android.puebla.anahuac.Model.Planet;
import com.gesdes.android.puebla.anahuac.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CardAdapter_busqueda extends RecyclerView.Adapter<CardAdapter_busqueda.PlanetHolder> {

    private Context context;
    private ArrayList<Planet> planets;

    public CardAdapter_busqueda(Context context, ArrayList<Planet> planets) {
        this.context = context;
        this.planets = planets;
    }

    @NonNull
    @Override
    public PlanetHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_layout_buscar, parent, false);
        return new PlanetHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanetHolder holder, int position) {
        Planet planet = planets.get(position);
        holder.bind(planets.get(position));
        holder.setDetails(planet);
    }

    @Override
    public int getItemCount() {
        return planets.size();
    }

    class PlanetHolder extends RecyclerView.ViewHolder {
        private TextView titulon;
        private TextView ubicacionn;
        private TextView folion;
        private TextView mensajen;
        private TextView fechan;
        private String colorn;
        private ImageView imagenn;
        private CardView estatus;
        private boolean toque=false ;


        PlanetHolder(View itemView) {
            super(itemView);
            titulon = itemView.findViewById(R.id.titulon);
            ubicacionn = itemView.findViewById(R.id.ubicacionarean);
            folion = itemView.findViewById(R.id.folion);
            fechan = itemView.findViewById(R.id.fechan);
            estatus = itemView.findViewById(R.id.colorestatusnotificaciones);
            imagenn = itemView.findViewById(R.id.imm);
            estatus = itemView.findViewById(R.id.colorestatusnotificaciones);

        }

        @SuppressLint("ResourceAsColor")
        void setDetails(Planet planet) {
            titulon.setText(planet.gettitulon());
            ubicacionn.setText(planet.getUbicacionn());
            folion.setText(planet.getFolion());
            fechan.setText(planet.getFechan());
            String im = planet.getImg();
            im = "https://uap-inventarios-wa.azurewebsites.net/" + im;
            Picasso.with(context).load(im).into(imagenn);
            String r = planet.getColorn();
            int red = Color.parseColor(r);
            estatus.setCardBackgroundColor(red);
        }

        void bind(final Planet employee) {


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (toque == false) {
                        toque = false;
                        int i = getAdapterPosition();
                        i = i;

                        String d = employee.getFolion();
                        Intent intent = new Intent(context, Detalle.class);
                        intent.putExtra("folio", d);
                        intent.putExtra("escaneo", "false");

                        context.startActivity(intent);
                    }
                }
            });
        }
    }

}

