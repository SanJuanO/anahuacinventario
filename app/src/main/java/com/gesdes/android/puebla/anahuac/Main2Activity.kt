package com.gesdes.android.puebla.anahuac

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gesdes.android.puebla.anahuac.Utilidades.ConexionSQLiteHelper
import com.gesdes.android.puebla.anahuac.Utilidades.Item
import com.gesdes.android.puebla.anahuac.Utilidades.Utilidades
import com.gesdes.android.puebla.anahuac.adapter.HomeAdapter
import com.gesdes.android.puebla.anahuac.adapter.ImagesAdapter
import kotlinx.android.synthetic.main.activity_agregarareas.*
import kotlinx.android.synthetic.main.activity_main2.*
import java.io.ByteArrayOutputStream

class Main2Activity : AppCompatActivity() {
    private var Imgbase64: String? = null

    var imageUrls= ArrayList<String>()
     val recyclerView: RecyclerView? = null
     val arrayList: java.util.ArrayList<Item>? = null
     val imgenesarray: Set<String>? = null
    var imagenes: java.util.ArrayList<String>? = null
    val i = 0
     val REQUEST_IMAGE_CAPTURE = 101
    var conn: ConexionSQLiteHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        imageUrls = intent.getStringArrayListExtra("imagenes")
        var titulod = intent.getStringExtra("titulod")

        imageRecycler2.layoutManager = LinearLayoutManager(this)
        imageRecycler2.adapter = ImagesAdapter(imageUrls)


        tituloscategoria.text=titulod

    }
    fun agregarfoto(view: View){
        tomarFoto()
    }
    fun terminar(view: View){
        finish()
    }

    fun tomarFoto() {
        val imageTakeIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (imageTakeIntent.resolveActivity(this.packageManager) != null) {
            startActivityForResult(imageTakeIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: android.content.Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val extras = data!!.extras
            val imageBitmap = extras!!["data"] as Bitmap?
            val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(resources, imageBitmap)
            roundedBitmapDrawable.isCircular = false
            var encodedImage: String? = encodeImage(imageBitmap!!)

            imageUrls.add(encodedImage!!)
            imageRecycler2.layoutManager = LinearLayoutManager(this)
            imageRecycler2.adapter = ImagesAdapter(imageUrls)
        }
    }

     fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

}
