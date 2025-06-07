package br.com.codeschool.crud_automovel.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import br.com.codeschool.crud_automovel.R
import br.com.codeschool.crud_automovel.databinding.ActivityFormularioVeiculoBinding
import br.com.codeschool.crud_automovel.model.Veiculo
import br.com.codeschool.crud_automovel.viewmodel.VeiculoViewModel

class FormularioVeiculoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormularioVeiculoBinding
    private lateinit var viewModel: VeiculoViewModel
    private var idVeiculo: String = ""
    private var modoEdicao: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityFormularioVeiculoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(VeiculoViewModel::class.java)

        idVeiculo = intent.getStringExtra("VEHICLE_ID") ?: ""
        modoEdicao = idVeiculo.isNotEmpty()

        configurarUI()
        configurarListeners()
        configurarObservers()

        if (modoEdicao) {
            viewModel.obterVeiculo(idVeiculo)
        }
    }

    private fun configurarUI() {
        binding.tvTitulo.text = if (modoEdicao) getString(R.string.edit_vehicle) else getString(R.string.add_vehicle)
    }

    private fun configurarListeners() {
        binding.botaoSalvar.setOnClickListener {
            if (validarCampos()) {
                salvarVeiculo()
            }
        }

        binding.botaoCancelar.setOnClickListener {
            finish()
        }
    }

    private fun configurarObservers() {
        viewModel.veiculo.observe(this) { veiculo ->
            veiculo?.let {
                preencherFormulario(it)
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
                Toast.makeText(
                    this,
                    if (modoEdicao) "Veículo atualizado com sucesso" else "Veículo cadastrado com sucesso",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
                viewModel.limparOperacaoSucesso()
            }
        }
    }

    private fun preencherFormulario(veiculo: Veiculo) {
        binding.editNomeProprietario.setText(veiculo.nomeProprietario)
        binding.editMarca.setText(veiculo.marca)
        binding.editModelo.setText(veiculo.modelo)
        binding.editPlaca.setText(veiculo.placa)
        binding.editAno.setText(veiculo.ano.toString())
        binding.editCor.setText(veiculo.cor)
        binding.editTipoCombustivel.setText(veiculo.tipoCombustivel)
        binding.editQuilometragem.setText(veiculo.quilometragem.toString())
        binding.editObservacoes.setText(veiculo.observacoes)
    }

    private fun validarCampos(): Boolean {
        var valido = true

        val nomeProprietario = binding.editNomeProprietario.text.toString().trim()
        val marca = binding.editMarca.text.toString().trim()
        val modelo = binding.editModelo.text.toString().trim()
        val placa = binding.editPlaca.text.toString().trim()
        val anoStr = binding.editAno.text.toString().trim()

        if (nomeProprietario.isEmpty()) {
            binding.editNomeProprietario.error = getString(R.string.required_field)
            valido = false
        }

        if (marca.isEmpty()) {
            binding.editMarca.error = getString(R.string.required_field)
            valido = false
        }

        if (modelo.isEmpty()) {
            binding.editModelo.error = getString(R.string.required_field)
            valido = false
        }

        if (placa.isEmpty()) {
            binding.editPlaca.error = getString(R.string.required_field)
            valido = false
        } else if (!validarPlaca(placa)) {
            binding.editPlaca.error = getString(R.string.invalid_plate)
            valido = false
        }

        if (anoStr.isEmpty()) {
            binding.editAno.error = getString(R.string.required_field)
            valido = false
        }

        return valido
    }

    private fun validarPlaca(placa: String): Boolean {
        // Validação simplificada para placa de veículo
        // Pode ser adaptada conforme padrões regionais (Brasil, Mercosul, etc.)
        return placa.length >= 6
    }

    private fun salvarVeiculo() {
        val nomeProprietario = binding.editNomeProprietario.text.toString().trim()
        val marca = binding.editMarca.text.toString().trim()
        val modelo = binding.editModelo.text.toString().trim()
        val placa = binding.editPlaca.text.toString().trim()
        val ano = binding.editAno.text.toString().trim().toIntOrNull() ?: 0
        val cor = binding.editCor.text.toString().trim()
        val tipoCombustivel = binding.editTipoCombustivel.text.toString().trim()
        val quilometragem = binding.editQuilometragem.text.toString().trim().toIntOrNull() ?: 0
        val observacoes = binding.editObservacoes.text.toString().trim()

        val veiculo = Veiculo(
            id = idVeiculo,
            nomeProprietario = nomeProprietario,
            marca = marca,
            modelo = modelo,
            placa = placa,
            ano = ano,
            cor = cor,
            tipoCombustivel = tipoCombustivel,
            quilometragem = quilometragem,
            observacoes = observacoes
        )

        if (modoEdicao) {
            viewModel.atualizarVeiculo(veiculo)
        } else {
            viewModel.adicionarVeiculo(veiculo)
        }
    }
}