package mx.edu.ittepic.eduardo.ladm_u3_practica2_notas_firestore_sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BaseDatos (context: Context?,
                 name: String,
                 factory: SQLiteDatabase.CursorFactory?,
                 version: Int)
    : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(p: SQLiteDatabase?) {
        p?.execSQL("CREATE TABLE NOTAS(IDNOTA INTEGER PRIMARY KEY AUTOINCREMENT,TITULO VARCHAR(100), CONTENIDO VARCHAR(400),FECHA DATE,HORA VARCHAR(20));")
    }

    override fun onUpgrade(p: SQLiteDatabase?, p1: Int, p2: Int) {

    }
}