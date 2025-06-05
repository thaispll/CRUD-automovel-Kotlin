package br.com.codeschool.crud_automovel.repository

import br.com.codeschool.crud_automovel.model.Veiculo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

//Responsável por gerenciar e fornecer dados ao ViewModel
class VeiculoRepository {
    private val db = FirebaseFirestore.getInstance()
    private val colecaoVeiculos = db.collection("veiculos")

    suspend fun obterVeiculoPorId(id:String): Veiculo? {
        return try {
            val documento = colecaoVeiculos.document(id).get().await()
            val veiculo = documento.toObject<Veiculo>()
            veiculo?.copy(id= documento.id)
        } catch (e:Exception) {
            null
        }
    }

    suspend fun obterTodosVeiculos(): List<Veiculo>{
        return try {
            val snapshot = colecaoVeiculos.get().await()
            snapshot.documents.mapNotNull { documento ->
                val veiculo = documento.toObject<Veiculo>()
                veiculo?.copy(id = documento.id)
            }
        } catch (e:Exception){
            emptyList()
        }
    }
    suspend fun adicionarVeiculo(veiculo: Veiculo):String? {
        return try{
            val referenciaDocumento = colecaoVeiculos.add(veiculo).await()
            referenciaDocumento.id
        } catch (e: Exception) {
            null
        }
    }

    suspend fun atualizarVeiculo(veiculo:Veiculo): Boolean {
        return try{
            colecaoVeiculos.document(veiculo.id).set(veiculo).await()
            true
        } catch(e: Exception){
            false
        }
    }

    suspend fun deletarVeiculo(id:String): Boolean {
        return try{
            colecaoVeiculos.document(id).delete().await()
            true
        } catch (e: Exception){
            false
        }
    }

    suspend fun buscarVeiculos(consulta:String): List<Veiculo> {
        return try {
            val resultadosProprietario =
                colecaoVeiculos.whereGreaterThanOrEqualTo("nomeProprietario", consulta)
                    .whereLessThanOrEqualTo("nomeProprietario", consulta + "\uf8ff")
                    .get().await()

            val resultadosMarca = colecaoVeiculos.whereGreaterThanOrEqualTo("marca", consulta)
                .whereLessThanOrEqualTo("marca", consulta + "\uf8ff")
                .get().await()

            val resultadosModelo = colecaoVeiculos.whereGreaterThanOrEqualTo("modelo", consulta)
                .whereLessThanOrEqualTo("modelo", consulta + "\uf8ff")
                .get().await()

            val resultadosPlaca = colecaoVeiculos.whereGreaterThanOrEqualTo("placa", consulta)
                .whereLessThanOrEqualTo("placa", consulta + "\uf8ff")
                .get().await()

            val mapaResultados = mutableMapOf<String,Veiculo>()

            listOf(resultadosProprietario,resultadosMarca,resultadosModelo,resultadosPlaca)
                .forEach{ snapshot ->
                    snapshot.documents.forEach { documento ->
                        val veiculo = documento.toObject<Veiculo>()
                        if (veiculo != null){
                            mapaResultados[documento.id] = veiculo.copy(id = documento.id)
                        }
                    }
                }
            mapaResultados.values.toList()

        } catch (e:Exception){
            emptyList()
        }
    }
}