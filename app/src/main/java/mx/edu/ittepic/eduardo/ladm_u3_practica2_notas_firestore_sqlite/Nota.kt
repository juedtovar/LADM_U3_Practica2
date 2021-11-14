package mx.edu.ittepic.eduardo.ladm_u3_practica2_notas_firestore_sqlite

import android.content.ContentValues
import android.content.Context
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class Nota (p:Context) {

    val pnt = p
    var baseRemota = FirebaseFirestore.getInstance()
    var Titulo = ""
    var Contenido = ""
    var Fecha = ""
    var Hora = ""

    fun insertar() : Boolean {

        val tablaNota = BaseDatos(pnt,"BDNOTAS",null,1).writableDatabase

        val datos = ContentValues()

        datos.put("TITULO",Titulo)
        datos.put("CONTENIDO",Contenido)
        datos.put("FECHA",Fecha)
        datos.put("HORA",Hora)

        val resultado = tablaNota.insert("NOTAS",null,datos)

        if (resultado == -1L){
            return false
        }

        return true
    }//insertar

    fun consultar() : ArrayList<String>{
        val tablaNota = BaseDatos(pnt,"BDNOTAS",null,1).readableDatabase
        val resultado = ArrayList<String>()

        val cursor = tablaNota.query("NOTAS", arrayOf("*"),null,null,null,null,null)

        if (cursor.moveToFirst()){
            do {

                var dato = cursor.getString(1)+"\n"+cursor.getString(2)+"\n"+cursor.getString(3)+" - "+cursor.getString(4)
                resultado.add(dato)
            }while (cursor.moveToNext())
        } else {
            resultado.add("NO HAY NOTAS GUARDADAS")
        }

        return resultado
    }//consultar

    fun Sincronizar(){
        val tablaNota = BaseDatos(pnt,"BDNOTAS",null,1).readableDatabase
        val cursor = tablaNota.query("NOTAS", arrayOf("*"),null,null,null,null,null)

        if (cursor.moveToFirst()){
            do {
                var datosInsertar = hashMapOf(
                    "TITULO" to cursor.getString(1),
                    "CONTENIDO" to cursor.getString(2),
                    "FECHA" to cursor.getString(3),
                    "HORA" to cursor.getString(4)
                )

                baseRemota.collection("NOTAS")
                    .add(datosInsertar) //si marca error poner datosInsertar as Any
                    .addOnSuccessListener { }
                    .addOnFailureListener {
                        Toast.makeText(pnt,"FALLO SINCRONIZACIÓN",Toast.LENGTH_LONG).show()
                    }
            } while (cursor.moveToNext())
        }
        Toast.makeText(pnt,"SINCRONIZACIÓN COMPLETA",Toast.LENGTH_LONG).show()
    }//Sincronizar

    fun consultar(idBuscar:Int) : Nota{
        val tablaNota = BaseDatos(pnt,"BDNOTAS",null,1).readableDatabase

        val cursor = tablaNota.query("NOTAS", arrayOf("*"),"IDNOTA=?", arrayOf(idBuscar.toString()),null,null,null)
        val nota = Nota(MainActivity())

        if (cursor.moveToFirst()){
            nota.Titulo = cursor.getString(1)
            nota.Contenido = cursor.getString(2)
        }
        return nota
    }//consultar

    fun obtenerIDs():ArrayList<Int>{
        val tablaNota = BaseDatos(pnt,"BDNOTAS",null,1).readableDatabase
        val resultado = ArrayList<Int>()

        val cursor = tablaNota.query("NOTAS", arrayOf("*"),null,null,null,null,null)

        if (cursor.moveToFirst()){
            do {
                resultado.add(cursor.getInt(0))
            }while (cursor.moveToNext())
        }
        return resultado
    }//obtenerIDs

    fun Eliminar(idEliminar:Int):Boolean{
        val tablaNota = BaseDatos(pnt,"BDNOTAS",null,1).writableDatabase

        val resultado = tablaNota.delete("NOTAS","IDNOTA=?", arrayOf(idEliminar.toString()))

        if (resultado == 0) return false
        return true
    }//eliminar

    fun Actualizar(idActualizar:Int):Boolean{
        val tablaNota = BaseDatos(pnt,"BDNOTAS",null,1).readableDatabase
        val datos = ContentValues()

        datos.put("TITULO",Titulo)
        datos.put("CONTENIDO",Contenido)
        datos.put("FECHA",Fecha)
        datos.put("HORA",Hora)

        val resultado = tablaNota.update("NOTAS",datos,"IDNOTA=?", arrayOf(idActualizar.toString()))
        if (resultado == 0) return false
        return true
    }//actualizar

}