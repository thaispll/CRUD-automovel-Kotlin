package br.com.codeschool.crud_automovel.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.codeschool.crud_automovel.R
import br.com.codeschool.crud_automovel.adapter.VeiculoAdapter
import br.com.codeschool.crud_automovel.databinding.ActivityListaVeiculoBinding
import br.com.codeschool.crud_automovel.viewmodel.AutenticacaoViewModel
import br.com.codeschool.crud_automovel.viewmodel.VeiculoViewModel

class ListaVeiculosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListaVeiculoBinding
    private lateinit var veiculoViewModel: VeiculoViewModel
    private lateinit var autenticacaoViewModel: AutenticacaoViewModel
    private lateinit var adapter: VeiculoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaVeiculoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.barraFerramentas)

        veiculoViewModel = ViewModelProvider(this).get(VeiculoViewModel::class.java)
        autenticacaoViewModel = ViewModelProvider(this).get(AutenticacaoViewModel::class.java)

        configurarRecyclerView()
        configurarListeners()
        configurarObservers()

        veiculoViewModel.carregarVeiculos()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            R.id.deslogar -> {
                autenticacaoViewModel.deslogar()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun configurarRecyclerView() {
        adapter = VeiculoAdapter(emptyList()) { veiculo ->
            val intent = Intent(this, DetalheVeiculoActivity::class.java)
            intent.putExtra("VEHICLE_ID", veiculo.id)
            startActivity(intent)
        }

        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = adapter
    }

    private fun configurarListeners() {
        binding.botaoFlutuante.setOnClickListener {
            startActivity(Intent(this, FormularioVeiculoActivity::class.java))
        }

        binding.svPesquisar.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.isNotEmpty()) {
                        veiculoViewModel.buscarVeiculos(it)
                    } else {
                        veiculoViewModel.carregarVeiculos()
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    if (it.isNotEmpty()) {
                        veiculoViewModel.buscarVeiculos(it)
                    } else {
                        veiculoViewModel.carregarVeiculos()
                    }
                }
                return true
            }
        })
    }

    private fun configurarObservers() {
        veiculoViewModel.veiculos.observe(this) { veiculos ->
            adapter.atualizarVeiculos(veiculos)
            binding.tvListaVazia.visibility = if (veiculos.isEmpty()) View.VISIBLE else View.GONE
        }

        veiculoViewModel.loading.observe(this) { carregando ->
            binding.barraDeProgresso.visibility = if (carregando) View.VISIBLE else View.GONE
        }

        veiculoViewModel.erro.observe(this) { erro ->
            erro?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                veiculoViewModel.limparErro()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        veiculoViewModel.carregarVeiculos()
    }
}
