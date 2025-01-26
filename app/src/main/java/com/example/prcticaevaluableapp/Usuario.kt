package com.example.prcticaevaluableapp

class Usuario(
    val id:Int, val nombre:String, val password:String):java.io.Serializable
{
    var hobbies = ArrayList<Hobbie>()

    fun cargarHobbies(hobbies:ArrayList<Hobbie>) : Boolean
    {
        //Si la lista de hobbies esta vacia devuelvo false
        if (hobbies.isEmpty()) return false

        //Si no esta vacia la cargo en el usuario
        this.hobbies = hobbies
        return true
    }

}