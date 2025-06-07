package br.com.codeschool.crud_automovel.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import br.com.codeschool.crud_automovel.databinding.ActivityDetalheVeiculoBinding
import br.com.codeschool.crud_automovel.model.Veiculo
import br.com.codeschool.crud_automovel.viewmodel.VeiculoViewModel



class DetalheVeiculoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalheVeiculoBinding
    private lateinit var viewModel: VeiculoViewModel
    private var idVeiculo: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalheVeiculoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(VeiculoViewModel::class.java)

        idVeiculo = intent.getStringExtra("VEHICLE_ID") ?: ""
        if (idVeiculo.isEmpty()) {
            Toast.makeText(this, "Erro ao carregar veículo", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        configurarListeners()
        configurarObservers()

        viewModel.obterVeiculo(idVeiculo)
    }

    private fun configurarListeners() {
        binding.botaoEditar.setOnClickListener {
            val intent = android.content.Intent(this, FormularioVeiculoActivity::class.java)
            intent.putExtra("VEHICLE_ID", idVeiculo)
            startActivity(intent)
        }

        binding.botaoDeletar.setOnClickListener {
            mostrarDialogConfirmacaoExclusao()
        }
    }

    private fun configurarObservers() {
        viewModel.veiculo.observe(this) { veiculo ->
            veiculo?.let {
                exibirDetalhesVeiculo(it)
            }
        }

        viewModel.loading.observe(this) { carregando ->
            binding.barraDeProgresso.visibility = if (carregando) View.VISIBLE else View.GONE
        }

        viewModel.erro.observe(this) { erro ->
            erro?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                viewModel.limparErro()
            }
        }

        viewModel.operacaoSucesso.observe(this) { sucesso ->
            if (sucesso) {
                Toast.makeText(this, "Veículo excluído com sucesso", Toast.LENGTH_SHORT).show()
                finish()
                viewModel.limparOperacaoSucesso()
            }
        }
    }

    private fun exibirDetalhesVeiculo(veiculo: Veiculo) {
        binding.tvProprietario.text = veiculo.nomeProprietario
        binding.tvMarca.text = veiculo.marca
        binding.tvModelo.text = veiculo.modelo
        binding.tvPlaca.text = veiculo.placa
        binding.tvAno.text = veiculo.ano.toString()
        binding.tvCor.text = veiculo.cor
        binding.tvCombustVel.text = veiculo.tipoCombustivel
        binding.tvQuilometragem.text = veiculo.quilometragem.toString()
        binding.tvObservacoes.text = veiculo.observacoes
    }

    private fun mostrarDialogConfirmacaoExclusao() {
        AlertDialog.Builder(this)
            .setTitle("Excluir Veículo")
            .setMessage("Tem certeza que deseja excluir este veículo?")
            .setPositiveButton("Sim") { _, _ ->
                viewModel.deletarVeiculo(idVeiculo)
            }
            .setNegativeButton("Não", null)
            .show()
    }
}