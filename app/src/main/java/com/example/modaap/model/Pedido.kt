package com.example.modaap.model

data class Pedido(val id: Int,
                  val idCliente: Int,
                  val cliente: String,
                  val telefono: String,
                  val fecha: String,
                  val total: Double,
                  val estado: String
)
