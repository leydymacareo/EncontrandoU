package com.leydymacareo.encontrandou.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

sealed class SessionState {
    object Loading : SessionState()
    object LoggedOut : SessionState()
    data class LoggedIn(
        val role: String,
        val name: String,
        val email: String,
        val sessionId: String
    ) : SessionState()
}


class SessionViewModel : ViewModel() {
    private val _sessionState = MutableStateFlow<SessionState>(SessionState.Loading)
    val sessionState: StateFlow<SessionState> = _sessionState

    fun generate5CharString(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..5)
            .map { chars.random() }
            .joinToString("")
    }

    fun setUserSession(role: String, name: String, email: String) {
        _sessionState.value = SessionState.LoggedIn(role, name, email, generate5CharString())
    }

    fun logout() {
        _sessionState.value = SessionState.LoggedOut
    }
}
