package com.example.prcticaevaluableapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AddHobbieAppActivity : AppCompatActivity() {

    private lateinit var botonAceptar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_hobbie)

        botonAceptar = findViewById(R.id.btnAceptar)

        botonAceptar.setOnClickListener{
            val intent = Intent()
            setResult(Activity.RESULT_OK,intent)
            finish()
        }

    }




}