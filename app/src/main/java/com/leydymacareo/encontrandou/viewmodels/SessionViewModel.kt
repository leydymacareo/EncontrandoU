// SessionViewModel.kt
package com.leydymacareo.encontrandou.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

sealed class SessionState {
    object Loading : SessionState()
    object LoggedOut : SessionState()
    data class LoggedIn(val role: String) : SessionState()
}

class SessionViewModel : ViewModel() {
    private val _sessionState = MutableStateFlow<SessionState>(SessionState.Loading)
    val sessionState: StateFlow<SessionState> = _sessionState

    fun setUserSession(role: String) {
        _sessionState.value = SessionState.LoggedIn(role)
    }

    fun logout() {
        _sessionState.value = SessionState.LoggedOut
    }
}
