package com.example.prcticaevaluableapp

import android.app.Activity
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.prcticaevaluableapp.DB.DBConexion
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import java.io.ByteArrayOutputStream
import kotlin.properties.Delegates

class AddHobbieAppActivity : AppCompatActivity() {

    private lateinit var botonSalirSinGuardar: Button
    private lateinit var botonSalirYGuardar: Button
    private lateinit var botonCargarFoto: Button
    private lateinit var imageView: ImageView
    private lateinit var lblMensajeAddHobbie: MaterialTextView
    private lateinit var editText_nombreHobbie: TextInputEditText
    private lateinit var editText_descHobbie: TextInputEditText

    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>

    private val manejadorImagenes: ManejadorImagenes = ManejadorImagenes()

    private var idUsuario by Delegates.notNull<Int>()

    private lateinit var usuarioLogged : Usuario

    private var conexion: DBConexion? = null
    private var db: SQLiteDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_hobbie)
        lblMensajeAddHobbie = findViewById(R.id.lblMensajeAddHobbie)
        editText_nombreHobbie = findViewById(R.id.textfield_nombreHobbie)
        editText_descHobbie = findViewById(R.id.textfield_descHobbie)
        botonCargarFoto = findViewById(R.id.button_cargarImagen)
        imageView = findViewById(R.id.imgCreateHobbie)
        botonSalirYGuardar = findViewById(R.id.button_saveHobbie)
        botonSalirSinGuardar = findViewById(R.id.buttonDiscardHobbie)


        if (intent.hasExtra("usuario"))
        {
            usuarioLogged = intent.getSerializableExtra("usuario") as Usuario
            idUsuario = usuarioLogged.id
            lblMensajeAddHobbie.text = "Añadir un nuevo hobbie"
        }
        else idUsuario = 0

        if (intent.getBooleanExtra("isEdit", false)) {
            val hobbie = intent.getSerializableExtra("hobbie") as Hobbie
            editText_nombreHobbie.setText(hobbie.nombre)
            editText_descHobbie.setText(hobbie.descripcion)
            imageView.setImageBitmap(manejadorImagenes.byteArrayToBitmap(obtenerImagenHobbie(hobbie)))
            idUsuario = hobbie.idUsuario
            lblMensajeAddHobbie.text = "Editar hobbie ${hobbie.nombre}"
            botonSalirSinGuardar.text = "Descartar cambios"
        }

        botonSalirYGuardar.setOnClickListener {
            val nombreHobbie = editText_nombreHobbie.text.toString()
            val descHobbie = editText_descHobbie.text.toString()
            val drawable = imageView.drawable


            if (drawable == null || nombreHobbie.isEmpty() || descHobbie.isEmpty()) {
                Toast.makeText(this, "Por favor, rellena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            conexion = DBConexion(this)
            db = conexion!!.writableDatabase
            if (intent.getBooleanExtra("isEdit", false)) {
                val hobbie = intent.getSerializableExtra("hobbie") as Hobbie
                Log.i("DB", "Se procede a editar el hobbie: ${hobbie.id}, del usuario : ${hobbie.idUsuario}: ${hobbie.nombre} , ${hobbie.descripcion} , ${hobbie.imagen} ")
                conexion!!.editarHobbie(db, Hobbie(hobbie.id, hobbie.idUsuario, nombreHobbie, descHobbie, manejadorImagenes.imagenToByteArray(imageView)))
            } else {
                val hobbie = Hobbie(0, idUsuario, nombreHobbie, descHobbie, manejadorImagenes.imagenToByteArray(imageView))
                Log.i("DB", "Se procede a crear el hobbie: ${hobbie.id}, del usuario : ${hobbie.idUsuario}: ${hobbie.nombre} , ${hobbie.descripcion} , ${hobbie.imagen} ")
                conexion!!.crearHobbie(db, hobbie)

                val intent = Intent()
                setResult(Activity.RESULT_OK, intent)
            }

            finish()
        }

        // Registro del ActivityResultLauncher para la galería de imágenes
        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            // Procesar el resultado de la selección de la imagen de la galería
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val selectedImageUri: Uri? = data?.data
                manejadorImagenes
                imageView.setImageURI(selectedImageUri)
            }
        }

        botonCargarFoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            // Seleccionar solo imágenes
            intent.type = "image/*"
            // Lanzar la galería de imágenes con el ActivityResultLauncher
            galleryLauncher.launch(intent)
        }

        botonSalirSinGuardar.setOnClickListener {
            val intent = Intent()
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        }

    }

    private fun obtenerImagenHobbie(hobbie: Hobbie) : ByteArray {
        conexion = DBConexion(this);
        db = conexion!!.writableDatabase
        val geHobbie = conexion!!.obtenerHobbiePorId(db, hobbie.id)
        return geHobbie.imagen
    }
}