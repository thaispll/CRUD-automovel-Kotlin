package br.com.codeschool.crud_automovel.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import br.com.codeschool.crud_automovel.databinding.ActivityLoginBinding
import br.com.codeschool.crud_automovel.viewmodel.AutenticacaoViewModel
import com.google.firebase.auth.FirebaseAuth


//Apresenta tela de login do app
class LoginActivity: AppCompatActivity() {
    //Variável para acessar os elementos da interface via ViewBinding
    private lateinit var binding: ActivityLoginBinding // binding = vinculacao

    //Variável para o ViewModel que gerencia a autenticação
    private lateinit var viewModel: AutenticacaoViewModel //viewmodel -modelo de autenticação

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(AutenticacaoViewModel::class.java)

        configurarListeners()
        configurarObservers()

        //Verificar se o usuário já está logado
        if(FirebaseAuth.getInstance().currentUser != null){
            navegarParaListaVeiculos()
        }
    }

    private fun configurarListeners() {
        binding.botaoLogin.setOnClickListener() {
            val email = binding.editEmail.text.toString().trim()
            val senha = binding.editSenha.text.toString().trim()

            if (validarEntradas(email, senha)) {
                viewModel.login(email, senha)
            }
        }
        //Ir para tela de registro
        binding.tvRegistrar.setOnClickListener() {
            startActivity(Intent(this, RegistroActivity::class.java))
        }

        binding.tvEsqueciSenha.setOnClickListener {
            val email = binding.editEmail.text.toString().trim()
            if (email.isNotEmpty()) {
                viewModel.esqueciMinhaSenha(email)
            } else {
                Toast.makeText(this, "Digite seu email para recuperar a senha", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun configurarObservers() {
        viewModel.loading.observe(this) { estaCarregando ->
            binding.barraDeProgresso.visibility = if (estaCarregando) View.VISIBLE else View.GONE
        }

        viewModel.erro.observe(this) { erro ->
            erro?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                viewModel.limparErro()
            }
        }
        viewModel.usuarioLogado.observe(this) { sucesso ->
            if (sucesso) {
                navegarParaListaVeiculos()
                viewModel.limparSucessoLogin()
            }
        }

        viewModel.resetSenhaSucesso.observe(this) { sucesso ->
            if (sucesso) {
                Toast.makeText(
                    this,
                    "E-mail de recuperação de senha enviado com sucesso",
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.limparResetSenhaSucesso()
            }
        }
    }

    private fun validarEntradas(email: String, senha: String): Boolean {
            var eValido = true

            if (email.isEmpty()) {
                binding.editEmail.error = "Campo obrigatório"
                eValido = false
            }

            if (senha.isEmpty()) {
                binding.editSenha.error = "Campo obrigatório"
                eValido = false
            }
            return eValido
        }
        private fun navegarParaListaVeiculos(){
            startActivity(Intent(this,ListaVeiculosActivity::class.java))
            finish()
        }
    }