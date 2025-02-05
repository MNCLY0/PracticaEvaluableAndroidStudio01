package com.example.prcticaevaluableapp

import android.app.Activity
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
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
import kotlin.properties.Delegates

class AddHobbieAppActivity : AppCompatActivity() {

    private lateinit var botonSalirSinGuardar: Button
    private lateinit var botonSalirYGuardar: Button
    private lateinit var botonCargarFoto: Button
    private lateinit var imageView: ImageView
    private lateinit var lblMensajeAddHobbie: MaterialTextView
    private lateinit var editTextnombreHobbie: TextInputEditText
    private lateinit var edittextDeschobbie: TextInputEditText

    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>

    private val manejadorImagenes: ManejadorImagenes = ManejadorImagenes()

    private var idUsuario by Delegates.notNull<Int>()

    private lateinit var usuarioLogged : Usuario

    private var conexion: DBConexion? = null
    private var db: SQLiteDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_hobbie)

        inicializarLateinits()

        realizarComprobaciones()

        registroGaleria()

        inicializarInteracciones()

    }

    private fun inicializarLateinits()
    {
        lblMensajeAddHobbie = findViewById(R.id.lblMensajeAddHobbie)
        editTextnombreHobbie = findViewById(R.id.textfield_nombreHobbie)
        edittextDeschobbie = findViewById(R.id.textfield_descHobbie)
        botonCargarFoto = findViewById(R.id.button_cargarImagen)
        imageView = findViewById(R.id.imgCreateHobbie)
        botonSalirYGuardar = findViewById(R.id.button_saveHobbie)
        botonSalirSinGuardar = findViewById(R.id.buttonDiscardHobbie)
    }

    private fun inicializarInteracciones()
    {
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
        //Si se pulsa el botón de guardar, se comprueban los campos y se añade el hobbie a la base de datos o se edita
        botonSalirYGuardar.setOnClickListener {
            val nombreHobbie = editTextnombreHobbie.text.toString()
            val descHobbie = edittextDeschobbie.text.toString()
            val drawable = imageView.drawable

            //Se comprueba que los campos no estén vacíos
            if (drawable == null || nombreHobbie.isEmpty() || descHobbie.isEmpty()) {
                Toast.makeText(this, "Por favor, rellena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            conexion = DBConexion(this)
            db = conexion!!.writableDatabase
            // Se comprueba si estamos editando un hobbie o añadiendo uno nuevo y se procede en consecuencia
            if (intent.getBooleanExtra("isEdit", false)) {
                //En caso de estar editando un hobbie, se obtiene el hobbie a editar y se procede a editarlo
                val hobbie = intent.getSerializableExtra("hobbie") as Hobbie
                Log.i("DB", "Se procede a editar el hobbie: ${hobbie.id}, del usuario : ${hobbie.idUsuario}: ${hobbie.nombre} , ${hobbie.descripcion} , ${hobbie.imagen} ")
                conexion!!.editarHobbie(db, Hobbie(hobbie.id, hobbie.idUsuario, nombreHobbie, descHobbie, manejadorImagenes.imagenToByteArray(imageView)))
            } else {
                // En caso de estar añadiendo un nuevo hobbie, se procede a añadirlo
                val hobbie = Hobbie(0, idUsuario, nombreHobbie, descHobbie, manejadorImagenes.imagenToByteArray(imageView))
                Log.i("DB", "Se procede a crear el hobbie: ${hobbie.id}, del usuario : ${hobbie.idUsuario}: ${hobbie.nombre} , ${hobbie.descripcion} , ${hobbie.imagen} ")
                conexion!!.crearHobbie(db, hobbie)

                val intent = Intent()
                setResult(Activity.RESULT_OK, intent)
            }

            finish()
        }
    }

    private fun registroGaleria()
    {
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
    }

    private fun realizarComprobaciones()
    {
        //Si el intent tiene un extra llamado usuario, se obtiene el usuario que ha iniciado sesión (siempre va a tener este extra a no ser
        // que estemos editando un hobbie, en cuyo caso no se necesita el usuario que ha iniciado sesión)
        if (intent.hasExtra("usuario"))
        {
            usuarioLogged = intent.getSerializableExtra("usuario") as Usuario
            idUsuario = usuarioLogged.id
            lblMensajeAddHobbie.text = "Añadir un nuevo hobbie"
        }
        else idUsuario = 0


        //El intent puede tener un extra llamado isEdit que indica si se va a editar un hobbie o se va a añadir uno nuevo
        // en caso de que se vaya a editar un hobbie, se rellenan los campos con los datos del hobbie a editar
        if (intent.getBooleanExtra("isEdit", false)) {
            val hobbie = intent.getSerializableExtra("hobbie") as Hobbie
            editTextnombreHobbie.setText(hobbie.nombre)
            edittextDeschobbie.setText(hobbie.descripcion)
            imageView.setImageBitmap(manejadorImagenes.byteArrayToBitmap(obtenerImagenHobbie(hobbie)))
            idUsuario = hobbie.idUsuario
            lblMensajeAddHobbie.text = "Editar hobbie ${hobbie.nombre}"
            botonSalirSinGuardar.text = "Descartar cambios"
        }
    }



    private fun obtenerImagenHobbie(hobbie: Hobbie) : ByteArray {
        conexion = DBConexion(this)
        db = conexion!!.writableDatabase
        val geHobbie = conexion!!.obtenerHobbiePorId(db, hobbie.id)
        return geHobbie.imagen
    }
}