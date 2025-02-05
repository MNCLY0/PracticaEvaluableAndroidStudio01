package com.example.prcticaevaluableapp

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.palette.graphics.Palette
import com.example.prcticaevaluableapp.DB.DBConexion
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText

class HobbieInfoActivity : AppCompatActivity() {

//    lateinit var textViewNombreHobbie : MaterialTextView
    private lateinit var textViewDescHobbie : TextInputEditText
    private lateinit var imageviewHobbie : ImageView
    private lateinit var botonEditar : FloatingActionButton
    private lateinit var botonBorrar : FloatingActionButton
    private lateinit var toolbar: Toolbar
    private lateinit var hobbie : Hobbie
    private var conexion: DBConexion? = null
    private var db: SQLiteDatabase? = null
    private val manejadorImagenes: ManejadorImagenes = ManejadorImagenes()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hobbieinfo)

        inicializarLateinits()

        textViewDescHobbie.setText(hobbie.descripcion)
        toolbar.setTitle("Hobbie: ${hobbie.nombre}")

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        inicializarImagen()

        inicializarInteracciones()

    }

    private fun inicializarLateinits()
    {
        toolbar = findViewById(R.id.toolbarHobbieInfo)
//        textViewNombreHobbie = findViewById(R.id.textViewNombreHobbie)
        imageviewHobbie = findViewById(R.id.imageViewHobbie)
        textViewDescHobbie = findViewById(R.id.textViewDescripcion)
        botonEditar = findViewById(R.id.floatingButtonEditHobbie)
        botonBorrar = findViewById(R.id.floatingButtonDeleteHobbie)


        hobbie = intent.getSerializableExtra("hobbie") as Hobbie
    }


    // Obtenemos la imagen del hobbie a partir de su ID
    private fun obtenerImagenHobbie(hobbie: Hobbie) : ByteArray {
        conexion = DBConexion(this)
        db = conexion!!.writableDatabase
        val geHobbie = conexion!!.obtenerHobbiePorId(db, hobbie.id)
        return geHobbie.imagen
    }

    override fun onResume() {
        super.onResume()
        actualizarInfoHobbie()
//        textViewNombreHobbie.text = hobbie.nombre
        textViewDescHobbie.setText(hobbie.descripcion)
        val imagenBitmap = manejadorImagenes.byteArrayToBitmap(hobbie.imagen)
        imageviewHobbie.setImageBitmap(imagenBitmap)
    }

    private fun actualizarInfoHobbie() {
        conexion = DBConexion(this)
        db = conexion!!.writableDatabase
        hobbie = conexion!!.obtenerHobbiePorId(db, hobbie.id)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Si se pulsa el botón de retroceso, cerramos la actividad
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    // Inicializamos la imagen del hobbie en el ImageView
    private fun inicializarImagen()
    {
        val imagenBitmap = manejadorImagenes.byteArrayToBitmap(obtenerImagenHobbie(hobbie))
        imageviewHobbie.setImageBitmap(imagenBitmap)
        // Comprobamos si la imagen está vacía
        if (imagenBitmap.byteCount > 0) {
            // Generamos una Palette a partir del Bitmap para extraer el color predominante
            Palette.from(imagenBitmap).generate { palette ->
                // Obtenemos el color predominante de la Palette, o usamos un color predeterminado del tema si no está disponible
                val dominantColor = palette?.getDominantColor(this.baseContext.getColor(R.color.md_theme_background))
                // Establecemos el color de fondo del ImageView al color predominante
                imageviewHobbie.setBackgroundColor(dominantColor ?: this.baseContext.getColor(R.color.md_theme_background))
            }
        }
    }

    private fun inicializarInteracciones()
    {
        // Botón encargado de llamar a la actividad de edición de hobbie
        botonEditar.setOnClickListener {
            val intent = Intent(this, AddHobbieAppActivity::class.java)
            intent.putExtra("isEdit", true)
            intent.putExtra("hobbie", Hobbie(hobbie.id, hobbie.idUsuario, hobbie.nombre, hobbie.descripcion, "".toByteArray()))
            startActivity(intent)
        }
        //Boton encargado de borrar el hobbie
        botonBorrar.setOnClickListener {
            // Mostramos un diálogo de confirmación antes de borrar el hobbie para tener una confirmación
            // de esta manera nos aseguramos que no se borre por error
            AlertDialog.Builder(this).apply {
                setTitle("Confirmar borrado")
                setMessage("¿Estás seguro de que deseas borrar el hobbie ${hobbie.nombre}?")
                setPositiveButton("Sí") { _, _ ->
                    conexion = DBConexion(this@HobbieInfoActivity)
                    db = conexion!!.writableDatabase
                    conexion!!.borrarHobbie(db, hobbie)
                    finish()
                }
                setNegativeButton("No", null)
            }.show()
        }
    }



}
