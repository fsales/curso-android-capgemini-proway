package com.fsales.app.smartcontact.ui.presentation.error

import android.util.Log
import androidx.lifecycle.ViewModel

private const val DEFAULT_TAG = "ViewModel"

internal fun ViewModel.logTechnicalError(
    action: String,
    throwable: Throwable,
    tag: String = this::class.java.simpleName.takeIf { it.isNotBlank() } ?: DEFAULT_TAG,
) {
    Log.e(tag, "Falha ao $action", throwable)
}
