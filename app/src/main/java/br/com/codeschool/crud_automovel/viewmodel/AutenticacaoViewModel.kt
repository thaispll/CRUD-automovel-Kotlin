package br.com.codeschool.crud_automovel.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.google.firebase.auth.FirebaseAuth

class AutenticacaoViewModel: ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _erro = MutableLiveData<String?>()
    val erro: LiveData<String?> = _erro

    private val _usuarioLogado = MutableLiveData<Boolean>()
    val usuarioLogado: LiveData<Boolean> = _usuarioLogado

    private val _resetSenhaSucesso = MutableLiveData<Boolean>()
    val resetSenhaSucesso:LiveData<Boolean> = _resetSenhaSucesso

    private val _registradoComSucesso = MutableLiveData<Boolean>()
    val registradoComSucesso: LiveData<Boolean> = _registradoComSucesso

    fun login (email:String, senha: String) {
        viewModelScope.launch {
            _loading.value=true
            try{
                auth.signInWithEmailAndPassword(email, senha).await()
                _usuarioLogado.value = true
                _erro.value = null
            } catch (e:Exception){
                _usuarioLogado.value = false
                _erro.value= e.message
            } finally {
                _loading.value=false
            }
        }
    }

    fun registrar(email: String, senha: String) {
        viewModelScope.launch {
            _loading.value = true
            try{
                auth.createUserWithEmailAndPassword(email, senha).await()
                _registradoComSucesso.value = true
                _erro.value = null
            } catch (e:Exception){
                _registradoComSucesso.value = false
                _erro.value= e.message
            } finally {
                _loading.value=false
            }
        }
    }
    fun esqueciMinhaSenha(email:String){
        viewModelScope.launch {
            _loading.value = true
            try{
                auth.sendPasswordResetEmail(email).await()
                _resetSenhaSucesso.value = true
                _erro.value = null
            } catch (e:Exception){
                _resetSenhaSucesso.value = false
                _erro.value= e.message
            } finally {
                _loading.value=false
            }
        }
    }

    fun deslogar() {
        auth.signOut()
    }

    fun limparErro(){
        _erro.value = null
    }

    fun limparSucessoLogin(){
        _usuarioLogado.value = false
    }

    fun limparRegistroSucesso(){
        _registradoComSucesso.value = false
    }

    fun limparResetSenhaSucesso(){
        _resetSenhaSucesso.value = false
    }
}