package com.example.modaap.data
import android.content.ContentValues
import android.content.Context
import com.example.modaap.model.ItemCarrito
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
class PedidoDao(
    context: Context
) {

    private val dbHelper =
        DBHelper(context)


    fun registrar(
        idCliente: Int,
        items: List<ItemCarrito>
    ): Long {

        val db =
            dbHelper.writableDatabase


        var idPedido = -1L


        db.beginTransaction()


        try {

            val total =
                items.sumOf {
                    it.subtotal()
                }


            val fecha =
                SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss",
                    Locale.getDefault()
                ).format(Date())


            // =====================
            // PEDIDO
            // =====================

            val valoresPedido =
                ContentValues().apply {

                    put(
                        "id_cliente",
                        idCliente
                    )

                    put(
                        "fecha",
                        fecha
                    )

                    put(
                        "total",
                        total
                    )

                    put(
                        "estado",
                        "PENDIENTE"
                    )
                }


            idPedido =
                db.insertOrThrow(
                    "pedido",
                    null,
                    valoresPedido
                )


            // =====================
            // DETALLES
            // =====================

            for (item in items) {

                val valoresDetalle =
                    ContentValues().apply {

                        put(
                            "id_pedido",
                            idPedido
                        )

                        put(
                            "id_ropa",
                            item.ropa.id
                        )

                        put(
                            "cantidad",
                            item.cantidad
                        )

                        put(
                            "precio_unit",
                            item.ropa.precio
                        )

                        put(
                            "subtotal",
                            item.subtotal()
                        )
                    }


                db.insertOrThrow(
                    "detalle_pedido",
                    null,
                    valoresDetalle
                )
            }


            db.setTransactionSuccessful()


        } catch (e: Exception) {

            idPedido = -1L

        } finally {

            db.endTransaction()
        }


        return idPedido
    }
}