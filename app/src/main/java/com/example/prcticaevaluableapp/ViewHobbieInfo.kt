package com.example.prcticaevaluableapp

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.palette.graphics.Palette
import com.example.prcticaevaluableapp.DB.DBConexion
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textview.MaterialTextView

class ViewHobbieInfo : AppCompatActivity() {

//    lateinit var textViewNombreHobbie : MaterialTextView
    lateinit var textViewDescHobbie : MaterialTextView
    lateinit var imageviewHobbie : ImageView
    lateinit var botonEditar : FloatingActionButton
    lateinit var botonBorrar : FloatingActionButton
    lateinit var toolbar: Toolbar
    lateinit var hobbie : Hobbie
    private var conexion: DBConexion? = null
    private var db: SQLiteDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hobbieinfo)
        toolbar = findViewById(R.id.toolbarHobbieInfo)
//        textViewNombreHobbie = findViewById(R.id.textViewNombreHobbie)
        imageviewHobbie = findViewById(R.id.imageViewHobbie)
        textViewDescHobbie = findViewById(R.id.textViewDescripcion)
        botonEditar = findViewById(R.id.floatingButtonEditHobbie)
        botonBorrar = findViewById(R.id.floatingButtonDeleteHobbie)





        hobbie = intent.getSerializableExtra("hobbie") as Hobbie

//        textViewNombreHobbie.text = hobbie.nombre
        textViewDescHobbie.text = hobbie.descripcion
        val imagenBitmap = imagenBitmap(obtenerImagenHobbie(hobbie))
        imageviewHobbie.setImageBitmap(imagenBitmap)
        toolbar.setTitle("Hobbie: ${hobbie.nombre}")

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }



        // Comprobamos si la imagen está vacía
        if (hobbie.imagen.isNotEmpty()) {
            // Generamos una Palette a partir del Bitmap para extraer el color predominante
            Palette.from(imagenBitmap).generate { palette ->
                // Obtenemos el color predominante de la Palette, o usamos un color predeterminado del tema si no está disponible
                val dominantColor = palette?.getDominantColor(this.baseContext.getColor(R.color.md_theme_background))
                // Establecemos el color de fondo del ImageView al color predominante
                imageviewHobbie.setBackgroundColor(dominantColor ?: this.baseContext.getColor(R.color.md_theme_background))
            }
        }

        botonEditar.setOnClickListener {
            val intent = Intent(this, AddHobbieAppActivity::class.java)
            intent.putExtra("isEdit", true)
            intent.putExtra("hobbie", Hobbie(hobbie.id, hobbie.idUsuario, hobbie.nombre, hobbie.descripcion, "".toByteArray()))
            startActivity(intent)
        }

        botonBorrar.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("Confirmar borrado")
                setMessage("¿Estás seguro de que deseas borrar el hobbie ${hobbie.nombre}?")
                setPositiveButton("Sí") { _, _ ->
                    conexion = DBConexion(this@ViewHobbieInfo)
                    db = conexion!!.writableDatabase
                    conexion!!.borrarHobbie(db, hobbie)
                    finish()
                }
                setNegativeButton("No", null)
            }.show()
        }

    }

    private fun obtenerImagenHobbie(hobbie: Hobbie) : ByteArray {
        conexion = DBConexion(this);
        db = conexion!!.writableDatabase
        val geHobbie = conexion!!.obtenerHobbiePorId(db, hobbie.id)
        return geHobbie.imagen
    }

    override fun onResume() {
        super.onResume()
        actualizarInfoHobbie()
//        textViewNombreHobbie.text = hobbie.nombre
        textViewDescHobbie.text = hobbie.descripcion
        val imagenBitmap = imagenBitmap(hobbie.imagen)
        imageviewHobbie.setImageBitmap(imagenBitmap)
    }

    private fun actualizarInfoHobbie() {
        conexion = DBConexion(this);
        db = conexion!!.writableDatabase
        hobbie = conexion!!.obtenerHobbiePorId(db, hobbie.id)
    }

    private fun imagenBitmap(imagen: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(imagen, 0, imagen.size)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Si se pulsa el botón de retroceso, cerramos la actividad
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }



}
