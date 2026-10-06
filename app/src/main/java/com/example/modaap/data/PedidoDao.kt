package com.example.modaap.data
import android.content.ContentValues
import android.content.Context
import com.example.modaap.model.ItemCarrito
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.modaap.model.Pedido
import com.example.modaap.model.DetallePedido
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
    fun listarPendientes(): ArrayList<Pedido> {

        val lista =
            ArrayList<Pedido>()

        val db =
            dbHelper.readableDatabase


        val cursor =
            db.rawQuery(
                """
            SELECT
                p.id,
                p.id_cliente,
                c.nombres,
                c.apellidos,
                c.telefono,
                p.fecha,
                p.total,
                p.estado

            FROM pedido p

            INNER JOIN cliente c
            ON p.id_cliente = c.id

            WHERE p.estado = 'PENDIENTE'

            ORDER BY p.id DESC
            """.trimIndent(),
                null
            )


        while (cursor.moveToNext()) {

            lista.add(

                Pedido(

                    id =
                        cursor.getInt(
                            cursor.getColumnIndexOrThrow(
                                "id"
                            )
                        ),

                    idCliente =
                        cursor.getInt(
                            cursor.getColumnIndexOrThrow(
                                "id_cliente"
                            )
                        ),

                    cliente =
                        cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                "nombres"
                            )
                        ) +
                                " " +
                                cursor.getString(
                                    cursor.getColumnIndexOrThrow(
                                        "apellidos"
                                    )
                                ),

                    telefono =
                        cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                "telefono"
                            )
                        ),

                    fecha =
                        cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                "fecha"
                            )
                        ),

                    total =
                        cursor.getDouble(
                            cursor.getColumnIndexOrThrow(
                                "total"
                            )
                        ),

                    estado =
                        cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                "estado"
                            )
                        )
                )
            )
        }


        cursor.close()

        return lista
    }
    fun obtenerDetalles(
        idPedido: Int
    ): ArrayList<DetallePedido> {

        val lista =
            ArrayList<DetallePedido>()

        val db =
            dbHelper.readableDatabase


        val cursor =
            db.rawQuery(
                """
            SELECT
                d.id,
                d.id_pedido,
                d.id_ropa,
                r.modelo,
                r.talla,
                d.cantidad,
                d.precio_unit,
                d.subtotal

            FROM detalle_pedido d

            INNER JOIN ropa r
            ON d.id_ropa = r.id

            WHERE d.id_pedido = ?
            """.trimIndent(),

                arrayOf(
                    idPedido.toString()
                )
            )


        while (cursor.moveToNext()) {

            lista.add(

                DetallePedido(

                    id =
                        cursor.getInt(
                            cursor.getColumnIndexOrThrow(
                                "id"
                            )
                        ),

                    idPedido =
                        cursor.getInt(
                            cursor.getColumnIndexOrThrow(
                                "id_pedido"
                            )
                        ),

                    idRopa =
                        cursor.getInt(
                            cursor.getColumnIndexOrThrow(
                                "id_ropa"
                            )
                        ),

                    modelo =
                        cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                "modelo"
                            )
                        ),

                    talla =
                        cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                "talla"
                            )
                        ),

                    cantidad =
                        cursor.getInt(
                            cursor.getColumnIndexOrThrow(
                                "cantidad"
                            )
                        ),

                    precioUnit =
                        cursor.getDouble(
                            cursor.getColumnIndexOrThrow(
                                "precio_unit"
                            )
                        ),

                    subtotal =
                        cursor.getDouble(
                            cursor.getColumnIndexOrThrow(
                                "subtotal"
                            )
                        )
                )
            )
        }


        cursor.close()

        return lista
    }
    fun atenderPedido(
        idPedido: Int
    ): Boolean {

        val db =
            dbHelper.writableDatabase

        db.beginTransaction()


        try {

            // =============================
            // 1. OBTENER DETALLES
            // =============================

            val cursor =
                db.rawQuery(
                    """
                SELECT
                    d.id_ropa,
                    d.cantidad,
                    r.cantidad AS stock

                FROM detalle_pedido d

                INNER JOIN ropa r
                ON d.id_ropa = r.id

                WHERE d.id_pedido = ?
                """.trimIndent(),

                    arrayOf(
                        idPedido.toString()
                    )
                )


            val productos =
                mutableListOf<
                        Triple<Int, Int, Int>
                        >()


            while (
                cursor.moveToNext()
            ) {

                val idRopa =
                    cursor.getInt(
                        cursor.getColumnIndexOrThrow(
                            "id_ropa"
                        )
                    )

                val cantidad =
                    cursor.getInt(
                        cursor.getColumnIndexOrThrow(
                            "cantidad"
                        )
                    )

                val stock =
                    cursor.getInt(
                        cursor.getColumnIndexOrThrow(
                            "stock"
                        )
                    )


                // =========================
                // 2. VALIDAR STOCK
                // =========================

                if (cantidad > stock) {

                    cursor.close()

                    throw Exception(
                        "Stock insuficiente"
                    )
                }


                productos.add(
                    Triple(
                        idRopa,
                        cantidad,
                        stock
                    )
                )
            }


            cursor.close()


            // =============================
            // 3. DESCONTAR STOCK
            // =============================

            for (
            producto in productos
            ) {

                val idRopa =
                    producto.first

                val cantidad =
                    producto.second


                db.execSQL(
                    """
                UPDATE ropa

                SET cantidad =
                    cantidad - ?

                WHERE id = ?
                """.trimIndent(),

                    arrayOf(
                        cantidad,
                        idRopa
                    )
                )
            }


            // =============================
            // 4. CAMBIAR ESTADO
            // =============================

            val fecha =
                java.text.SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss",
                    java.util.Locale.getDefault()
                ).format(
                    java.util.Date()
                )


            val valores =
                android.content.ContentValues()
                    .apply {

                        put(
                            "estado",
                            "ATENDIDO"
                        )

                        put(
                            "fecha_atencion",
                            fecha
                        )
                    }


            db.update(
                "pedido",
                valores,
                "id = ?",
                arrayOf(
                    idPedido.toString()
                )
            )


            // TODO CORRECTO

            db.setTransactionSuccessful()

            return true


        } catch (
            e: Exception
        ) {

            return false

        } finally {

            db.endTransaction()
        }
    }
}