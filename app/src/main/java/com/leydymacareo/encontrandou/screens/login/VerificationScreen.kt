package com.leydymacareo.encontrandou.screens.login

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerificationScreen(
    onBack: () -> Unit = {},
    onVerified: () -> Unit = {}
) {
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var isLoading by remember { mutableStateOf(false) }
    var resendEnabled by remember { mutableStateOf(false) }
    var secondsLeft by remember { mutableStateOf(40) }

    // Temporizador para reenviar correo
    LaunchedEffect(Unit) {
        resendEnabled = false
        secondsLeft = 40
        while (secondsLeft > 0) {
            delay(1000)
            secondsLeft--
        }
        resendEnabled = true
    }

    fun checkVerification() {
        isLoading = true
        val user = auth.currentUser
        if (user == null) {
            Toast.makeText(context, "No hay sesión activa. Inicia sesión de nuevo.", Toast.LENGTH_LONG).show()
            isLoading = false
            return
        }

        user.reload().addOnCompleteListener { reloadTask ->
            if (reloadTask.isSuccessful) {
                if (user.isEmailVerified) {
                    Toast.makeText(context, "¡Correo verificado con éxito!", Toast.LENGTH_SHORT).show()
                    onVerified()
                } else {
                    Toast.makeText(context, "Tu correo aún no ha sido verificado.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Error al comprobar verificación.", Toast.LENGTH_SHORT).show()
            }
            isLoading = false
        }
    }

    fun resendEmail() {
        auth.currentUser?.sendEmailVerification()
            ?.addOnSuccessListener {
                Toast.makeText(context, "Correo de verificación reenviado.", Toast.LENGTH_SHORT).show()
                resendEnabled = false
                secondsLeft = 40
                coroutineScope.launch {
                    while (secondsLeft > 0) {
                        delay(1000)
                        secondsLeft--
                    }
                    resendEnabled = true
                }
            }
            ?.addOnFailureListener {
                Toast.makeText(context, "Error al reenviar correo.", Toast.LENGTH_SHORT).show()
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Hemos enviado un correo de verificación a tu email.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = null,
                tint = Color(0xFF64B5F6),
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 32.dp)
            )

            Button(
                onClick = { checkVerification() },
                enabled = !isLoading,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = if (isLoading) "Verificando..." else "Verificar")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (!resendEnabled) {
                Text("Reenviar en $secondsLeft segundos")
            } else {
                TextButton(onClick = { resendEmail() }) {
                    Text("Reenviar correo", color = Color(0xFF2196F3))
                }
            }
        }
    }
}
