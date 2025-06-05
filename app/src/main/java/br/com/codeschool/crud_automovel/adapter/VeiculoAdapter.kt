package br.com.codeschool.crud_automovel.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.codeschool.crud_automovel.model.Veiculo

class VeiculoAdapter (
    private var veiculos: List<Veiculo>,
    private val aoClicarNoItem: (Veiculo) -> Unit
) : RecyclerView.Adapter<VeiculoAdapter.VeiculoViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int):
            VeiculoViewHolder {
    }
}

