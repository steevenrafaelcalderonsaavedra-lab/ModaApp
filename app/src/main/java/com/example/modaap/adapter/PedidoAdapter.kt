package com.example.modaap.adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.modaap.databinding.ItemPedidoBinding
import com.example.modaap.model.Pedido
class PedidoAdapter (
    private var lista: List<Pedido>,
    private val onClick: (Pedido) -> Unit
) : RecyclerView.Adapter<PedidoAdapter.ViewHolder>() {


    class ViewHolder(
        val binding: ItemPedidoBinding
    ) : RecyclerView.ViewHolder(
        binding.root
    )


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val binding =
            ItemPedidoBinding.inflate(
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

        val pedido =
            lista[position]


        holder.binding.tvNumeroPedido.text =
            "Pedido #${pedido.id}"

        holder.binding.tvCliente.text =
            "Cliente: ${pedido.cliente}"

        holder.binding.tvTelefono.text =
            "Teléfono: ${pedido.telefono}"

        holder.binding.tvFecha.text =
            "Fecha: ${pedido.fecha}"

        holder.binding.tvTotal.text =
            "Total: S/ %.2f"
                .format(pedido.total)

        holder.binding.tvEstado.text =
            pedido.estado


        holder.binding.root
            .setOnClickListener {

                onClick(pedido)
            }
    }


    override fun getItemCount(): Int {
        return lista.size
    }


    fun actualizar(
        nuevaLista: List<Pedido>
    ) {

        lista = nuevaLista

        notifyDataSetChanged()
    }
}