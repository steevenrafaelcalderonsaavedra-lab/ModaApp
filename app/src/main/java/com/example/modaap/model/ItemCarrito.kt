package com.example.modaap.model

data class ItemCarrito(
    val ropa: Ropa,
    var cantidad: Int
) {
    fun subtotal(): Double {
        return ropa.precio * cantidad
    }
}
