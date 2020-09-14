package com.gesdes.android.puebla.anahuac.Model;

public class Planet {

    private String titulon;
    private String ubicacionn;
    private String folion;
    private String mensajen;
    private String fechan;
    private String colorn;
    private String img;


    public Planet(String titulon, String ubicacionn, String folion, String mensajen, String fechan, String colorn,String imagen) {
        this.titulon = titulon;
        this.ubicacionn = ubicacionn;
        this.folion = folion;
        this.mensajen = mensajen;
        this.fechan = fechan;
        this.colorn = colorn;
        this.img = imagen;

    }

    public String gettitulon() {
        return titulon;
    }


    public String getUbicacionn() {
        return ubicacionn;
    }



    public String getFolion() {
        return folion;
    }


    public String getMensajen() {
        return mensajen;
    }

    public String getFechan() {
        return fechan;
    }
    public String getColorn() {
        return colorn;
    }
    public String getImg() {
        return img;
    }

}