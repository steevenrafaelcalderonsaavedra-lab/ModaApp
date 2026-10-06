package com.example.modaap

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.modaap.data.PedidoDao
import com.example.modaap.databinding.ActivityDetallePedidoBinding

class DetallePedidoActivity : AppCompatActivity() {
    private lateinit var binding:
            ActivityDetallePedidoBinding

    private lateinit var pedidoDao:
            PedidoDao

    private var idPedido =
        -1


    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        binding =
            ActivityDetallePedidoBinding.inflate(
                layoutInflater
            )

        setContentView(binding.root)


        pedidoDao =
            PedidoDao(this)


        idPedido =
            intent.getIntExtra(
                "idPedido",
                -1
            )


        if (idPedido == -1) {

            finish()

            return
        }


        cargarDetalles()


        binding.btnAtender
            .setOnClickListener {

                confirmarAtencion()
            }
    }


    private fun cargarDetalles() {

        val detalles =
            pedidoDao.obtenerDetalles(
                idPedido
            )


        binding.tvTitulo.text =
            "Pedido #$idPedido"


        val texto =
            StringBuilder()


        for (detalle in detalles) {

            texto.append(
                "${detalle.modelo}\n"
            )

            texto.append(
                "Talla: ${detalle.talla}\n"
            )

            texto.append(
                "Cantidad: ${detalle.cantidad}\n"
            )

            texto.append(
                "Precio: S/ %.2f\n"
                    .format(
                        detalle.precioUnit
                    )
            )

            texto.append(
                "Subtotal: S/ %.2f\n\n"
                    .format(
                        detalle.subtotal
                    )
            )
        }


        binding.tvDetalles.text =
            texto.toString()
    }


    private fun confirmarAtencion() {

        AlertDialog.Builder(this)

            .setTitle(
                "Atender pedido"
            )

            .setMessage(
                "¿Desea marcar este pedido como atendido?"
            )

            .setPositiveButton(
                "Sí"
            ) { _, _ ->

                atender()
            }

            .setNegativeButton(
                "Cancelar",
                null
            )

            .show()
    }


    private fun atender() {

        val resultado =
            pedidoDao.atenderPedido(
                idPedido
            )


        if (resultado) {

            Toast.makeText(
                this,
                "Pedido atendido correctamente",
                Toast.LENGTH_LONG
            ).show()

            finish()

        } else {

            Toast.makeText(
                this,
                "No se puede atender: stock insuficiente",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}