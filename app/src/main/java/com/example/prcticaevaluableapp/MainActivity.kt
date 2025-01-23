package com.example.prcticaevaluableapp

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.os.Handler
import android.util.ArrayMap
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.TopAppBarDefaults
import com.example.prcticaevaluableapp.DB.DBConexion
import com.example.prcticaevaluableapp.DB.DBManager
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {


    private lateinit var inputUser: EditText
    private lateinit var inputPassword: EditText

    private lateinit var botonAceptar: Button
    private lateinit var botonCrearCuenta: Button
//    private lateinit var botonSalir: Button

    private lateinit var textoDebug: TextView

    var conexion: DBConexion? = null
    var db: SQLiteDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        inputUser = findViewById(R.id.loginuser)
        inputPassword = findViewById(R.id.loginpassword)
        botonAceptar = findViewById(R.id.botonAceptar)
        botonCrearCuenta = findViewById(R.id.logBotonCrear)
//        botonSalir = findViewById(R.id.botonSalir)
        textoDebug = findViewById(R.id.textodebug)


        //Boton aceptar comprueba el login
        botonAceptar.setOnClickListener{
            //Si el login es correcto se hace un intent de la clase MainMenu y le paso por put extra el nombre de usuario
            // si no lo es muestro una notificación con mensaje de error
            val usuarioCheck = comprobarLogin()
            if (usuarioCheck.nombre.isNotBlank() and usuarioCheck.password.isNotBlank())
            {
                val intent = Intent(this,MainMenu::class.java)
                intent.putExtra("nombreUser",usuarioCheck.nombre)
                startActivity(intent)
            }
            else
            {
                val toast = Toast.makeText(this,R.string.inicio_de_sesion_incorrecto,Toast.LENGTH_SHORT)
                toast.show()
            }
        }

        //Boton crear cuenta crea un usuario en la base de datos y muestra un mensaje de bienvenida
        botonCrearCuenta.setOnClickListener{
            val intento = tryCrearCuenta()

            if (intento.nombre.isNotBlank() and intento.nombre.isNotBlank())
            {
                val toast = Toast.makeText(this,"¡Cuenta creada con exito, bienvenido ${intento.nombre}!", Toast.LENGTH_SHORT)
                toast.show()
            }
            else
            {
                val toast = Toast.makeText(this,"Error al crear cuenta, el usuario ${intento.nombre} ya existe en la base de datos.", Toast.LENGTH_SHORT)
                toast.show()
            }
        }

//        //Boton salir cierra la app
//        botonSalir.setOnClickListener{
//            exitProcess(0)
//        }
    }


    /**
     * Función que intenta crear una cuenta en la base de datos
     * @return Usuario
     */
    private fun tryCrearCuenta() : Usuario
    {
        conexion = DBConexion(this);
        db = conexion!!.writableDatabase

        val nombreUser = inputUser.getText().toString()
        val passwordUser = inputPassword.getText().toString()
        val usuario = Usuario(0,nombreUser,passwordUser)

        return conexion!!.crearUsuario(db,usuario)

    }

    /**
     * Función que comprueba el login de un usuario
     * @return Usuario
     */

    private fun comprobarLogin(): Usuario {

        // Obtengo usuario y contraseña del input
        val nombreUser = inputUser.getText().toString()
        val passwordUser = inputPassword.getText().toString()

        //En caso de que el usuario exista como clave en el diccionario, se comprueba la igualdad en la contraseña
        //en caso contrario se devuelve false sin comprobar la contraseña
        if (nombreUser.isNotBlank() and passwordUser.isNotBlank())
        {
            conexion = DBConexion(this);
            db = conexion!!.writableDatabase

            val usuariocheck = Usuario(0,nombreUser,passwordUser)
            val usuarioCheked = conexion!!.checkUsuarioLogin(db,usuariocheck)

            if (usuariocheck.nombre == usuarioCheked.nombre)
            {
                return usuariocheck
            }
        }
        return Usuario(0,"","")
    }


}