package com.example.prcticaevaluableapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.view.get
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class MainMenu : AppCompatActivity() {

    private lateinit var ventanaDeslizante: ViewPager
    private lateinit var tablayout: TabLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainmenu)

        ventanaDeslizante = findViewById(R.id.viewpager)
        tablayout = findViewById(R.id.tabLayout)


        val controlador = ControladorVentanasDeslizantes(supportFragmentManager)

        controlador.addFragment(HobbiesFragment(), "Hobbies")
        controlador.addFragment(ViajesFragment(), "Viajes")

        ventanaDeslizante.adapter = controlador

        tablayout.setupWithViewPager(ventanaDeslizante)

    }

    //    Ponemos el menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        menu?.findItem(R.id.nombreUser)?.setTitle(intent.getStringExtra("nombreUser").toString())
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            //El boton de añadir hobbie, lanza el intent AddHobbieAppActivity
            R.id.bntAddHobbie ->
                {
                    lanzarAddHobbie()
                    true
                }
            //El boton de mostrar informacion, lanza el intent InformacionAppActivity
            R.id.btnInformacionApp -> {
                lanzarInformacionDe()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun lanzarInformacionDe(view: View? = null) {
        val i = Intent(this, InformacionAppActivity::class.java)
        startActivity(i)
    }

    private fun lanzarAddHobbie(view: View? = null) {
        val i = Intent(this, AddHobbieAppActivity::class.java)
        startActivity(i)
    }


}