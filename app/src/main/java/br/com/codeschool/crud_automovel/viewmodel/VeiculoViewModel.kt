package br.com.codeschool.crud_automovel.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.codeschool.crud_automovel.model.Veiculo
import br.com.codeschool.crud_automovel.repository.VeiculoRepository
import kotlinx.coroutines.launch

class VeiculoViewModel: ViewModel() {
    private val repository = VeiculoRepository()

    private val _veiculos = MutableLiveData<List<Veiculo>>()
    val veiculos: LiveData<List<Veiculo>> = _veiculos

    private val _veiculo = MutableLiveData<Veiculo?>()
    val veiculo: LiveData<Veiculo?> = _veiculo

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _erro = MutableLiveData<String?>()
    val erro: LiveData<String?> = _erro

    private val _operacaoSucesso = MutableLiveData<Boolean>()
    val operacaoSucesso: LiveData<Boolean> = _operacaoSucesso

    fun carregarVeiculos(){
        viewModelScope.launch {
            _loading.value = true
            try{
                val listaVeiculos = repository.obterTodosVeiculos()
                _veiculos.value = listaVeiculos
                _erro.value = null
            } catch (e:Exception){
                _erro.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun obterVeiculo(id: String) {
        viewModelScope.launch{
            _loading.value = true
            try {
                val dadosVeiculo = repository.obterVeiculoPorId(id)
                _veiculo.value = dadosVeiculo
                _erro.value = null
            } catch (e:Exception) {
                _erro.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun adicionarVeiculo(veiculo: Veiculo) {
        viewModelScope.launch{
            _loading.value = true
            try {
                val id = repository.adicionarVeiculo(veiculo)
                _operacaoSucesso.value =  id != null
                _erro.value = null
            } catch (e:Exception) {
                _operacaoSucesso.value=false
                _erro.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun atualizarVeiculo(veiculo: Veiculo) {
        viewModelScope.launch{
            _loading.value = true
            try {
                val sucesso = repository.atualizarVeiculo(veiculo)
                _operacaoSucesso.value =  sucesso
                _erro.value = null
            } catch (e:Exception) {
                _operacaoSucesso.value=false
                _erro.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }
    fun deletarVeiculo(id: String) {
        viewModelScope.launch{
            _loading.value = true
            try {
                val sucesso = repository.deletarVeiculo(id)
                _operacaoSucesso.value =  sucesso
                _erro.value = null
            } catch (e:Exception) {
                _operacaoSucesso.value=false
                _erro.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }
    fun buscarVeiculos(consulta: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val listaVeiculos = repository.buscarVeiculos(consulta)
                _veiculos.value = listaVeiculos
                _erro.value = null
            } catch (e: Exception) {
                _erro.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun limparErro(){
        _erro.value = null
    }

    fun limparOperacaoSucesso(){
        _operacaoSucesso.value = false
    }

}