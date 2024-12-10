package com.example.prcticaevaluableapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.ArrayMap
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private lateinit var users : ArrayMap<String,String>

    private lateinit var inputUser: EditText
    private lateinit var inputPassword: EditText

    private lateinit var botonAceptar: Button
    private lateinit var botonSalir: Button

    private lateinit var textoDebug: TextView


    override fun onCreate(savedInstanceState: Bundle?) {

        //Creo un diccionario
        users = ArrayMap()

        //Añado usuario (clave) y contraseña (valor) al diccionario
        users["Yunaiber"] = "123456"
        users["Usuario"] = "123456"
        users["Angela"] = "123456"
        users["Antonia"] = "123456"
        users["Manuel"] = "123456"

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        inputUser = findViewById(R.id.loginuser)
        inputPassword = findViewById(R.id.loginpassword)
        botonAceptar = findViewById(R.id.botonAceptar)
        botonSalir = findViewById(R.id.botonSalir)
        textoDebug = findViewById(R.id.textodebug)

        botonAceptar.setOnClickListener{
            //Si el login es correcto se hace un intent de la clase MainMenu y le paso por put extra el nombre de usuario
            // si no lo es muestro una notificación con mensaje de error
            if (comprobarLogin())
            {
                val intent = Intent(this,MainMenu::class.java)
                intent.putExtra("nombreUser",inputUser.getText().toString())
                startActivity(intent)
            }
            else
            {
                val toast = Toast.makeText(this,R.string.inicio_de_sesion_incorrecto,Toast.LENGTH_LONG)
                toast.show()
            }
        }
        //Boton salir cierra la app
        botonSalir.setOnClickListener{
            exitProcess(0)
        }
    }

    private fun comprobarLogin(): Boolean {

        // Obtengo usuario y contraseña del input
        val nombreUser = inputUser.getText().toString()
        val passwordUser = inputPassword.getText().toString()

        //En caso de que el usuario exista como clave en el diccionario, se comprueba la igualdad en la contraseña
        //en caso contrario se devuelve false sin comprobar la contraseña
        if (users[nombreUser] != null)
        {
            return users[nombreUser] == passwordUser
        }
        return false
    }


}