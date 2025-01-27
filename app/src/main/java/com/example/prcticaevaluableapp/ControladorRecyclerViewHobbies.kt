package com.example.prcticaevaluableapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView

class ControladorRecyclerViewHobbies(private val listaHobbies: ArrayList<Hobbie>) : RecyclerView.Adapter<VistaHobbie>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VistaHobbie {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.hobbie_item_layout,parent,false)
        return VistaHobbie(view)
    }

    override fun getItemCount(): Int {
        return listaHobbies.size
    }

    override fun onBindViewHolder(holder: VistaHobbie, position: Int) {
        val hobbie = listaHobbies[position]
        holder.titulo?.text = hobbie.nombre

        holder.itemView.setOnClickListener{
            // Creamos un Intent para abrir la actividad de detalle del hobbie
            val intent = Intent(holder.itemView.context, ViewHobbieInfo::class.java)
            // Pasamos el hobbie a la actividad de detalle
            intent.putExtra("hobbie", Hobbie(hobbie.id,hobbie.idUsuario, hobbie.nombre, hobbie.descripcion, "".toByteArray()))
            // Iniciamos la actividad de detalle
            holder.itemView.context.startActivity(intent)
            Log.i("APLICACIONTEST:", "onBindViewHolder: Se ha pulsado el hobbie ${hobbie.nombre}")
        }

        // Comprobamos si hay un salto de línea en la descripción
        //Esto lo hacemos para que no se muestre el salto de línea en la descripción y solo se muestre la primerar parte de la descripción
        val index = hobbie.descripcion.indexOf('\n')
        if (index != -1) {
            holder.descripcion?.text = hobbie.descripcion.substring(0, index) + "..."
        } else {
            holder.descripcion?.text = hobbie.descripcion
        }


        // Comprobamos si la imagen está vacía
        if (hobbie.imagen.isEmpty()) {
            // Establecemos una imagen predeterminada si la imagen está vacía
            holder.foto?.setImageResource(R.drawable.ic_launcher_background)
        } else {
            // Convertimos el array de bytes de la imagen a un Bitmap
            val bitmap = imagenBitmap(hobbie.imagen)
            // Establecemos el Bitmap en el ImageView
            holder.foto?.setImageBitmap(bitmap)
            // Generamos una Palette a partir del Bitmap para extraer el color predominante
            Palette.from(bitmap).generate { palette ->
                // Obtenemos el color predominante de la Palette, o usamos un color predeterminado del tema si no está disponible
                val dominantColor = palette?.getDominantColor(holder.itemView.context.getColor(R.color.md_theme_background))
                // Establecemos el color de fondo del ImageView al color predominante
                holder.foto?.setBackgroundColor(dominantColor ?: holder.itemView.context.getColor(R.color.md_theme_background))
            }
        }
    }

    private fun imagenBitmap(imagen: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(imagen, 0, imagen.size)

    }
}

    class VistaHobbie(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val titulo : TextView? = itemView?.findViewById(R.id.itemHobbieTitle)
        val descripcion : TextView? = itemView?.findViewById(R.id.itemHobbieDescription)
        val foto : ImageView? = itemView?.findViewById(R.id.itemHobbieImage)
    }
