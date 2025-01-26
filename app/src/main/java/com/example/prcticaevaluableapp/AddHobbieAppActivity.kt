package com.example.prcticaevaluableapp

import android.app.Activity
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.prcticaevaluableapp.DB.DBConexion
import com.google.android.material.textfield.TextInputEditText
import java.io.ByteArrayOutputStream

class AddHobbieAppActivity : AppCompatActivity() {

    private lateinit var botonSalirSinGuardar: Button
    private lateinit var botonSalirYGuardar: Button
    private lateinit var botonCargarFoto: Button
    private lateinit var imageView: ImageView
    private lateinit var editText_nombreHobbie: TextInputEditText
    private lateinit var editText_descHobbie: TextInputEditText
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>

    private lateinit var usuarioLogged : Usuario

    private var conexion: DBConexion? = null
    private var db: SQLiteDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_hobbie)

        editText_nombreHobbie = findViewById(R.id.textfield_nombreHobbie)
        editText_descHobbie = findViewById(R.id.textfield_descHobbie)

        usuarioLogged = intent.getSerializableExtra("usuario") as Usuario
        botonCargarFoto = findViewById(R.id.button_cargarImagen)
        imageView = findViewById(R.id.imgCreateHobbie)
        botonSalirYGuardar = findViewById(R.id.button_saveHobbie)

        // Registro del ActivityResultLauncher para la galería de imágenes
        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            // Procesar el resultado de la selección de la imagen de la galería
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val selectedImageUri: Uri? = data?.data
                imageView.setImageURI(selectedImageUri)
            }
        }

        botonSalirYGuardar.setOnClickListener {

            conexion = DBConexion(this);
            db = conexion!!.writableDatabase

            val hobbie = Hobbie(0, usuarioLogged.id, editText_nombreHobbie.text.toString(), editText_descHobbie.text.toString(), ByteArray(0))
            Log.i("DB", "Se procede a crear el hobbie: ${hobbie.id}, del usuario : ${hobbie.idUsuario}: ${hobbie.nombre} , ${hobbie.descripcion} , ${hobbie.imagen} ")
            conexion!!.crearHobbie(db, Hobbie(0, usuarioLogged.id, editText_nombreHobbie.text.toString(), editText_descHobbie.text.toString(), imagenAByteArray(imageView)))

            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        botonCargarFoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            // Seleccionar solo imágenes
            intent.type = "image/*"
            // Lanzar la galería de imágenes con el ActivityResultLauncher
            galleryLauncher.launch(intent)
        }

        botonSalirSinGuardar = findViewById(R.id.buttonDiscardHobbie)
        botonSalirSinGuardar.setOnClickListener {
            val intent = Intent()
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        }
    }

    private fun imagenAByteArray(imageView: ImageView): ByteArray {
        Log.d("imagenAByteArray", "Convirtiendo imagen a byte array")
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        Log.d("imagenAByteArray", "Bitmap obtenido: $bitmap")
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        Log.d("imagenAByteArray", "Imagen comprimida")
        val byteArray = stream.toByteArray()
        Log.d("imagenAByteArray", "Byte array generado: ${byteArray.size} bytes")
        return byteArray
    }
}