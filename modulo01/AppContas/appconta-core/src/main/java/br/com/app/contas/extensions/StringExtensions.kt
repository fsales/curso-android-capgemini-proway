package br.com.app.contas.extensions

import br.com.app.contas.presentation.messages.Messages
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val DATE_FORMAT =
    DateTimeFormatter.ofPattern("dd/MM/yyyy")

fun String.toDate(): LocalDate ? =  runCatching {
    LocalDate.parse(this, DATE_FORMAT)
}.onFailure{
    e -> throw IllegalArgumentException(Messages.DATA_INVALIDA, e)
}.getOrNull()

fun LocalDate.toBrazilDateString(): String = format(DATE_FORMAT)