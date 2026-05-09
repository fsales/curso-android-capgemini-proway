package com.fsales.app.smartcontact.network.service

import com.fsales.app.smartcontact.network.model.ViaCepResponse
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Interface Retrofit para a API ViaCEP.
 * Base URL: https://viacep.com.br/
 *
 * Retrofit 3.x: suspend functions retornam o tipo diretamente;
 * erros HTTP lançam [retrofit2.HttpException].
 */
interface ViaCepService {

    /**
     * Busca informações de endereço a partir de um CEP numérico de 8 dígitos.
     *
     * @param cep CEP sem formatação (somente dígitos), ex: "71200020"
     */
    @GET("ws/{cep}/json/")
    suspend fun buscarCep(@Path("cep") cep: String): ViaCepResponse
}

