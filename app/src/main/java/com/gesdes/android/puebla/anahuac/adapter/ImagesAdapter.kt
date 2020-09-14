package com.gesdes.android.puebla.anahuac.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gesdes.android.puebla.anahuac.R
import java.util.ArrayList

class ImagesAdapter(image: ArrayList<String>) : RecyclerView.Adapter<ImageHolder>() {
    val imageUrls: ArrayList<String>

    init {
        imageUrls = image
    }

    override fun getItemCount(): Int {
        return imageUrls.size;
    }





    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        var imageUrl = imageUrls[position]
        holder?.updateWithUrl(imageUrl)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        var imageItem = LayoutInflater.from(parent?.context).inflate(R.layout.image_item, parent, false)
        return ImageHolder(imageItem)    }
}