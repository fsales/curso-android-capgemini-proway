package com.fsales.app.smartcontact.repository

import com.fsales.app.smartcontact.network.model.ViaCepResponse

interface CepRepository {
    /**
     * Busca dados do endereço para o [cep] informado (somente dígitos, 8 caracteres).
     *
     * @return [Result.success] com [ViaCepResponse] em caso de sucesso,
     *         [Result.failure] em caso de erro de rede ou CEP não encontrado.
     */
    suspend fun buscarCep(cep: String): Result<ViaCepResponse>
}


