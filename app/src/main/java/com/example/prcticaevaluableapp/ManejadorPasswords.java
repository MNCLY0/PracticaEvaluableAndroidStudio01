package com.example.prcticaevaluableapp;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ManejadorPasswords {
    //Este método recibe una contraseña y la hashea con el algoritmo MD5
    //Para esto ututiliza la clase MessageDigest de Java
    public static String hashPassword(String password) {
        try {
            //Se crea un objeto de la clase MessageDigest con el algoritmo MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Se hashea la contraseña y se guarda en un array de bytes
            byte[] hashInBytes = md.digest(password.getBytes());
            //Se convierte el array de bytes a un string en hexadecimal
            StringBuilder sb = new StringBuilder();
            //Se recorre el array de bytes y se convierte a hexadecimal
            for (byte b : hashInBytes) {
                sb.append(String.format("%02x", b));
            }
            //Se retorna el string en hexadecimal
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
