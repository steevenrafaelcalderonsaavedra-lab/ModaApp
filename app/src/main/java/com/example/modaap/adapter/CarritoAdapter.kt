package com.example.modaap.adapter
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.modaap.databinding.ItemCarritoBinding
import com.example.modaap.model.ItemCarrito
class CarritoAdapter(
    private var lista: List<ItemCarrito>,
    private val onLongClick: (ItemCarrito) -> Unit
) : RecyclerView.Adapter<CarritoAdapter.ViewHolder>() {


    class ViewHolder(
        val binding: ItemCarritoBinding
    ) : RecyclerView.ViewHolder(
        binding.root
    )


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val binding =
            ItemCarritoBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ),
                parent,
                false
            )

        return ViewHolder(binding)
    }


    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val item =
            lista[position]


        holder.binding.tvModelo.text =
            item.ropa.modelo

        holder.binding.tvTalla.text =
            "Talla: ${item.ropa.talla}"

        holder.binding.tvCantidad.text =
            "Cantidad: ${item.cantidad}"

        holder.binding.tvSubtotal.text =
            "Subtotal: S/ %.2f".format(
                item.subtotal()
            )


        if (item.ropa.foto.isNotEmpty()) {

            val bitmap =
                BitmapFactory.decodeFile(
                    item.ropa.foto
                )

            holder.binding.imgRopa
                .setImageBitmap(bitmap)
        }


        holder.binding.root
            .setOnLongClickListener {

                onLongClick(item)

                true
            }
    }


    override fun getItemCount(): Int {
        return lista.size
    }


    fun actualizar(
        nuevaLista: List<ItemCarrito>
    ) {

        lista = nuevaLista

        notifyDataSetChanged()
    }
}