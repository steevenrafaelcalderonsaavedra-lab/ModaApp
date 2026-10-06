package com.example.modaap.util
import com.example.modaap.model.ItemCarrito
import com.example.modaap.model.Ropa
object Carrito {

    val items = mutableListOf<ItemCarrito>()


    fun agregar(
        ropa: Ropa,
        cantidad: Int
    ): Boolean {

        if (cantidad <= 0) {
            return false
        }

        if (cantidad > ropa.cantidad) {
            return false
        }


        val existente =
            items.find {
                it.ropa.id == ropa.id
            }


        if (existente != null) {

            val nuevaCantidad =
                existente.cantidad + cantidad

            if (nuevaCantidad > ropa.cantidad) {
                return false
            }

            existente.cantidad =
                nuevaCantidad

        } else {

            items.add(
                ItemCarrito(
                    ropa,
                    cantidad
                )
            )
        }

        return true
    }


    fun eliminar(item: ItemCarrito) {
        items.remove(item)
    }


    fun total(): Double {

        return items.sumOf {
            it.subtotal()
        }
    }


    fun cantidadProductos(): Int {

        return items.sumOf {
            it.cantidad
        }
    }


    fun estaVacio(): Boolean {

        return items.isEmpty()
    }


    fun vaciar() {
        items.clear()
    }
}