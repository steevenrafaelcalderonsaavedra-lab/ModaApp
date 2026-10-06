package com.example.modaap
import android.widget.Toast
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.modaap.adapter.CatalogoAdapter
import com.example.modaap.data.DBHelper
import com.example.modaap.data.RopaDao
import com.example.modaap.databinding.ActivityCatalogoBinding
import com.google.android.material.chip.Chip
import com.example.modaap.model.Ropa
import com.example.modaap.util.Carrito
class CatalogoActivity : AppCompatActivity() {
    private lateinit var binding:
            ActivityCatalogoBinding

    private lateinit var dbHelper:
            DBHelper

    private lateinit var ropaDao:
            RopaDao

    private lateinit var adapter:
            CatalogoAdapter


    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        binding =
            ActivityCatalogoBinding.inflate(
                layoutInflater
            )

        setContentView(binding.root)


        dbHelper =
            DBHelper(this)

        ropaDao =
            RopaDao(this)


        adapter =
            CatalogoAdapter(
                emptyList()
            ) { ropa ->

                mostrarCantidad(ropa)
            }

        // GRILLA DE DOS COLUMNAS

        binding.rvCatalogo.layoutManager =
            GridLayoutManager(
                this,
                2
            )


        binding.rvCatalogo.adapter =
            adapter


        cargarChips()

        cargarProductos(null)


        binding.btnCarrito
            .setOnClickListener {

                val intent =
                    Intent(
                        this,
                        CarritoActivity::class.java
                    )

                startActivity(intent)
            }
    }


    private fun cargarChips() {

        binding
            .chipGroupCategorias
            .removeAllViews()


        // CHIP TODAS

        val chipTodas =
            crearChip(
                "Todas",
                0
            )


        binding
            .chipGroupCategorias
            .addView(chipTodas)


        chipTodas.isChecked = true


        // CATEGORÍAS DE SQLITE

        val categorias =
            dbHelper.listarCategorias()


        for (categoria in categorias) {

            val chip =
                crearChip(
                    categoria.second,
                    categoria.first
                )


            binding
                .chipGroupCategorias
                .addView(chip)
        }
    }


    private fun crearChip(
        nombre: String,
        idCategoria: Int
    ): Chip {

        val chip =
            Chip(this)


        chip.id =
            View.generateViewId()


        chip.text =
            nombre


        chip.isCheckable =
            true


        chip.setOnClickListener {

            if (idCategoria == 0) {

                cargarProductos(null)

            } else {

                cargarProductos(
                    idCategoria
                )
            }
        }


        return chip
    }


    private fun cargarProductos(
        idCategoria: Int?
    ) {

        val lista =
            ropaDao.listarDisponibles(
                idCategoria
            )


        adapter.actualizar(
            lista
        )
    }
    private fun mostrarCantidad(
        ropa: Ropa
    ) {

        val opciones =
            (1..ropa.cantidad)
                .map {
                    it.toString()
                }
                .toTypedArray()


        androidx.appcompat.app.AlertDialog
            .Builder(this)
            .setTitle(
                "Agregar ${ropa.modelo}"
            )

            .setSingleChoiceItems(
                opciones,
                0,
                null
            )

            .setMessage(
                "Disponible: ${ropa.cantidad}"
            )

            .setPositiveButton(
                "Agregar"
            ) { dialog, _ ->

                val alert =
                    dialog as androidx.appcompat.app.AlertDialog

                val posicion =
                    alert.listView.checkedItemPosition

                val cantidad =
                    if (posicion >= 0)
                        posicion + 1
                    else
                        1


                val agregado =
                    Carrito.agregar(
                        ropa,
                        cantidad
                    )


                if (agregado) {

                    Toast.makeText(
                        this,
                        "Agregado al carrito",
                        Toast.LENGTH_SHORT
                    ).show()

                    actualizarContador()

                } else {

                    Toast.makeText(
                        this,
                        "No puede superar el stock disponible",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            .setNegativeButton(
                "Cancelar",
                null
            )

            .show()
    }
    private fun actualizarContador() {

        val cantidad =
            Carrito.cantidadProductos()

        binding.btnCarrito.text =
            "Carrito ($cantidad)"
    }
    override fun onResume() {
        super.onResume()

        actualizarContador()
    }
}