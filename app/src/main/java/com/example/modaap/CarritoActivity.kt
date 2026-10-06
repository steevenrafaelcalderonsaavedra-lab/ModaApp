package com.example.modaap

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.modaap.adapter.CarritoAdapter
import com.example.modaap.databinding.ActivityCarritoBinding
import com.example.modaap.model.ItemCarrito
import com.example.modaap.util.Carrito

class CarritoActivity : AppCompatActivity() {
    private lateinit var binding:
            ActivityCarritoBinding

    private lateinit var adapter:
            CarritoAdapter


    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        binding =
            ActivityCarritoBinding.inflate(
                layoutInflater
            )

        setContentView(binding.root)


        adapter =
            CarritoAdapter(
                Carrito.items
            ) { item ->

                confirmarEliminar(item)
            }


        binding.rvCarrito.layoutManager =
            LinearLayoutManager(this)

        binding.rvCarrito.adapter =
            adapter


        binding.btnHacerPedido
            .setOnClickListener {

                val intent =
                    Intent(
                        this,
                        PedidoActivity::class.java
                    )

                startActivity(intent)
            }


        actualizar()
    }


    override fun onResume() {
        super.onResume()

        actualizar()
    }


    private fun confirmarEliminar(
        item: ItemCarrito
    ) {

        AlertDialog.Builder(this)

            .setTitle(
                "Quitar prenda"
            )

            .setMessage(
                "¿Desea quitar ${item.ropa.modelo} del carrito?"
            )

            .setPositiveButton(
                "Sí"
            ) { _, _ ->

                Carrito.eliminar(item)

                actualizar()
            }

            .setNegativeButton(
                "Cancelar",
                null
            )

            .show()
    }


    private fun actualizar() {

        adapter.actualizar(
            Carrito.items
        )


        binding.tvTotal.text =
            "Total: S/ %.2f".format(
                Carrito.total()
            )


        if (Carrito.estaVacio()) {

            binding.tvVacio.visibility =
                View.VISIBLE

            binding.rvCarrito.visibility =
                View.GONE

            binding.btnHacerPedido.isEnabled =
                false

        } else {

            binding.tvVacio.visibility =
                View.GONE

            binding.rvCarrito.visibility =
                View.VISIBLE

            binding.btnHacerPedido.isEnabled =
                true
        }
    }
}