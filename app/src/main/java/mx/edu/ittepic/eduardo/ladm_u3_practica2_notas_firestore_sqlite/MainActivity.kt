package mx.edu.ittepic.eduardo.ladm_u3_practica2_notas_firestore_sqlite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class MainActivity : AppCompatActivity() {

    var Actualizar = false
    var idNotas = ArrayList<Int>()
    var idSeleccionado = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        MostrarNotas()

        btnNuevo.setOnClickListener {
            val d = Date()
            val fecha = SimpleDateFormat("yyyy-MM-dd")
            val hora = SimpleDateFormat("HH:mm:ss")

            val nota = Nota(this)

            nota.Titulo = txtTitulo.text.toString()
            nota.Contenido = txtContenido.text.toString()
            nota.Fecha = fecha.format(d)
            nota.Hora = hora.format(d)

            if (Actualizar){

                val resultadoActualizar = nota.Actualizar(idSeleccionado)
                Actualizar = false

                if (resultadoActualizar){
                    Toast.makeText(this,"EXITO SE ACTUALIZO", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this,"ERROR NO SE ACTUALIZO", Toast.LENGTH_LONG).show()
                }
                imagenadd.setImageResource(R.drawable.plus)
                txtadd.setText("Añadir Nota")
                Limpiar()

            } else {
                val resultadoInsertar = nota.insertar()

                if (resultadoInsertar){
                    Toast.makeText(this,"EXITO SE INSERTO", Toast.LENGTH_LONG).show()
                    Limpiar()
                } else {
                    Toast.makeText(this,"ERROR NO SE INSERTO", Toast.LENGTH_LONG).show()
                }

            }//else

        }//btnNuevo

        btnNube.setOnClickListener {
            Nota(this).Sincronizar()
        }//btnNube
    }

    fun Limpiar(){
        txtTitulo.setText("")
        txtContenido.setText("")
        MostrarNotas()
    }//Limpiar

    fun MostrarNotas(){
        val arregloNota = Nota(this).consultar()

        listaNotas.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arregloNota)

        idNotas.clear()
        idNotas = Nota(this).obtenerIDs()
        activarEvento(listaNotas)
    }//MostrarConductores

    fun activarEvento(ListaNotas: ListView){
        ListaNotas.setOnItemClickListener { adapterView, view, i, l ->

            if (!idNotas.isEmpty()) {
                idSeleccionado = idNotas[i]

                AlertDialog.Builder(this)
                    .setTitle("ATENCION")
                    .setMessage("QUE DESEA REALIZAR CON LA NOTA")
                    .setPositiveButton("EDITAR") { d, i -> Editar() }
                    .setNegativeButton("ELIMINAR") { d, i -> Eliminar() }
                    .setNeutralButton("CANCELAR") { d, i ->
                        d.cancel()
                    }
                    .show()
            }
        }
    }//activarEvento

    fun Eliminar(){
        AlertDialog.Builder(this)
            .setTitle("IMPORTANTE")
            .setMessage("¿SEGURO QUE DESEAS ELIMINAR ESTA NOTA?")
            .setPositiveButton("SI"){d,i->
                val resultado = Nota(this).Eliminar(idSeleccionado)

                if (resultado){
                    Toast.makeText(this,"SE ELIMINO CON EXITO",Toast.LENGTH_LONG).show()
                    MostrarNotas()
                } else {
                    Toast.makeText(this,"NO SE LOGRO ELIMINAR",Toast.LENGTH_LONG).show()
                }
            }
            .setNegativeButton("NO"){d,i->
                d.cancel()
            }
            .show()
    }//Eliminar

    fun Editar(){
        Actualizar = true
        imagenadd.setImageResource(R.drawable.editar)
        txtadd.setText("Actualizar")

        val nota = Nota(this).consultar(idSeleccionado)

        txtTitulo.setText(nota.Titulo)
        txtContenido.setText(nota.Contenido)
    }//Editar

    /*
    private fun insertar() {
        //para INSERTAR el maetodo a usar es ADD
        //ADD espera TODOS los campos del documento
        //con formato CLAVE VALOR

        val d = Date()
        val fecha = SimpleDateFormat("yyyy-MM-dd")
        val hora = SimpleDateFormat("HH:mm:ss")


        var datosInsertar = hashMapOf(
            "TITULO" to txtTitulo.text.toString(),
            "CONTENIDO" to txtContenido.text.toString(),
            "FECHA" to fecha.format(d),
            "HORA" to hora.format(d)
        )

        baseRemota.collection("NOTAS")
            .add(datosInsertar) //si marca error poner datosInsertar as Any
            .addOnSuccessListener {
                Toast.makeText(this,"SE INSERTO CORRECTAMENTE EN LA NUBE",Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(this,"ERROR",Toast.LENGTH_LONG).show()
            }
    }*/

}