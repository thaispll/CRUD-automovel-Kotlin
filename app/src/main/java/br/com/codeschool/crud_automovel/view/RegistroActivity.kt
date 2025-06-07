package br.com.codeschool.crud_automovel.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.codeschool.crud_automovel.databinding.ActivityRegisterBinding
import br.com.codeschool.crud_automovel.viewmodel.AutenticacaoViewModel

class RegistroActivity: AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: AutenticacaoViewModel

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(AutenticacaoViewModel::class.java)

        configurarListeners()
        configurarObservers()
    }

    private fun configurarListeners() {
        binding.botaoRegistro.setOnClickListener {
            val email = binding.editEmail.text.toString().trim()
            val senha = binding.editSenha.text.toString().trim()
            val confirmarSenha = binding.editConfirmeSenha.text.toString().trim()

            if (validarCampos(email, senha, confirmarSenha)){
                viewModel.registrar(email, senha)
            }
        }
        binding.tvLogin.setOnClickListener{
            finish()
        }
    }



    private fun configurarObservers() {
        viewModel.loading.observe(this) { loading ->
                binding.barraDeProgresso.visibility =
                    if (loading) View.VISIBLE else View.GONE
        }

        viewModel.erro.observe(this)  { erro ->
            erro?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                viewModel.limparErro()
            }
        }

        viewModel.registradoComSucesso.observe(this) { sucesso ->
            if (sucesso){
                Toast.makeText(this,"Cadastro realizado com sucesso", Toast.LENGTH_LONG).show()
               navegarParaListaVeiculos()
               viewModel.limparRegistroSucesso()
            }
        }
    }

    private fun validarCampos(email: String, senha: String, confirmarSenha: String): Boolean {
        var valido = true

        if (email.isEmpty()) {
            binding.editEmail.error = "Campo obrigatório"
            valido = false
        }

        if (senha.isEmpty()) {
            binding.editSenha.error = "Campo obrigatório"
            valido = false
        } else if (senha.length < 6) {
            binding.editSenha.error = "A senha deve ter pelo menos 6 caracteres"
            valido = false
        }

        if (confirmarSenha.isEmpty()) {
            binding.editConfirmeSenha.error = "Campo obrigatório"
            valido = false
        } else if (senha != confirmarSenha) {
            binding.editConfirmeSenha.error = "As senhas não coincidem"
            valido = false
        }

        return valido
    }

    private fun navegarParaListaVeiculos() {
        startActivity(Intent(this, ListaVeiculosActivity::class.java))
        finish()
    }
}