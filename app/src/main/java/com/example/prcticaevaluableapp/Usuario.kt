package com.example.prcticaevaluableapp

data class Usuario(
    val id:Int,
    var nombre:String,
    val password:String,
    var imagen:ByteArray):java.io.Serializable
