package com.example.modaap.adapter
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.modaap.databinding.ItemCatalogoBinding
import com.example.modaap.model.Ropa
class CatalogoAdapter (
    private var lista: List<Ropa>,
    private val onAgregar: (Ropa) -> Unit
) :
    RecyclerView.Adapter<
            CatalogoAdapter.CatalogoViewHolder
            >() {


    class CatalogoViewHolder(
        val binding: ItemCatalogoBinding
    ) :
        RecyclerView.ViewHolder(
            binding.root
        )


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CatalogoViewHolder {

        val binding =
            ItemCatalogoBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ),
                parent,
                false
            )


        return CatalogoViewHolder(
            binding
        )
    }


    override fun onBindViewHolder(
        holder: CatalogoViewHolder,
        position: Int
    ) {

        val ropa =
            lista[position]


        holder.binding.tvModelo.text =
            ropa.modelo


        holder.binding.tvTalla.text =
            "Talla: ${ropa.talla}"


        holder.binding.tvPrecio.text =
            "S/ %.2f".format(
                ropa.precio
            )


        if (ropa.foto.isNotEmpty()) {

            val bitmap =
                BitmapFactory.decodeFile(
                    ropa.foto
                )

            holder.binding.imgProducto
                .setImageBitmap(bitmap)
        }
        holder.binding.btnAgregar.setOnClickListener {

            onAgregar(ropa)
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