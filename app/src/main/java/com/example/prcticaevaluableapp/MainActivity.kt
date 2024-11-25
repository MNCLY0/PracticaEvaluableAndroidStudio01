package com.example.prcticaevaluableapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var inputUser: EditText
    private lateinit var inputPassword: EditText

    private lateinit var botonAceptar: Button

    private lateinit var textoDebug: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        inputUser = findViewById(R.id.loginuser)
        inputPassword = findViewById(R.id.loginpassword)
        botonAceptar = findViewById(R.id.botonAceptar)
        textoDebug = findViewById(R.id.textodebug)

        botonAceptar.setOnClickListener{
            if (comprobarLogin())
            {
                textoDebug.setText("Inicio de sesion correcto")
            }
            else
            {
                textoDebug.setText("Inicio de sesion incorrecto")
            }
        }

    }

    private fun comprobarLogin(): Boolean {

        val nombreUser = inputUser.getText()
        val passwordUser = inputUser.getText()

        return nombreUser.equals(R.string.usuario) && passwordUser.equals(R.string.password)
    }


}