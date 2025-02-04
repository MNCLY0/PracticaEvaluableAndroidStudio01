package com.example.prcticaevaluableapp

import android.annotation.SuppressLint
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.prcticaevaluableapp.DB.DBConexion
import com.google.android.material.textview.MaterialTextView


class Login : AppCompatActivity() {


    private lateinit var inputUser: EditText
    private lateinit var inputPassword: EditText

    private lateinit var botonAceptar: Button
    private lateinit var botonCrearCuenta: MaterialTextView
    private lateinit var webViewGif: WebView
//    private lateinit var botonSalir: Button

    private lateinit var textoDebug: TextView

    var conexion: DBConexion? = null
    var db: SQLiteDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        inicializarLateinits()

        inicializarGif("https://media.tenor.com/3NP3M9aViooAAAAi/duck-waddling.gif")

        inicializarInteracciones()
    }

    private fun inicializarLateinits()
    {
        inputUser = findViewById(R.id.loginuser)
        inputPassword = findViewById(R.id.loginpassword)
        botonAceptar = findViewById(R.id.botonAceptar)
        botonCrearCuenta = findViewById(R.id.loginPulsaParaCrear)
//        botonSalir = findViewById(R.id.botonSalir)
        webViewGif = findViewById(R.id.webViewLogin)
    }

    //Función que inicializa el gif en el webview con la ruta que le pasamos
    private fun inicializarGif(ruta: String)
    {
        webViewGif.settings.cacheMode = WebSettings.LOAD_NO_CACHE // Deshabilita la caché
        @SuppressLint("SetJavaScriptEnabled")
        webViewGif.settings.javaScriptEnabled = true
        webViewGif.settings.useWideViewPort = true
        webViewGif.settings.loadWithOverviewMode = true
        webViewGif.webViewClient = WebViewClient()
        webViewGif.loadUrl(ruta)
    }

    //Función que inicializa las interacciones de los botones de la actividad
    private fun inicializarInteracciones()
    {
        //Boton aceptar comprueba el login
        botonAceptar.setOnClickListener{
            //Si el login es correcto se hace un intent de la clase MainMenu y le paso por put extra el nombre de usuario
            // si no lo es muestro una notificación con mensaje de error
            val usuarioCheck = comprobarLogin()
            if (usuarioCheck.id != 0)
            {
                val intent = Intent(this,MainMenu::class.java)
                Log.i("APLICACIONTEST:", "onCreate: se manda el usuario ${usuarioCheck.nombre} a la siguiente actividad")
                intent.putExtra("usuario",usuarioCheck)
                startActivity(intent)
            }
            else
            {
                val toast = Toast.makeText(this, R.string.inicio_de_sesion_incorrecto,Toast.LENGTH_SHORT)
                toast.show()
            }

        }

        //Boton crear cuenta crea un usuario en la base de datos y muestra un mensaje de bienvenida
        botonCrearCuenta.setOnClickListener{
            val intent = Intent(this,CrearCuentaActivity::class.java)
            startActivity(intent)
        }
    }



    private fun comprobarLogin(): Usuario {

        // Obtengo usuario y contraseña del input
        val nombreUser = inputUser.getText().toString()
        val passwordUser = inputPassword.getText().toString()

        // Si los campos no están vacíos compruebo el login en la base de datos
        if (nombreUser.isNotBlank() and passwordUser.isNotBlank())
        {
            conexion = DBConexion(this);
            db = conexion!!.writableDatabase

            val usuariocheck = Usuario(0,nombreUser,passwordUser,"".toByteArray())
            val usuarioCheked = conexion!!.checkUsuarioLogin(db,usuariocheck)
            Log.i("APLICACIONTEST:", "onCreate: se comprueba el usuario ${usuarioCheked.nombre} en la base de datos y se compara el nombre con el usuario ${usuariocheck.nombre}")
            //Si el nombre de usuario es igual al que devuelve la base de datos devuelvo el usuario
            if (usuariocheck.nombre == usuarioCheked.nombre)
            {
                return usuarioCheked
            }
        }
        //Si no devuelvo un usuario vacío
        return Usuario(0,"","", "".toByteArray())
    }


}