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
    private var idRopa: Int = -1

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

        idRopa = intent.getIntExtra("id", -1)

        if (idRopa != -1) {
            cargarDatosRopa()
        }


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
        binding.btnEliminar.setOnClickListener {
            confirmarEliminar()
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
    private fun cargarDatosRopa() {

        val ropa =
            ropaDao.obtener(idRopa)
                ?: return


        binding.etModelo.setText(
            ropa.modelo
        )

        binding.etMarca.setText(
            ropa.marca
        )

        binding.etColor.setText(
            ropa.color
        )

        binding.etPrecio.setText(
            ropa.precio.toString()
        )

        binding.etCantidad.setText(
            ropa.cantidad.toString()
        )


        rutaFoto =
            ropa.foto


        if (rutaFoto.isNotEmpty()) {

            val bitmap =
                android.graphics.BitmapFactory
                    .decodeFile(rutaFoto)

            binding.imgFoto
                .setImageBitmap(bitmap)
        }


        // CATEGORÍA

        val posicionCategoria =
            categorias.indexOfFirst {

                it.first ==
                        ropa.idCategoria
            }


        if (posicionCategoria >= 0) {

            binding.spCategoria
                .setSelection(
                    posicionCategoria
                )
        }


        // TALLA

        val tallas =
            arrayOf(
                "XS",
                "S",
                "M",
                "L",
                "XL"
            )


        val posicionTalla =
            tallas.indexOf(
                ropa.talla
            )


        if (posicionTalla >= 0) {

            binding.spTalla
                .setSelection(
                    posicionTalla
                )
        }


        // CAMBIAR MODO

        binding.btnGuardar.text =
            "Actualizar"

        binding.btnEliminar.visibility =
            android.view.View.VISIBLE
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
            binding.etModelo.text
                .toString()
                .trim()

        val marca =
            binding.etMarca.text
                .toString()
                .trim()

        val color =
            binding.etColor.text
                .toString()
                .trim()

        val precioTexto =
            binding.etPrecio.text
                .toString()
                .trim()

        val cantidadTexto =
            binding.etCantidad.text
                .toString()
                .trim()


        // ==========================
        // VALIDACIONES
        // ==========================

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


        if (precio == null || precio <= 0) {

            binding.etPrecio.error =
                "El precio debe ser mayor a 0"

            return
        }


        val cantidad =
            cantidadTexto.toIntOrNull()


        if (cantidad == null || cantidad < 0) {

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


        // ==========================
        // INSERTAR
        // ==========================

        if (idRopa == -1) {

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


        } else {

            // ==========================
            // ACTUALIZAR
            // ==========================

            val resultado =
                ropaDao.actualizar(
                    idRopa,
                    modelo,
                    idCategoria,
                    talla,
                    marca,
                    color,
                    precio,
                    cantidad,
                    rutaFoto
                )


            if (resultado > 0) {

                Toast.makeText(
                    this,
                    "Prenda actualizada correctamente",
                    Toast.LENGTH_SHORT
                ).show()

                finish()

            } else {

                Toast.makeText(
                    this,
                    "No se pudo actualizar la prenda",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    private fun confirmarEliminar() {

        androidx.appcompat.app.AlertDialog
            .Builder(this)
            .setTitle("Eliminar prenda")
            .setMessage(
                "¿Está seguro de eliminar esta prenda?"
            )
            .setPositiveButton("Sí") { _, _ ->

                eliminarRopa()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
    private fun eliminarRopa() {

        try {

            val resultado =
                ropaDao.eliminar(idRopa)

            if (resultado > 0) {

                Toast.makeText(
                    this,
                    "Prenda eliminada correctamente",
                    Toast.LENGTH_SHORT
                ).show()

                finish()

            } else {

                Toast.makeText(
                    this,
                    "No se pudo eliminar la prenda",
                    Toast.LENGTH_SHORT
                ).show()
            }

        } catch (
            e: android.database.sqlite.SQLiteConstraintException
        ) {

            Toast.makeText(
                this,
                "No se puede eliminar: tiene pedidos",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}