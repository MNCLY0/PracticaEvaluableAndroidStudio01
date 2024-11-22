package com.example.prcticaevaluableapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class main_menu : AppCompatActivity() {
    private lateinit var ventanaDeslizante: ViewPager
    private lateinit var tablayout: TabLayout
    private lateinit var txtTexto: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

//        //obtiene las referencias de los objetos layout
//        ventanaDeslizante = findViewById(R.id.viewpager)
//        tablayout = findViewById(R.id.tabLayout)
//        txtTexto = findViewById(R.id.txtTextoInformacion)
//
//        // Inicia el controlador deslizante de ventanas que recibe como parámetro un objeto supportFragmentManager
//        //Ese objeto gestiona los fragmentos en las actividades.
//        //En Java seria equivalente a this.getSupportFragmentManager()
//        val controlador = ControladorVentanasDeslizantes(supportFragmentManager)
//
//        controlador.addFragment(CorreosFragment(), "Correos")
//        controlador.addFragment(ContactosFragment(), "Contactos")
//
//        //Agregar al controlador el deslizador de vistas
//        //Estamos siciendo que el comprotamiento del viewPager(widget que desliza)
//        //sea el del controlador
//        ventanaDeslizante.adapter = controlador
//        //Vincula el controlasor al tablayout
//        tablayout.setupWithViewPager(ventanaDeslizante)
//
//
//        tablayout.getTabAt(0)?.setIcon(R.drawable.baseline_mail_outline_24)
//        tablayout.getTabAt(1)?.setIcon(R.drawable.baseline_person_outline_24)
//    }
//
//    //    Ponemos el menus
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.action_settings -> true
//
//            R.id.btnInformacionApp -> {
//                lanzarInformacionDe()
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
//
//    private fun lanzarInformacionDe(view: View? = null) {
//        val i = Intent(this, InformacionLegalActivity::class.java)
////        startActivity(i)
//        resultLauncher.launch(i)
//    }
//
//    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
//            result: ActivityResult ->
//        val nombre = result.data?.getStringExtra("Mensaje")
//        if (result.resultCode == Activity.RESULT_OK) {
//            txtTexto.setText("El usuario ha aceptado los terminos" + " " + nombre)
//        }
//        else
//        {
//            txtTexto.setText("El usuario no ha aceptado los terminos")
//        }
    }
}