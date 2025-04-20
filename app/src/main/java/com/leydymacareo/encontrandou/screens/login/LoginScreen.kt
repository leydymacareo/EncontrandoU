package com.leydymacareo.encontrandou.screens.login

import com.leydymacareo.encontrandou.utils.traducirErrorFirebase
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.leydymacareo.encontrandou.NavRoutes
import com.leydymacareo.encontrandou.R



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginSreen(
    navController: NavController,
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showResetPasswordDialog by remember { mutableStateOf(false) }
    var recoveryEmail by remember { mutableStateOf("") }

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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.logo_unab),
                contentDescription = "Logo Unab",
                modifier = Modifier.size(200.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Iniciar Sesión",
                fontSize = 24.sp,
                color = Color(0xFFFF9900),
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email",
                        tint = Color.Black,
                        modifier = Modifier.padding(end = 20.dp)
                    )
                },
                label = {
                    Text(
                        text = "Correo Electrónico",
                        color = Color.Black,
                        modifier = Modifier.padding(start = 8.dp),
                        fontSize = 18.sp
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color(0xFFBFEBFB),
                    unfocusedBorderColor = Color(0xFF80D7F8)
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Contraseña",
                        tint = Color.Black,
                        modifier = Modifier.padding(end = 20.dp)
                    )
                },
                label = {
                    Text(
                        text = "Contraseña",
                        color = Color.Black,
                        modifier = Modifier.padding(start = 8.dp),
                        fontSize = 18.sp
                    )
                },
                shape = RoundedCornerShape(12.dp),
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color(0xFFBFEBFB),
                    unfocusedBorderColor = Color(0xFF80D7F8)
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    isLoading = true
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            isLoading = false
                            if (task.isSuccessful) {
                                val user = auth.currentUser
                                if (user != null && user.isEmailVerified) {
                                    navController.navigate(NavRoutes.Home) {
                                        popUpTo(NavRoutes.Login) { inclusive = true }
                                    }
                                } else {
                                    navController.navigate(NavRoutes.VerifyEmail)
                                }
                            } else {
                                val msg = traducirErrorFirebase(context, task.exception?.message)
                                Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                            }
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00AFF1)),
                enabled = !isLoading
            ) {
                Text(
                    text = if (isLoading) "Iniciando..." else "Iniciar Sesión",
                    fontSize = 18.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = { showResetPasswordDialog = true }) {
                Text("¿Olvidaste tu contraseña?", color = Color(0xFF00AFF1))
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showResetPasswordDialog) {
        AlertDialog(
            onDismissRequest = { showResetPasswordDialog = false },
            title = { Text("Recuperar contraseña") },
            text = {
                Column {
                    Text("Ingresa tu correo para enviarte un enlace de recuperación.")
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = recoveryEmail,
                        onValueChange = { recoveryEmail = it },
                        label = { Text("Correo electrónico") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (recoveryEmail.isNotBlank()) {
                        FirebaseAuth.getInstance()
                            .sendPasswordResetEmail(recoveryEmail)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Correo enviado", Toast.LENGTH_SHORT).show()
                                showResetPasswordDialog = false
                                recoveryEmail = ""
                            }
                            .addOnFailureListener {
                                val msg = traducirErrorFirebase(context, it.message)
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            }
                    }
                }) {
                    Text("Enviar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetPasswordDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
