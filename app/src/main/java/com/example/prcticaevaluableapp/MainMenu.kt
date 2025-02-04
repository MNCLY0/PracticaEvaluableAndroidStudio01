package com.example.prcticaevaluableapp

import android.database.sqlite.SQLiteDatabase
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.example.prcticaevaluableapp.DB.DBConexion
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.tabs.TabLayout

class MainMenu : AppCompatActivity() {

    private lateinit var ventanaDeslizante: ViewPager
    private lateinit var tablayout: TabLayout
    private lateinit var usuarioLogged : Usuario
    private lateinit var toolbar : Toolbar

    private lateinit var viewHobbies : View
    private val manejadorImagenes: ManejadorImagenes = ManejadorImagenes()
    private lateinit var botonAddHobbie : Button


    var conexion: DBConexion? = null
    var db: SQLiteDatabase? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainmenu)

        inicializarLateinits()

        @Suppress("DEPRECATION")
        Log.i(R.string.app_name.toString(), "Se procede a serializar el usuario")
        usuarioLogged = intent.getSerializableExtra("usuario") as Usuario
        println("Usuario logged? ${usuarioLogged.nombre}")

        obtenerImagenUsuario()

        setSupportActionBar(toolbar)

        inicializarTablayout()

    }

    private fun inicializarLateinits()
    {
        ventanaDeslizante = findViewById(R.id.viewpager)
        tablayout = findViewById(R.id.tabLayout)
        toolbar = findViewById(R.id.toolbarMainMenu)
    }

    private fun obtenerImagenUsuario()
    {
        conexion = DBConexion(this)
        db = conexion!!.writableDatabase

        usuarioLogged.imagen = conexion!!.obtenerFotoUsuario(db,usuarioLogged)
    }

    //Función que inicializa el tablayout
    private fun inicializarTablayout()
    {
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


//    Ponemos el menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        //Ponemos la imagen del usuario en el toolbar
        findViewById<ShapeableImageView>(R.id.fotoPerfilMainMenu).setBackgroundDrawable(manejadorImagenes.byteArrayToDrawable(this, usuarioLogged.imagen))
        return true
    }


}