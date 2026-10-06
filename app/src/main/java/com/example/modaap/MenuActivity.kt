package com.example.modaap

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.modaap.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =
            ActivityMenuBinding.inflate(
                layoutInflater
            )

        setContentView(binding.root)

        // ==========================================
        // ROPA
        // ==========================================

        binding.cardRopa.setOnClickListener {

            val intent = Intent(
                this,
                RopaActivity::class.java
            )

            startActivity(intent)
        }

        // ==========================================
        // PEDIDOS
        // ==========================================

        binding.cardPedidos.setOnClickListener {

            val intent = Intent(
                this,
                PedidosActivity::class.java
            )

            startActivity(intent)
        }

        // ==========================================
        // CLIENTES
        // ==========================================

        binding.cardClientes.setOnClickListener {

            val intent = Intent(
                this,
                ClientesActivity::class.java
            )

            startActivity(intent)
        }

        // ==========================================
        // REPORTES
        // ==========================================

        binding.cardReportes.setOnClickListener {

            val intent = Intent(
                this,
                ReportesActivity::class.java
            )

            startActivity(intent)
        }

        // ==========================================
        // SALIR
        // ==========================================

        binding.btnSalir.setOnClickListener {

            confirmarCerrarSesion()
        }
    }

    // ==========================================
    // CONFIRMAR CIERRE DE SESIÓN
    // ==========================================

    private fun confirmarCerrarSesion() {

        AlertDialog.Builder(this)
            .setTitle("Cerrar sesión")

            .setMessage(
                "¿Desea cerrar la sesión del administrador?"
            )

            .setPositiveButton(
                "Sí"
            ) { _, _ ->

                cerrarSesion()
            }

            .setNegativeButton(
                "Cancelar",
                null
            )

            .show()
    }

    // ==========================================
    // HU-13: CERRAR SESIÓN
    // ==========================================

    private fun cerrarSesion() {

        val preferencias =
            getSharedPreferences(
                "ModaAppPrefs",
                MODE_PRIVATE
            )

        // Desactivar sesión
        preferencias
            .edit()
            .putBoolean(
                "sesion_admin",
                false
            )
            .apply()

        // Volver al Login
        val intent = Intent(
            this,
            LoginActivity::class.java
        )

        // Borrar las pantallas anteriores
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)

        finish()
    }
}