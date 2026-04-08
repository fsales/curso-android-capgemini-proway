package com.fsales.app.e_aluno.ui.mapper

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.fsales.app.e_aluno.R
import com.fsales.app.e_aluno.domain.model.PeriodoTurno
import com.fsales.app.e_aluno.domain.model.Semestre

@Composable
fun PeriodoTurno.toUiText(): String {
    return when (this) {
        PeriodoTurno.MATUTINO -> stringResource(R.string.turno_matutino)
        PeriodoTurno.VESPERTINO -> stringResource(R.string.turno_vespertino)
        PeriodoTurno.NOTURNO -> stringResource(R.string.turno_noturno)
    }
}

@Composable
fun Semestre.toUiText(): String {
    return when (this) {
        Semestre.PRIMEIRO -> stringResource(R.string.semestre_primeiro)
        Semestre.SEGUNDO -> stringResource(R.string.semestre_segundo)
        Semestre.TERCEIRO -> stringResource(R.string.semestre_terceiro)
        Semestre.QUARTO -> stringResource(R.string.semestre_quarto)
        Semestre.QUINTO -> stringResource(R.string.semestre_quinto)
        Semestre.SEXTO -> stringResource(R.string.semestre_sexto)
        Semestre.SETIMO -> stringResource(R.string.semestre_setimo)
        Semestre.OITAVO -> stringResource(R.string.semestre_oitavo)
    }
}

