package com.example.prcticaevaluableapp

import android.content.Intent
import android.os.Bundle
import android.util.ArrayMap
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var users : ArrayMap<String,String>

    private lateinit var inputUser: EditText
    private lateinit var inputPassword: EditText

    private lateinit var botonAceptar: Button

    private lateinit var textoDebug: TextView


    override fun onCreate(savedInstanceState: Bundle?) {

        users = ArrayMap()

        users["Yunaiber"] = "123456"
        users["Angela"] = "123456"
        users["Antonia"] = "123456"
        users["Manuel"] = "123456"

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        inputUser = findViewById(R.id.loginuser)
        inputPassword = findViewById(R.id.loginpassword)
        botonAceptar = findViewById(R.id.botonAceptar)
        textoDebug = findViewById(R.id.textodebug)

        botonAceptar.setOnClickListener{
            if (comprobarLogin())
            {
                val intent = Intent(this,MainMenu::class.java)
                intent.putExtra("nombreUser",inputUser.getText().toString())
                startActivity(intent)
            }
            else
            {
                textoDebug.text = getString(R.string.inicio_de_sesion_incorrecto)
            }
        }
    }

    private fun comprobarLogin(): Boolean {

        val nombreUser = inputUser.getText().toString()
        val passwordUser = inputPassword.getText().toString()

        if (users[nombreUser] != null)
        {
            return users[nombreUser] == passwordUser
        }
        return false
    }


}