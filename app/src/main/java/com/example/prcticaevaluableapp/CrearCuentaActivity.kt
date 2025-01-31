package com.example.prcticaevaluableapp

import android.app.Activity
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.prcticaevaluableapp.DB.DBConexion
import com.example.prcticaevaluableapp.ui.theme.PrácticaEvaluableAppTheme
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class CrearCuentaActivity : ComponentActivity() {

    lateinit var imagenCuenta : ShapeableImageView
    lateinit var nombreUsuario: TextInputEditText
    lateinit var passwordUsuario: TextInputEditText
    lateinit var botonCargarImagen: MaterialButton
    lateinit var botonCrearCuenta: MaterialButton
    lateinit var botonCancelar: MaterialButton

    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private val manejadorImagenes: ManejadorImagenes = ManejadorImagenes()

    var conexion: DBConexion? = null
    var db: SQLiteDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_cuenta)
        imagenCuenta = findViewById(R.id.fotoPerfilCrearCuenta)
        nombreUsuario = findViewById(R.id.loginuser)
        passwordUsuario = findViewById(R.id.loginpassword)
        botonCargarImagen = findViewById(R.id.loginBotonCargarImagen)
        botonCrearCuenta = findViewById(R.id.botonCrearCuenta)
        botonCancelar = findViewById(R.id.botonCancelar)

        // Registro del ActivityResultLauncher para la galería de imágenes
        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            // Procesar el resultado de la selección de la imagen de la galería
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val selectedImageUri: Uri? = data?.data
                imagenCuenta.setImageURI(selectedImageUri)
            }
        }

        botonCargarImagen.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            // Seleccionar solo imágenes
            intent.type = "image/*"
            // Lanzar la galería de imágenes con el ActivityResultLauncher
            galleryLauncher.launch(intent)
        }


        //Boton crear cuenta crea un usuario en la base de datos y muestra un mensaje de bienvenida
        botonCrearCuenta.setOnClickListener{

            if (nombreUsuario.getText().toString().trim().isBlank() or passwordUsuario.getText().toString().trim().isBlank() or (imagenCuenta.drawable == null))
            {
                val toast = Toast.makeText(this,"Error al crear cuenta, los campos no pueden estar vacios.", Toast.LENGTH_SHORT)
                toast.show()
            }
            else
            {
                val intento = tryCrearCuenta()

                if (intento.nombre.isNotBlank() and intento.nombre.isNotBlank())
                {
                    val toast = Toast.makeText(this,"¡Cuenta creada con exito, bienvenido ${intento.nombre}!", Toast.LENGTH_SHORT)
                    toast.show()
                    finish()
                }
                else
                {
                    val toast = Toast.makeText(this,"Error al crear cuenta, el usuario ${intento.nombre} ya existe en la base de datos.", Toast.LENGTH_SHORT)
                    toast.show()
                }
            }
        }
    }

    private fun tryCrearCuenta() : Usuario
    {
        conexion = DBConexion(this);
        db = conexion!!.writableDatabase

        val nombreUser = nombreUsuario.getText().toString().trim()
        val passwordUser = passwordUsuario.getText().toString().trim()
        val usuario = Usuario(0,nombreUser,passwordUser,manejadorImagenes.imagenToByteArray(imagenCuenta))

        return conexion!!.crearUsuario(db,usuario)
    }

    }
