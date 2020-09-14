package com.gesdes.android.puebla.anahuac

import android.app.Activity
import android.content.Context
import android.os.Bundle
import me.dm7.barcodescanner.zxing.ZXingScannerView
import me.dm7.barcodescanner.zxing.ZXingScannerView.ResultHandler


class Escanearqr : Activity(), ResultHandler {
    private var mScannerView: ZXingScannerView? = null
    public override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        mScannerView = ZXingScannerView(this) // Programmatically initialize the scanner view
        setContentView(mScannerView) // Set the scanner view as the content view
    }

    public override fun onResume() {
        super.onResume()
        mScannerView!!.setResultHandler(this) // Register ourselves as a handler for scan results.
        mScannerView!!.startCamera() // Start camera on resume
    }

    public override fun onPause() {
        super.onPause()
        mScannerView!!.stopCamera() // Stop camera on pause
    }
    override fun handleResult(rawResult: com.google.zxing.Result) {

        val preferencias = getSharedPreferences("VARIABLES", Context.MODE_PRIVATE)
        val editor = preferencias.edit()
        val po: String = rawResult.toString()
        editor.putString("escaneadon", po)

        editor.commit()
finish()
    }



}