package com.example.prcticaevaluableapp.DB;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
    private DBConexion conexion; // 3 usages
    private SQLiteDatabase basedatos; // 1 usage

    public DBManager(Context context) { this.conexion = new DBConexion(context); }

    public DBManager open() throws SQLException {
        basedatos = this.conexion.getWritableDatabase();
        return this;
    }



    public void close() { this.conexion.close(); }
}
