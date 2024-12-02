package com.example.prcticaevaluableapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter


class ControladorVentanasDeslizantes(supportFragmentManager: FragmentManager) : FragmentStatePagerAdapter(supportFragmentManager)  {

   private val listaFragmentos = ArrayList<Fragment>()
   private val listaTitulosFragmentos = ArrayList<String>()

    fun addFragment(fragment: Fragment, tittle: String)
    {
        listaFragmentos.add(fragment)
        listaTitulosFragmentos.add(tittle)
    }

    override fun getCount(): Int {
        return listaFragmentos.count()
    }

    override fun getItem(position: Int): Fragment {
        return listaFragmentos[position]
    }

    override fun getPageTitle(position: Int): CharSequence {
        return  listaTitulosFragmentos[position]
    }

}
