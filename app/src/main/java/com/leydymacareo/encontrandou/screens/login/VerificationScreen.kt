package com.leydymacareo.encontrandou.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerificationScreen(
    onBack: () -> Unit = {},
    onVerified: () -> Unit = {}
) {
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
                text = "Hemos enviado un código de 6 dígitos a tu correo.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 24.dp),
                textAlign = TextAlign.Center
            )

            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = "Correo",
                tint = Color(0xFF64B5F6),
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 32.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                repeat(6) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFFE3F2FD), shape = RoundedCornerShape(8.dp))
                    )
                }
            }

            Button(
                onClick = onVerified,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
            ) {
                Text("Verificar")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "¿No recibiste el correo?\nReenviar en 40 s",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun VerificationScreenPreview() {
    //VerificationScreen()
}
