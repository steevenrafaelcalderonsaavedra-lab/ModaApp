package com.example.modaap

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.modaap.data.ReporteDao
import com.example.modaap.databinding.ActivityReportesBinding

class ReportesActivity : AppCompatActivity() {
    private lateinit var binding:
            ActivityReportesBinding

    private lateinit var reporteDao:
            ReporteDao


    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        binding =
            ActivityReportesBinding.inflate(
                layoutInflater
            )

        setContentView(binding.root)


        reporteDao =
            ReporteDao(this)


        binding.btnActualizar
            .setOnClickListener {

                cargarReportes()
            }


        cargarReportes()
    }


    override fun onResume() {

        super.onResume()

        cargarReportes()
    }


    private fun cargarReportes() {

        val totalPedidos =
            reporteDao.totalPedidos()

        val pendientes =
            reporteDao.pedidosPendientes()

        val atendidos =
            reporteDao.pedidosAtendidos()

        val clientes =
            reporteDao.totalClientes()

        val totalRopa =
            reporteDao.totalRopa()

        val stock =
            reporteDao.totalStock()

        val stockBajo =
            reporteDao.stockBajo()

        val vendido =
            reporteDao.totalVendido()


        binding.tvTotalPedidos.text =
            "Total de pedidos: $totalPedidos"


        binding.tvPendientes.text =
            "Pendientes: $pendientes"


        binding.tvAtendidos.text =
            "Atendidos: $atendidos"


        binding.tvTotalVendido.text =
            "Total vendido: S/ %.2f"
                .format(vendido)


        binding.tvTotalRopa.text =
            "Modelos registrados: $totalRopa"


        binding.tvTotalStock.text =
            "Unidades disponibles: $stock"


        binding.tvStockBajo.text =
            "Productos con stock bajo: $stockBajo"


        binding.tvTotalClientes.text =
            "Clientes registrados: $clientes"
    }
}