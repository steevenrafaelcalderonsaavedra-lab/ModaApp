package com.example.modaap.adapter
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.modaap.databinding.ItemRopaBinding
import com.example.modaap.model.Ropa
class RopaAdapter(
    private var lista: List<Ropa>,
    private val onClick: (Ropa) -> Unit
) :
    RecyclerView.Adapter<RopaAdapter.RopaViewHolder>() {


    class RopaViewHolder(
        val binding: ItemRopaBinding
    ) :
        RecyclerView.ViewHolder(
            binding.root
        )


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RopaViewHolder {

        val binding =
            ItemRopaBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ),
                parent,
                false
            )


        return RopaViewHolder(binding)
    }


    override fun onBindViewHolder(
        holder: RopaViewHolder,
        position: Int
    ) {

        val ropa =
            lista[position]


        holder.binding.tvModelo.text =
            ropa.modelo


        holder.binding.tvTalla.text =
            "Talla: ${ropa.talla}"


        holder.binding.tvColor.text =
            "Color: ${ropa.color}"


        holder.binding.tvCantidad.text =
            "Stock: ${ropa.cantidad}"


        if (ropa.foto.isNotEmpty()) {

            val bitmap =
                BitmapFactory.decodeFile(
                    ropa.foto
                )

            holder.binding.imgRopa
                .setImageBitmap(bitmap)
        }
        holder.binding.root.setOnClickListener {

            onClick(ropa)
        }
    }


    override fun getItemCount(): Int {

        return lista.size
    }


    fun actualizar(
        nuevaLista: List<Ropa>
    ) {

        lista = nuevaLista

        notifyDataSetChanged()
    }
}