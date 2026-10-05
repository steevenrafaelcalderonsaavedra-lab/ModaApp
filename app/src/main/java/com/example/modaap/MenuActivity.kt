package com.example.modaap

import android.os.Bundle
import android.content.Intent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.modaap.databinding.ActivityMenuBinding
class MenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // ROPA
        binding.cardRopa.setOnClickListener {

            val intent = Intent(
                this,
                RopaActivity::class.java
            )

            startActivity(intent)
        }


        // PEDIDOS
        binding.cardPedidos.setOnClickListener {

            val intent = Intent(
                this,
                PedidosActivity::class.java
            )

            startActivity(intent)
        }


        // CLIENTES
        binding.cardClientes.setOnClickListener {

            val intent = Intent(
                this,
                ClientesActivity::class.java
            )

            startActivity(intent)
        }


        // REPORTES
        binding.cardReportes.setOnClickListener {

            val intent = Intent(
                this,
                ReportesActivity::class.java
            )

            startActivity(intent)
        }


        // SALIR
        binding.btnSalir.setOnClickListener {

            val intent = Intent(
                this,
                LoginActivity::class.java
            )

            startActivity(intent)

            finish()
        }
    }
}