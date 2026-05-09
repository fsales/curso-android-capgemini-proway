package com.fsales.app.smartcontact.repository

import com.fsales.app.smartcontact.network.model.ViaCepResponse
import com.fsales.app.smartcontact.network.service.ViaCepService
import javax.inject.Inject

class CepRepositoryImpl @Inject constructor(
    private val viaCepService: ViaCepService,
) : CepRepository {

    /**
     * Retrofit 3.x: suspending functions lançam [retrofit2.HttpException] em erros HTTP
     * e [java.io.IOException] em falhas de rede. O [runCatching] captura ambos.
     */
    override suspend fun buscarCep(cep: String): Result<ViaCepResponse> =
        runCatching {
            val resposta = viaCepService.buscarCep(cep)

            if (resposta.erro) {
                throw CepException.CepNaoEncontrado(cep)
            }

            resposta
        }.recoverCatching { throwable ->
            // Relança como nossas exceções de domínio para não vazar Retrofit no ViewModel
            throw when (throwable) {
                is CepException -> throwable
                else -> CepException.ErroRede(throwable.message ?: "desconhecido")
            }
        }
}

/** Exceções específicas do domínio de CEP. */
sealed class CepException(message: String) : Exception(message) {
    /** CEP digitado não foi encontrado na base ViaCEP (`"erro": true`). */
    class CepNaoEncontrado(cep: String) : CepException("CEP $cep não encontrado")
    /** Falha de rede ou HTTP inesperado. */
    class ErroRede(detail: String) : CepException("Erro de rede: $detail")
}

