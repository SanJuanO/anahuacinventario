package com.gesdes.android.puebla.anahuac.adapter

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.gesdes.android.puebla.anahuac.R
import com.squareup.picasso.Picasso

class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val myImageView: ImageView = itemView.findViewById<ImageView>(R.id.myImageView)

    fun updateWithUrl(url: String) {
        Picasso.with(itemView.context).load(url).into(myImageView)

    }
}