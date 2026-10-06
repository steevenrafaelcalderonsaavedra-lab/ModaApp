package com.example.modaap.model

data class DetallePedido(
    val id: Int,
    val idPedido: Int,
    val idRopa: Int,
    val modelo: String,
    val talla: String,
    val cantidad: Int,
    val precioUnit: Double,
    val subtotal: Double
)
