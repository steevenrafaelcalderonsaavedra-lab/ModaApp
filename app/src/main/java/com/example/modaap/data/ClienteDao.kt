package com.example.modaap.data
import android.content.ContentValues
import android.content.Context
import com.example.modaap.model.Cliente
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
class ClienteDao (
    context: Context
) {

    private val dbHelper =
        DBHelper(context)


    fun buscarPorTelefono(
        telefono: String
    ): Cliente? {

        val db =
            dbHelper.readableDatabase


        val cursor =
            db.rawQuery(
                """
                SELECT
                    id,
                    telefono,
                    nombres,
                    apellidos
                FROM cliente
                WHERE telefono = ?
                """.trimIndent(),

                arrayOf(telefono)
            )


        var cliente: Cliente? = null


        if (cursor.moveToFirst()) {

            cliente =
                Cliente(
                    id =
                        cursor.getInt(
                            cursor.getColumnIndexOrThrow("id")
                        ),

                    telefono =
                        cursor.getString(
                            cursor.getColumnIndexOrThrow("telefono")
                        ),

                    nombres =
                        cursor.getString(
                            cursor.getColumnIndexOrThrow("nombres")
                        ),

                    apellidos =
                        cursor.getString(
                            cursor.getColumnIndexOrThrow("apellidos")
                        )
                )
        }


        cursor.close()

        return cliente
    }


    fun insertar(
        telefono: String,
        nombres: String,
        apellidos: String
    ): Long {

        val db =
            dbHelper.writableDatabase


        val fecha =
            SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                Locale.getDefault()
            ).format(Date())


        val valores =
            ContentValues().apply {

                put(
                    "telefono",
                    telefono
                )

                put(
                    "nombres",
                    nombres
                )

                put(
                    "apellidos",
                    apellidos
                )

                put(
                    "fecha_registro",
                    fecha
                )
            }


        return db.insert(
            "cliente",
            null,
            valores
        )
    }
}