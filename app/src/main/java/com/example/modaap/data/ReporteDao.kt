package com.example.modaap.data
import android.content.Context
class ReporteDao(
    context: Context
) {

    private val dbHelper =
        DBHelper(context)


    // =============================
    // TOTAL DE PEDIDOS
    // =============================

    fun totalPedidos(): Int {

        val db =
            dbHelper.readableDatabase

        val cursor =
            db.rawQuery(
                """
                SELECT COUNT(*) AS total
                FROM pedido
                """.trimIndent(),
                null
            )

        var total = 0

        if (cursor.moveToFirst()) {

            total =
                cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                        "total"
                    )
                )
        }

        cursor.close()

        return total
    }


    // =============================
    // PEDIDOS PENDIENTES
    // =============================

    fun pedidosPendientes(): Int {

        val db =
            dbHelper.readableDatabase

        val cursor =
            db.rawQuery(
                """
                SELECT COUNT(*) AS total
                FROM pedido
                WHERE estado = 'PENDIENTE'
                """.trimIndent(),
                null
            )

        var total = 0

        if (cursor.moveToFirst()) {

            total =
                cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                        "total"
                    )
                )
        }

        cursor.close()

        return total
    }


    // =============================
    // PEDIDOS ATENDIDOS
    // =============================

    fun pedidosAtendidos(): Int {

        val db =
            dbHelper.readableDatabase

        val cursor =
            db.rawQuery(
                """
                SELECT COUNT(*) AS total
                FROM pedido
                WHERE estado = 'ATENDIDO'
                """.trimIndent(),
                null
            )

        var total = 0

        if (cursor.moveToFirst()) {

            total =
                cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                        "total"
                    )
                )
        }

        cursor.close()

        return total
    }


    // =============================
    // TOTAL CLIENTES
    // =============================

    fun totalClientes(): Int {

        val db =
            dbHelper.readableDatabase

        val cursor =
            db.rawQuery(
                """
                SELECT COUNT(*) AS total
                FROM cliente
                """.trimIndent(),
                null
            )

        var total = 0

        if (cursor.moveToFirst()) {

            total =
                cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                        "total"
                    )
                )
        }

        cursor.close()

        return total
    }


    // =============================
    // TOTAL DE MODELOS DE ROPA
    // =============================

    fun totalRopa(): Int {

        val db =
            dbHelper.readableDatabase

        val cursor =
            db.rawQuery(
                """
                SELECT COUNT(*) AS total
                FROM ropa
                """.trimIndent(),
                null
            )

        var total = 0

        if (cursor.moveToFirst()) {

            total =
                cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                        "total"
                    )
                )
        }

        cursor.close()

        return total
    }


    // =============================
    // TOTAL UNIDADES EN STOCK
    // =============================

    fun totalStock(): Int {

        val db =
            dbHelper.readableDatabase

        val cursor =
            db.rawQuery(
                """
                SELECT
                    COALESCE(SUM(cantidad), 0)
                    AS total
                FROM ropa
                """.trimIndent(),
                null
            )

        var total = 0

        if (cursor.moveToFirst()) {

            total =
                cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                        "total"
                    )
                )
        }

        cursor.close()

        return total
    }


    // =============================
    // STOCK BAJO
    // Consideramos <= 5 unidades
    // =============================

    fun stockBajo(): Int {

        val db =
            dbHelper.readableDatabase

        val cursor =
            db.rawQuery(
                """
                SELECT COUNT(*) AS total
                FROM ropa
                WHERE cantidad <= 5
                """.trimIndent(),
                null
            )

        var total = 0

        if (cursor.moveToFirst()) {

            total =
                cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                        "total"
                    )
                )
        }

        cursor.close()

        return total
    }


    // =============================
    // TOTAL VENDIDO
    // SOLO PEDIDOS ATENDIDOS
    // =============================

    fun totalVendido(): Double {

        val db =
            dbHelper.readableDatabase

        val cursor =
            db.rawQuery(
                """
                SELECT
                    COALESCE(SUM(total), 0)
                    AS total_vendido
                FROM pedido
                WHERE estado = 'ATENDIDO'
                """.trimIndent(),
                null
            )

        var total = 0.0

        if (cursor.moveToFirst()) {

            total =
                cursor.getDouble(
                    cursor.getColumnIndexOrThrow(
                        "total_vendido"
                    )
                )
        }

        cursor.close()

        return total
    }
}