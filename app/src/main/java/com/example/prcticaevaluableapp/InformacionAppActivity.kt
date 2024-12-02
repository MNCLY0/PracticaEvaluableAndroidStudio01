package com.example.prcticaevaluableapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class InformacionAppActivity : AppCompatActivity() {

    private lateinit var botonAceptar: Button
    private lateinit var botonCancelar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informacion_app)

        botonAceptar = findViewById(R.id.btnAceptar)

        botonAceptar.setOnClickListener{
            val intent = Intent()
            setResult(Activity.RESULT_OK,intent)
            finish()
        }

    }




}