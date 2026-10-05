package com.example.modaap

import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.modaap.data.DBHelper
import com.example.modaap.data.RopaDao
import com.example.modaap.databinding.ActivityRegistrarRopaBinding
import java.io.File
import java.io.FileOutputStream
class RegistrarRopaActivity : AppCompatActivity() {
    private lateinit var binding:
            ActivityRegistrarRopaBinding

    private lateinit var dbHelper: DBHelper

    private lateinit var ropaDao: RopaDao


    private var fotoSeleccionada: Uri? = null

    private var rutaFoto: String = ""


    private var categorias =
        ArrayList<Pair<Int, String>>()


    private val seleccionarFoto =
        registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri ->

            if (uri != null) {

                fotoSeleccionada = uri

                binding.imgFoto.setImageURI(uri)

                rutaFoto =
                    copiarFoto(uri)
            }
        }


    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        binding =
            ActivityRegistrarRopaBinding.inflate(
                layoutInflater
            )

        setContentView(binding.root)


        dbHelper = DBHelper(this)

        ropaDao = RopaDao(this)


        cargarCategorias()

        cargarTallas()


        binding.btnElegirFoto.setOnClickListener {

            seleccionarFoto.launch(

                PickVisualMediaRequest(
                    ActivityResultContracts
                        .PickVisualMedia
                        .ImageOnly
                )
            )
        }


        binding.btnGuardar.setOnClickListener {

            guardarRopa()
        }
    }


    private fun cargarCategorias() {

        categorias =
            dbHelper.listarCategorias()


        val nombres =
            categorias.map {
                it.second
            }


        val adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                nombres
            )


        adapter.setDropDownViewResource(
            android.R.layout
                .simple_spinner_dropdown_item
        )


        binding.spCategoria.adapter =
            adapter
    }


    private fun cargarTallas() {

        val tallas =
            arrayOf(
                "XS",
                "S",
                "M",
                "L",
                "XL"
            )


        val adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                tallas
            )


        adapter.setDropDownViewResource(
            android.R.layout
                .simple_spinner_dropdown_item
        )


        binding.spTalla.adapter =
            adapter
    }


    private fun copiarFoto(
        uri: Uri
    ): String {

        return try {

            val nombre =
                "ropa_${System.currentTimeMillis()}.jpg"


            val archivo =
                File(
                    filesDir,
                    nombre
                )


            val entrada =
                contentResolver
                    .openInputStream(uri)


            val salida =
                FileOutputStream(archivo)


            entrada?.copyTo(salida)


            entrada?.close()

            salida.close()


            archivo.absolutePath

        } catch (e: Exception) {

            Toast.makeText(
                this,
                "Error al guardar foto",
                Toast.LENGTH_SHORT
            ).show()

            ""
        }
    }


    private fun guardarRopa() {

        val modelo =
            binding.etModelo
                .text
                .toString()
                .trim()


        val marca =
            binding.etMarca
                .text
                .toString()
                .trim()


        val color =
            binding.etColor
                .text
                .toString()
                .trim()


        val precioTexto =
            binding.etPrecio
                .text
                .toString()
                .trim()


        val cantidadTexto =
            binding.etCantidad
                .text
                .toString()
                .trim()


        // VALIDACIONES

        if (modelo.isEmpty()) {

            binding.etModelo.error =
                "Ingrese el modelo"

            return
        }


        if (rutaFoto.isEmpty()) {

            Toast.makeText(
                this,
                "Seleccione una foto",
                Toast.LENGTH_SHORT
            ).show()

            return
        }


        if (precioTexto.isEmpty()) {

            binding.etPrecio.error =
                "Ingrese el precio"

            return
        }


        if (cantidadTexto.isEmpty()) {

            binding.etCantidad.error =
                "Ingrese la cantidad"

            return
        }


        val precio =
            precioTexto.toDoubleOrNull()


        if (
            precio == null ||
            precio <= 0
        ) {

            binding.etPrecio.error =
                "El precio debe ser mayor a 0"

            return
        }


        val cantidad =
            cantidadTexto.toIntOrNull()


        if (
            cantidad == null ||
            cantidad < 0
        ) {

            binding.etCantidad.error =
                "Cantidad inválida"

            return
        }


        if (categorias.isEmpty()) {

            Toast.makeText(
                this,
                "No existen categorías",
                Toast.LENGTH_SHORT
            ).show()

            return
        }


        val posicion =
            binding.spCategoria
                .selectedItemPosition


        val idCategoria =
            categorias[posicion].first


        val talla =
            binding.spTalla
                .selectedItem
                .toString()


        val resultado =
            ropaDao.insertar(
                modelo,
                idCategoria,
                talla,
                marca,
                color,
                precio,
                cantidad,
                rutaFoto
            )


        if (resultado != -1L) {

            Toast.makeText(
                this,
                "Prenda registrada correctamente",
                Toast.LENGTH_SHORT
            ).show()

            finish()

        } else {

            Toast.makeText(
                this,
                "Error al registrar prenda",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}