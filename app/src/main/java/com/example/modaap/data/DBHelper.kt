package com.example.modaap.data
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
class DBHelper (context: Context) :
SQLiteOpenHelper(
context,
DATABASE_NAME,
null,
DATABASE_VERSION
) {

    companion object {
        const val DATABASE_NAME = "modaapp.db"
        const val DATABASE_VERSION = 2
    }

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)

        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase) {

        // TABLA USUARIO
        db.execSQL(
            """
            CREATE TABLE usuario (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                usuario TEXT UNIQUE NOT NULL,
                clave TEXT NOT NULL,
                rol TEXT NOT NULL,
                telefono TEXT
            )
            """.trimIndent()
        )


        // TABLA CATEGORIA
        db.execSQL(
            """
            CREATE TABLE categoria (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT UNIQUE NOT NULL
            )
            """.trimIndent()
        )
        crearTablasPedidos(db)

        // TABLA ROPA
        db.execSQL(
            """
            CREATE TABLE ropa (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                modelo TEXT NOT NULL,
                id_categoria INTEGER NOT NULL,
                talla TEXT,
                marca TEXT,
                color TEXT,
                precio REAL CHECK(precio > 0),
                cantidad INTEGER CHECK(cantidad >= 0),
                foto TEXT,
                
                FOREIGN KEY(id_categoria)
                REFERENCES categoria(id)
            )
            """.trimIndent()
        )


        // USUARIO ADMINISTRADOR
        db.execSQL(
            """
            INSERT INTO usuario
            (usuario, clave, rol, telefono)
            VALUES
            ('admin', '1234', 'ADMIN', '')
            """.trimIndent()
        )


        // CATEGORÍAS
        db.execSQL("INSERT INTO categoria(nombre) VALUES ('Polos')")
        db.execSQL("INSERT INTO categoria(nombre) VALUES ('Pantalones')")
        db.execSQL("INSERT INTO categoria(nombre) VALUES ('Vestidos')")
        db.execSQL("INSERT INTO categoria(nombre) VALUES ('Casacas')")
        db.execSQL("INSERT INTO categoria(nombre) VALUES ('Zapatillas')")
    }


    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {

        if (oldVersion < 2) {

            crearTablasPedidos(db)
        }
    }
    private fun crearTablasPedidos(
        db: SQLiteDatabase
    ) {

        db.execSQL(
            """
        CREATE TABLE IF NOT EXISTS cliente (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            telefono TEXT UNIQUE NOT NULL,
            nombres TEXT NOT NULL,
            apellidos TEXT NOT NULL,
            fecha_registro TEXT NOT NULL
        )
        """.trimIndent()
        )


        db.execSQL(
            """
        CREATE TABLE IF NOT EXISTS pedido (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            id_cliente INTEGER NOT NULL,
            fecha TEXT NOT NULL,
            total REAL NOT NULL,
            estado TEXT NOT NULL DEFAULT 'PENDIENTE',
            fecha_atencion TEXT,

            FOREIGN KEY(id_cliente)
            REFERENCES cliente(id)
        )
        """.trimIndent()
        )


        db.execSQL(
            """
        CREATE TABLE IF NOT EXISTS detalle_pedido (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            id_pedido INTEGER NOT NULL,
            id_ropa INTEGER NOT NULL,
            cantidad INTEGER NOT NULL CHECK(cantidad > 0),
            precio_unit REAL NOT NULL,
            subtotal REAL NOT NULL,

            FOREIGN KEY(id_pedido)
            REFERENCES pedido(id)
            ON DELETE CASCADE,

            FOREIGN KEY(id_ropa)
            REFERENCES ropa(id)
        )
        """.trimIndent()
        )
    }

    fun validarUsuario(
        usuario: String,
        clave: String
    ): Boolean {

        val db = readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT id
            FROM usuario
            WHERE usuario = ?
            AND clave = ?
            """.trimIndent(),
            arrayOf(usuario, clave)
        )

        val existe = cursor.moveToFirst()

        cursor.close()

        return existe
    }


    fun obtenerRol(usuario: String): String {

        val db = readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT rol
            FROM usuario
            WHERE usuario = ?
            """.trimIndent(),
            arrayOf(usuario)
        )

        var rol = ""

        if (cursor.moveToFirst()) {
            rol = cursor.getString(
                cursor.getColumnIndexOrThrow("rol")
            )
        }

        cursor.close()

        return rol
    }
    fun listarCategorias(): ArrayList<Pair<Int, String>> {

        val lista =
            ArrayList<Pair<Int, String>>()

        val db =
            readableDatabase

        val cursor =
            db.rawQuery(
                """
            SELECT id, nombre
            FROM categoria
            ORDER BY id
            """.trimIndent(),
                null
            )


        while (cursor.moveToNext()) {

            val id =
                cursor.getInt(
                    cursor.getColumnIndexOrThrow("id")
                )

            val nombre =
                cursor.getString(
                    cursor.getColumnIndexOrThrow("nombre")
                )


            lista.add(
                Pair(id, nombre)
            )
        }


        cursor.close()

        return lista
    }
}