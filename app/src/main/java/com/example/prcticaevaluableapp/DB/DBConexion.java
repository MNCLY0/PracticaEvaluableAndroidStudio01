package com.example.prcticaevaluableapp.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.prcticaevaluableapp.Hobbie;
import com.example.prcticaevaluableapp.Usuario;

import java.util.ArrayList;
import java.util.Arrays;

public class DBConexion extends SQLiteOpenHelper {

    private static final String DB_NAME =  "aplicacionDB";
    private static final int DB_VERSION = 3;
    //    Tabla contactos
    public static final String TABLA_USUARIO = "usuario";
    public static final String TABLA_HOBBIE = "hobbie";
    public static final String USUARIO_ID = "_id";
    public static final String USUARIO_NOMBRE = "nombre";
    public static final String USUARIO_PASSWORD = "password";
    public static final String SENTENCIA_SELECCION_CONTACTOS = "select _id, nombre, password" +
            "from usuario";

    public static final String SENTENCIA_CREACION_TABLA_USUARIO = "create table usuario " +
            "(_id integer primary key autoincrement, nombre text not null, password text not null);";

    public static final String SENTENCIA_CREACION_TABLA_HOBBIE = "create table hobbie " +
            "(_id integer primary key autoincrement, " +
            "nombre text not null, " +
            "descripcion text, " +
            "foto blob, " +
            "idUsuario integer, " +
            "foreign key (idUsuario) references usuario(_id));";

    public DBConexion(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        codigo sql
//        instanciamos y creamos la base de datos
//        este codigo se ejectua cuando se crea la base de datos
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_USUARIO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_HOBBIE);
        db.execSQL(SENTENCIA_CREACION_TABLA_USUARIO);
        db.execSQL(SENTENCIA_CREACION_TABLA_HOBBIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//      codigo sql para actualizacion de la base de datos
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_USUARIO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_HOBBIE);
        onCreate(db);
    }


    public Usuario checkUsuarioLogin(SQLiteDatabase db, Usuario usuario) {
        String query = "select _id, nombre, password from usuario where nombre = ?";
        Cursor c = db.rawQuery(query, new String[]{usuario.getNombre()});
        if (c.moveToFirst()) {
            @SuppressLint("Range") String realPassword = c.getString(c.getColumnIndex("password"));
            if (usuario.getPassword().equals(realPassword))
            {
                @SuppressLint("Range") String nombre = c.getString(c.getColumnIndex("nombre"));
                @SuppressLint("Range") int id = c.getInt(c.getColumnIndex("_id"));
                return new Usuario(id,nombre,"");
            }
        }
        c.close();
        return new Usuario(0,"","");
    }

    //Comprobamos la existencia del usuario
    public boolean checkUsuarioExists(SQLiteDatabase db, Usuario usuario)
    {
        String query = "select _id, nombre, password from usuario where nombre = ?";
        Cursor c = db.rawQuery(query, new String[]{usuario.getNombre()});
        return c.moveToFirst();
    }

    public Usuario crearUsuario(SQLiteDatabase db, Usuario usuario)
    {
        if (!checkUsuarioExists(db,usuario))
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put("nombre",usuario.getNombre());
            contentValues.put("password",usuario.getPassword());
            db.insert("usuario",null,contentValues);
            return usuario;
        }
        return new Usuario(0,"","");
    }
        
    @SuppressLint("Range")
    public ArrayList<Hobbie> obtenerHobbiesUsuario(SQLiteDatabase db, Usuario usuario)
    {
        String query = "select _id, nombre, descripcion,idUsuario,foto from hobbie where idUsuario = ?";
        Cursor c = db.rawQuery(query, new String[]{String.valueOf(usuario.getId())});
        ArrayList<Hobbie> hobbies = new ArrayList<>();
        if (c.moveToFirst()) {
            Log.i("DBConexion", "Hobbies encontrados");
            do {
                Log.i("DBConexion", "Hobbie encontrado");
                int id = c.getInt(c.getColumnIndex("_id"));
                Log.i("DBConexion", "ID: " + id);
                int idUsuario = c.getInt(c.getColumnIndex("idUsuario"));
                Log.i("DBConexion", "ID Usuario: " + idUsuario);
                String nombre = c.getString(c.getColumnIndex("nombre"));
                Log.i("DBConexion", "Nombre: " + nombre);
                String descripcion = c.getString(c.getColumnIndex("descripcion"));
                Log.i("DBConexion", "Descripcion: " + descripcion);
                byte[] foto = c.getBlob(c.getColumnIndex("foto"));
                Log.i("DBConexion", "Foto: " + Arrays.toString(foto));
                hobbies.add(new Hobbie(id,idUsuario,nombre,descripcion,foto));
            } while (c.moveToNext());
        }
        return hobbies;
    }

    public Hobbie crearHobbie(SQLiteDatabase db, Hobbie hobbie)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("nombre",hobbie.getNombre());
        contentValues.put("descripcion",hobbie.getDescripcion());
        contentValues.put("foto",hobbie.getImagen());
        contentValues.put("idUsuario",hobbie.getIdUsuario());
        db.insert("hobbie",null,contentValues);
        return hobbie;
    }


}
