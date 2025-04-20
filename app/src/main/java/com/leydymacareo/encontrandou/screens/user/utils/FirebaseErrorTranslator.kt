package com.leydymacareo.encontrandou.utils

import android.content.Context
import com.leydymacareo.encontrandou.R

fun traducirErrorFirebase(context: Context, error: String?): String {
    return when {
        error == null -> context.getString(R.string.error_unknown)
        "password is invalid" in error.lowercase() -> context.getString(R.string.error_password_invalid)
        "no user record" in error.lowercase() || "no user found" in error.lowercase() -> context.getString(R.string.error_user_not_found)
        "email address is badly formatted" in error.lowercase() -> context.getString(R.string.error_email_bad_format)
        "email already in use" in error.lowercase() -> context.getString(R.string.error_email_already_in_use)
        "weak password" in error.lowercase() -> context.getString(R.string.error_weak_password)
        "operation not allowed" in error.lowercase() -> context.getString(R.string.error_operation_not_allowed)
        "network error" in error.lowercase() -> context.getString(R.string.error_network)
        "too many requests" in error.lowercase() -> context.getString(R.string.error_too_many_requests)
        else -> error
    }
}
