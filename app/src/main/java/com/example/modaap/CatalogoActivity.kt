package com.example.modaap

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
            )


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
}