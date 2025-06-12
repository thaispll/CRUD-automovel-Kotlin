package br.com.codeschool.crud_automovel.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.codeschool.crud_automovel.databinding.ItemVeiculoBinding
import br.com.codeschool.crud_automovel.model.Veiculo
import br.com.codeschool.crud_automovel.viewmodel.VeiculoViewModel


class VeiculoAdapter (
    private var veiculos: List<Veiculo>,
    private val aoClicarNoItem: (Veiculo) -> Unit
) : RecyclerView.Adapter<VeiculoAdapter.VeiculoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VeiculoViewHolder {
        val binding = ItemVeiculoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return VeiculoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VeiculoViewHolder, position: Int) {
        val veiculo = veiculos[position]
        holder.vincular(veiculo)
    }

    override fun getItemCount(): Int = veiculos.size

    fun atualizarVeiculos(novosVeiculos: List<Veiculo>) {
        veiculos = novosVeiculos
        notifyDataSetChanged()
    }

    inner class VeiculoViewHolder(private val binding: ItemVeiculoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val posicao = adapterPosition
                if (posicao != RecyclerView.NO_POSITION) {
                    aoClicarNoItem(veiculos[posicao])
                }
            }
        }

        fun vincular(veiculo: Veiculo) {
            binding.tvProprietario.text = veiculo.nomeProprietario
            binding.tvInformacoesVeiculo.text =
                "${veiculo.marca} ${veiculo.modelo} - ${veiculo.placa}"
            binding.tvAno.text = "Ano: ${veiculo.ano}"
        }
    }
}

