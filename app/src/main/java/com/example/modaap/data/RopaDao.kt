package com.example.modaap.data
import android.content.ContentValues
import android.content.Context
import com.example.modaap.model.Ropa
class RopaDao (context: Context) {

    private val dbHelper =
        DBHelper(context)


    fun insertar(
        modelo: String,
        idCategoria: Int,
        talla: String,
        marca: String,
        color: String,
        precio: Double,
        cantidad: Int,
        foto: String
    ): Long {

        val db =
            dbHelper.writableDatabase


        val valores =
            ContentValues().apply {

                put("modelo", modelo)

                put(
                    "id_categoria",
                    idCategoria
                )

                put("talla", talla)

                put("marca", marca)

                put("color", color)

                put("precio", precio)

                put(
                    "cantidad",
                    cantidad
                )

                put("foto", foto)
            }


        return db.insert(
            "ropa",
            null,
            valores
        )
    }


    fun listar(): ArrayList<Ropa> {

        val lista =
            ArrayList<Ropa>()


        val db =
            dbHelper.readableDatabase


        val cursor =
            db.rawQuery(
                """
                SELECT
                    r.id,
                    r.modelo,
                    r.id_categoria,
                    c.nombre AS categoria,
                    r.talla,
                    r.marca,
                    r.color,
                    r.precio,
                    r.cantidad,
                    r.foto
                    
                FROM ropa r
                
                INNER JOIN categoria c
                ON r.id_categoria = c.id
                
                ORDER BY r.id DESC
                """.trimIndent(),
                null
            )


        while (cursor.moveToNext()) {

            lista.add(

                Ropa(

                    id =
                        cursor.getInt(
                            cursor.getColumnIndexOrThrow("id")
                        ),

                    modelo =
                        cursor.getString(
                            cursor.getColumnIndexOrThrow("modelo")
                        ),

                    idCategoria =
                        cursor.getInt(
                            cursor.getColumnIndexOrThrow("id_categoria")
                        ),

                    categoria =
                        cursor.getString(
                            cursor.getColumnIndexOrThrow("categoria")
                        ),

                    talla =
                        cursor.getString(
                            cursor.getColumnIndexOrThrow("talla")
                        ),

                    marca =
                        cursor.getString(
                            cursor.getColumnIndexOrThrow("marca")
                        ),

                    color =
                        cursor.getString(
                            cursor.getColumnIndexOrThrow("color")
                        ),

                    precio =
                        cursor.getDouble(
                            cursor.getColumnIndexOrThrow("precio")
                        ),

                    cantidad =
                        cursor.getInt(
                            cursor.getColumnIndexOrThrow("cantidad")
                        ),

                    foto =
                        cursor.getString(
                            cursor.getColumnIndexOrThrow("foto")
                        )
                )
            )
        }


        cursor.close()

        return lista
    }


    fun listarDisponibles(
        idCategoria: Int? = null
    ): ArrayList<Ropa> {

        val lista =
            ArrayList<Ropa>()

        val db =
            dbHelper.readableDatabase


        val consulta: String

        val argumentos: Array<String>?


        if (
            idCategoria == null ||
            idCategoria == 0
        ) {

            consulta =
                """
                SELECT
                    r.id,
                    r.modelo,
                    r.id_categoria,
                    c.nombre AS categoria,
                    r.talla,
                    r.marca,
                    r.color,
                    r.precio,
                    r.cantidad,
                    r.foto
                    
                FROM ropa r
                
                INNER JOIN categoria c
                ON r.id_categoria = c.id
                
                WHERE r.cantidad > 0
                
                ORDER BY r.id DESC
                """.trimIndent()

            argumentos = null

        } else {

            consulta =
                """
                SELECT
                    r.id,
                    r.modelo,
                    r.id_categoria,
                    c.nombre AS categoria,
                    r.talla,
                    r.marca,
                    r.color,
                    r.precio,
                    r.cantidad,
                    r.foto
                    
                FROM ropa r
                
                INNER JOIN categoria c
                ON r.id_categoria = c.id
                
                WHERE r.cantidad > 0
                AND r.id_categoria = ?
                
                ORDER BY r.id DESC
                """.trimIndent()

            argumentos =
                arrayOf(
                    idCategoria.toString()
                )
        }


        val cursor =
            db.rawQuery(
                consulta,
                argumentos
            )


        while (cursor.moveToNext()) {

            lista.add(

                Ropa(

                    cursor.getInt(
                        cursor.getColumnIndexOrThrow("id")
                    ),

                    cursor.getString(
                        cursor.getColumnIndexOrThrow("modelo")
                    ),

                    cursor.getInt(
                        cursor.getColumnIndexOrThrow("id_categoria")
                    ),

                    cursor.getString(
                        cursor.getColumnIndexOrThrow("categoria")
                    ),

                    cursor.getString(
                        cursor.getColumnIndexOrThrow("talla")
                    ),

                    cursor.getString(
                        cursor.getColumnIndexOrThrow("marca")
                    ),

                    cursor.getString(
                        cursor.getColumnIndexOrThrow("color")
                    ),

                    cursor.getDouble(
                        cursor.getColumnIndexOrThrow("precio")
                    ),

                    cursor.getInt(
                        cursor.getColumnIndexOrThrow("cantidad")
                    ),

                    cursor.getString(
                        cursor.getColumnIndexOrThrow("foto")
                    )
                )
            )
        }


        cursor.close()

        return lista
    }
}