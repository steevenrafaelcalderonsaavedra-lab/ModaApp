package com.example.modaap

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.modaap.data.ClienteDao
import com.example.modaap.data.PedidoDao
import com.example.modaap.databinding.ActivityPedidoBinding
import com.example.modaap.model.Cliente
import com.example.modaap.util.Carrito

class PedidoActivity : AppCompatActivity() {
    private lateinit var binding:
            ActivityPedidoBinding

    private lateinit var clienteDao:
            ClienteDao

    private lateinit var pedidoDao:
            PedidoDao


    private var clienteEncontrado:
            Cliente? = null

    private var esClienteNuevo =
        false


    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        binding =
            ActivityPedidoBinding.inflate(
                layoutInflater
            )

        setContentView(binding.root)


        clienteDao =
            ClienteDao(this)

        pedidoDao =
            PedidoDao(this)


        binding.tvTotal.text =
            "Total: S/ %.2f".format(
                Carrito.total()
            )


        binding.btnContinuar
            .setOnClickListener {

                buscarCliente()
            }


        binding.btnConfirmar
            .setOnClickListener {

                confirmarPedido()
            }
    }


    private fun buscarCliente() {

        val telefono =
            binding.etTelefono
                .text
                .toString()
                .trim()


        // TELÉFONO DE 9 DÍGITOS

        if (
            telefono.length != 9 ||
            !telefono.all {
                it.isDigit()
            }
        ) {

            binding.etTelefono.error =
                "Ingrese un teléfono de 9 dígitos"

            return
        }


        clienteEncontrado =
            clienteDao
                .buscarPorTelefono(
                    telefono
                )


        if (clienteEncontrado != null) {

            // CLIENTE EXISTENTE

            esClienteNuevo =
                false


            binding.layoutNuevoCliente
                .visibility =
                View.GONE


            binding.tvSaludo
                .visibility =
                View.VISIBLE


            binding.tvSaludo.text =
                "Hola, ${clienteEncontrado!!.nombres}"


            binding.btnConfirmar
                .isEnabled =
                true

        } else {

            // CLIENTE NUEVO

            esClienteNuevo =
                true


            binding.tvSaludo
                .visibility =
                View.GONE


            binding.layoutNuevoCliente
                .visibility =
                View.VISIBLE


            binding.btnConfirmar
                .isEnabled =
                true


            Toast.makeText(
                this,
                "Cliente nuevo: complete sus datos",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun confirmarPedido() {

        if (Carrito.estaVacio()) {

            Toast.makeText(
                this,
                "El carrito está vacío",
                Toast.LENGTH_SHORT
            ).show()

            return
        }


        var idCliente: Int


        if (esClienteNuevo) {

            val telefono =
                binding.etTelefono
                    .text
                    .toString()
                    .trim()


            val nombres =
                binding.etNombres
                    .text
                    .toString()
                    .trim()


            val apellidos =
                binding.etApellidos
                    .text
                    .toString()
                    .trim()


            if (nombres.isEmpty()) {

                binding.etNombres.error =
                    "Ingrese sus nombres"

                return
            }


            if (apellidos.isEmpty()) {

                binding.etApellidos.error =
                    "Ingrese sus apellidos"

                return
            }


            val nuevoId =
                clienteDao.insertar(
                    telefono,
                    nombres,
                    apellidos
                )


            if (nuevoId == -1L) {

                Toast.makeText(
                    this,
                    "No se pudo registrar el cliente",
                    Toast.LENGTH_SHORT
                ).show()

                return
            }


            idCliente =
                nuevoId.toInt()

        } else {

            val cliente =
                clienteEncontrado


            if (cliente == null) {

                Toast.makeText(
                    this,
                    "Primero pulse Continuar",
                    Toast.LENGTH_SHORT
                ).show()

                return
            }


            idCliente =
                cliente.id
        }


        // GUARDAR PEDIDO + DETALLE

        val idPedido =
            pedidoDao.registrar(
                idCliente,
                Carrito.items
            )


        if (idPedido != -1L) {

            Carrito.vaciar()


            Toast.makeText(
                this,
                "Pedido #$idPedido registrado",
                Toast.LENGTH_LONG
            ).show()


            finish()

        } else {

            Toast.makeText(
                this,
                "Error al registrar el pedido",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}