package br.com.codeschool.crud_automovel.model

data class Veiculo (
    val id: String = "",
    val nomeProprietario: String = "",
    val marca: String = "",
    val modelo: String = "",
    val placa: String = "",
    val ano: Int = 0,
    val cor: String = "",
    val tipoCombustivel: String = "",
    val quilometragem: Int = 0,
    val observacoes: String = ""
)
