package com.example.prcticaevaluableapp

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prcticaevaluableapp.DB.DBConexion
import com.google.android.material.floatingactionbutton.FloatingActionButton


class HobbiesFragment : Fragment() {

    protected var mAdapter: ControladorRecyclerViewHobbies? = null
    private lateinit var botonAddHobbie: FloatingActionButton
    lateinit var recyclerView : RecyclerView
    private var conexion: DBConexion? = null
    private var db: SQLiteDatabase? = null

    private lateinit var listaHobbies : ArrayList<Hobbie>

    private lateinit var usuarioLogged : Usuario

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.fragment_hobbies, container, false)

        inicializarLateinits(rootView)

        iniciarRecogidaDatos()

        inicializarInteracciones()
        return rootView
    }

    private fun inicializarLateinits(rootView : View)
    {
        usuarioLogged = arguments?.getSerializable("usuario") as Usuario
        recyclerView = rootView.findViewById(R.id.recyclerViewHobbies)
        recyclerView.layoutManager = LinearLayoutManager(context)
        botonAddHobbie = rootView.findViewById(R.id.floatingButtonAddHobby)
    }

    override fun onResume() {
        iniciarRecogidaDatos()
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        botonAddHobbie = view.findViewById(R.id.floatingButtonAddHobby)
        botonAddHobbie.setOnClickListener {
            lanzarAddHobbie()
        }
    }

    private fun lanzarAddHobbie(view: View? = null) {
        val i = Intent(this.context, AddHobbieAppActivity::class.java)
        i.putExtra("usuario", usuarioLogged)
        startActivity(i)
    }

    private fun iniciarRecogidaDatos()
    {
        if (usuarioLogged.id != -1)
        {
            conexion = DBConexion(this.context);
            db = conexion!!.writableDatabase
            val hobbies = conexion!!.obtenerHobbiesUsuario(db,usuarioLogged)
            listaHobbies = hobbies

            mAdapter = ControladorRecyclerViewHobbies(listaHobbies)
            recyclerView.adapter = mAdapter

        }
    }

    private fun inicializarInteracciones()
    {
        botonAddHobbie.setOnClickListener{
            val intent = Intent(context,AddHobbieAppActivity::class.java)
            startActivity(intent)
        }
    }
}