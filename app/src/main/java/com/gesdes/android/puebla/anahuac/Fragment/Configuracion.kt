package com.gesdes.android.puebla.anahuac.Fragment;



import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.gesdes.android.puebla.anahuac.*
import com.gesdes.android.puebla.anahuac.Utilidades.AuthenticationManager
import kotlinx.android.synthetic.main.fragment_configuracion.view.*


class Configuracion : Fragment() {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private val ARG_PARAM1 = "param1"
    var toque:Boolean =false


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView =inflater.inflate(R.layout.fragment_configuracion, container, false)
        val context = this
        val preferences = this.requireActivity().getSharedPreferences("VARIABLES", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("idencargado", "")


        editor.commit()

        rootView.fabrica.setOnClickListener(){
            val temp = preferences.getBoolean("PLPR", false)

            if (!temp) {
                Toast.makeText(activity, "No cuentas con permiso", Toast.LENGTH_SHORT).show()
            }else {
                if (toque == false) {

                val intent = Intent(activity, Fabrica::class.java)
                intent.putExtra("config", "fabrica");
                intent.putExtra("donde", 1);


                startActivity(intent)
                }
            }

        }
        rootView.ubicacion.setOnClickListener(){

            val temp = preferences.getBoolean("PLUB", false)

            if (!temp) {
                Toast.makeText(activity, "No cuentas con permiso", Toast.LENGTH_SHORT).show()
            }else {
                if (toque == false) {
                   toque = false

                    val intent = Intent(activity, Fabrica::class.java)
                    intent.putExtra("config", "ubicacion");
                    intent.putExtra("donde", 1);
                    startActivity(intent)
                }

            }

        }
        rootView.areasadd.setOnClickListener(){

            val temp = preferences.getBoolean("PLUM", false)

            if (!temp) {
                Toast.makeText(activity, "No cuentas con permiso", Toast.LENGTH_SHORT).show()
            }else {
                if (toque == false) {
                   toque = false
                    val intenta = Intent(activity, Areas::class.java)
                    intenta.putExtra("donde", "config");

                    startActivity(intenta)
                }
            }




        }
        rootView.subarreaadd.setOnClickListener(){

            val temp = preferences.getBoolean("PLUM", false)

            if (!temp) {
                Toast.makeText(activity, "No cuentas con permiso", Toast.LENGTH_SHORT).show()
            }else {
                if (toque == false) {
                   toque = false
                    val intenta = Intent(activity, SubAreas::class.java)
                    intenta.putExtra("donde", "config");

                    startActivity(intenta)
                }
            }




        }
        rootView.centrocostos.setOnClickListener(){

            val temp = preferences.getBoolean("PLCC", false)

            if (!temp) {
                Toast.makeText(activity, "No cuentas con permiso", Toast.LENGTH_SHORT).show()
            }else {
                if (toque == false) {
                   toque = false
                    val intent = Intent(activity, Fabrica::class.java)
                    intent.putExtra("config", "centro");
                    intent.putExtra("donde", 1);

                    startActivity(intent)
                }

            }



        }
        rootView.unidadb.setOnClickListener(){
            val temp = preferences.getBoolean("PLUM", false)

            if (!temp) {
                Toast.makeText(activity, "No cuentas con permiso", Toast.LENGTH_SHORT).show()
            }else {
                if (toque == false) {
                   toque = false
                    val intent = Intent(activity, Fabrica::class.java)
                    intent.putExtra("config", "unidad");
                    intent.putExtra("donde", 1);

                    startActivity(intent)
                }

            }




        }


        rootView.categoriaadd.setOnClickListener(){
            val temp = preferences.getBoolean("PLCL", false)

            if (!temp) {
                Toast.makeText(activity, "No cuentas con permiso", Toast.LENGTH_SHORT).show()
            }else {

                if (toque == false) {
                   toque = false
                    val intent = Intent(activity, Fabrica::class.java)
                    intent.putExtra("config", "categoria");
                    intent.putExtra("donde", 1);

                    startActivity(intent)
                }
            }

        }


        rootView.informes.setOnClickListener(){


            AuthenticationManager.getInstance().disconnect()
            val intent = Intent(activity, Informe::class.java)


            startActivity(intent)

        }

        rootView.close.setOnClickListener(){


         AuthenticationManager.getInstance().disconnect()
            val intent = Intent(activity, ConnectActivity::class.java)


            startActivity(intent)
requireActivity().finish()

        }

        return rootView
    }

    companion object {

        @JvmStatic
        fun newInstance() =Configuracion()
    }

    fun areasadd(view: View?) {
        if (toque == false) {
            val intent = Intent(activity, Areas::class.java)
            //   intent.putExtra("config", "ubicacion");

            startActivityForResult(intent, 88)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: android.content.Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        toque=false


    }

}

