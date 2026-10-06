package com.example.modaap

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.modaap.adapter.PedidoAdapter
import com.example.modaap.data.PedidoDao
import com.example.modaap.databinding.ActivityPedidosBinding

class PedidosActivity : AppCompatActivity() {
    private lateinit var binding:
            ActivityPedidosBinding

    private lateinit var pedidoDao:
            PedidoDao

    private lateinit var adapter:
            PedidoAdapter


    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        binding =
            ActivityPedidosBinding.inflate(
                layoutInflater
            )

        setContentView(binding.root)


        pedidoDao =
            PedidoDao(this)


        adapter =
            PedidoAdapter(
                emptyList()
            ) { pedido ->

                val intent =
                    Intent(
                        this,
                        DetallePedidoActivity::class.java
                    )

                intent.putExtra(
                    "idPedido",
                    pedido.id
                )

                startActivity(intent)
            }


        binding.rvPedidos.layoutManager =
            LinearLayoutManager(this)

        binding.rvPedidos.adapter =
            adapter
    }


    override fun onResume() {

        super.onResume()

        cargarPedidos()
    }


    private fun cargarPedidos() {

        val lista =
            pedidoDao.listarPendientes()


        adapter.actualizar(lista)


        if (lista.isEmpty()) {

            binding.tvSinPedidos.visibility =
                View.VISIBLE

            binding.rvPedidos.visibility =
                View.GONE

        } else {

            binding.tvSinPedidos.visibility =
                View.GONE

            binding.rvPedidos.visibility =
                View.VISIBLE
        }
    }
}