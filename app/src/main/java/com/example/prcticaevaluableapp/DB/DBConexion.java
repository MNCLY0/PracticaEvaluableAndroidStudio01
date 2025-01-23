package com.example.prcticaevaluableapp.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.prcticaevaluableapp.Usuario;

import java.util.ArrayList;

public class DBConexion extends SQLiteOpenHelper {

    private static final String DB_NAME =  "aplicacionDB";
    private static final int DB_VERSION = 2;
    //    Tabla contactos
    public static final String TABLA_USUARIO = "usuario";
    public static final String USUARIO_ID = "_id";
    public static final String USUARIO_NOMBRE = "nombre";
    public static final String USUARIO_PASSWORD = "password";
    public static final String SENTENCIA_SELECCION_CONTACTOS = "select _id, nombre, password" +
            "from usuario";

    public static final String SENTENCIA_CREACION_TABLA_USUARIO = "create table usuario " +
            "(_id integer primary key autoincrement, nombre text not null, password text not null);";

    //    public static final String SENTENCIA_ACTUALIZACION_TABLA_CONTACTOS = "UPDATE contactos add colum " +
//            "(_foto integer)";
    public DBConexion(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        codigo sql
//        instanciamos y creamos la base de datos
//        este codigo se ejectua cuando se crea la base de datos
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_USUARIO);
        db.execSQL(SENTENCIA_CREACION_TABLA_USUARIO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//      codigo sql para actualizacion de la base de datos
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_USUARIO);
        onCreate(db);
    }

//    public ArrayList<Usuario> selectUsuarios (SQLiteDatabase db) {
//        ArrayList<Usuario> contactos = new ArrayList<>();
////        Consultamos los datos
//        Cursor c = db.rawQuery(SENTENCIA_SELECCION_CONTACTOS, null);
//
//        if (c.moveToFirst()){
////            Es un fetch Array, ya utilizado en SQL Statement
//            do {
////                @SuppressLint("Range")
////                int id = c.getInt(c.getColumnIndex("_id"));
////                Asignamos el valor en nuestra variables para usarlos en lo que necesitemos
//                @SuppressLint("Range") String nombre = c.getString(c.getColumnIndex("nombre"));
//                @SuppressLint("Range") String password = c.getString(c.getColumnIndex("password"));
//                Contacto contactoExtraidoBD = new Contacto(nombre, email, telefono, 1);
//                contactos.add(contactoExtraidoBD);
//
//            } while (c.moveToNext());
//        }
//        c.close();
//        return contactos;
//    }


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
}
