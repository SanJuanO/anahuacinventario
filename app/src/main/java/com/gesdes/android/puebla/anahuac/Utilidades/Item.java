package com.gesdes.android.puebla.anahuac.Utilidades;

/*
 * Created by Sambhaji Karad on 04-Jan-18
 * Mobile 9423476192
 * Email sambhaji2134@gmail.com/
*/

import androidx.core.graphics.drawable.RoundedBitmapDrawable;

public class Item {

    public String text;
    public RoundedBitmapDrawable drawable;
    public String color;

    public Item(String text, RoundedBitmapDrawable drawable, String color ) {
        this.text = text;
        this.drawable = drawable;
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public RoundedBitmapDrawable getDrawable() {
        return drawable;
    }

    public void setDrawable(RoundedBitmapDrawable drawable) {
        this.drawable = drawable;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
