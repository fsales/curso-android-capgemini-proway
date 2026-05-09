package com.fsales.app.smartcontact.network.model

import kotlinx.serialization.Serializable

/**
 * Modelo de resposta da API ViaCEP (https://viacep.com.br/).
 *
 * Exemplo de retorno:
 * {
 *   "cep": "71200-020",
 *   "logradouro": "Trecho SIA Trecho 2",
 *   "bairro": "Zona Industrial (Guará)",
 *   "localidade": "Brasília",
 *   "uf": "DF",
 *   "estado": "Distrito Federal",
 *   "erro": true          ← presente somente quando o CEP não existe
 * }
 */
@Serializable
data class ViaCepResponse(
    val cep: String = "",
    val logradouro: String = "",
    val complemento: String = "",
    val bairro: String = "",
    /** Cidade */
    val localidade: String = "",
    /** Sigla do estado (ex: "DF") */
    val uf: String = "",
    /** Nome completo do estado (ex: "Distrito Federal") */
    val estado: String = "",
    val regiao: String = "",
    val ibge: String = "",
    val gia: String = "",
    val ddd: String = "",
    val siafi: String = "",
    /** Presente e `true` quando o CEP não é encontrado */
    val erro: Boolean = false,
)

