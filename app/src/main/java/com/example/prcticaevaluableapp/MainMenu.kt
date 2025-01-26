package com.example.prcticaevaluableapp

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.example.prcticaevaluableapp.DB.DBConexion
import com.google.android.material.tabs.TabLayout

class MainMenu : AppCompatActivity() {

    private lateinit var ventanaDeslizante: ViewPager
    private lateinit var tablayout: TabLayout
    private lateinit var usuarioLogged : Usuario
    private lateinit var toolbar : Toolbar

    private lateinit var viewHobbies : View

    private lateinit var botonAddHobbie : Button


    var conexion: DBConexion? = null
    var db: SQLiteDatabase? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainmenu)

        ventanaDeslizante = findViewById(R.id.viewpager)
        tablayout = findViewById(R.id.tabLayout)
        toolbar = findViewById(R.id.toolbarMainMenu)

        @Suppress("DEPRECATION")
        Log.i(R.string.app_name.toString(), "Se procede a serializar el usuario")
        usuarioLogged = intent.getSerializableExtra("usuario") as Usuario
        println("Usuario logged? ${usuarioLogged.nombre}")

        setSupportActionBar(toolbar);
        val controlador = ControladorVentanasDeslizantes(supportFragmentManager)

        val hobbiesFragment = HobbiesFragment()

        val bundle = Bundle()
        bundle.putSerializable("usuario", usuarioLogged)

        hobbiesFragment.arguments = bundle

        controlador.addFragment(hobbiesFragment, "Hobbies")
        controlador.addFragment(ViajesFragment(), "Viajes")

        ventanaDeslizante.adapter = controlador

        tablayout.setupWithViewPager(ventanaDeslizante)

    }

    //Ponemos el menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        menu?.findItem(R.id.nombreUser)?.setTitle(usuarioLogged.nombre)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
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


    private fun introducirHobbiesARecycler()
    {
        val hobbies = usuarioLogged.hobbies
    }


}