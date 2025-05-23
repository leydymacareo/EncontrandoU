package com.leydymacareo.encontrandou.screens.login


import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import com.google.firebase.firestore.FirebaseFirestore
import com.leydymacareo.encontrandou.NavRoutes
import com.leydymacareo.encontrandou.R
import com.leydymacareo.encontrandou.viewmodel.SessionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    sessionViewModel: SessionViewModel,
    onBack: () -> Unit = {}
) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

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
                onValueChange = {
                    email = it
                    emailError = ""
                },
                modifier = Modifier
                    .fillMaxWidth(),
                label = { Text("Correo Electrónico") },
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = "Email", tint = Color.Black)
                },
                supportingText = {
                    if (emailError.isNotEmpty()) Text(emailError, color = Color.Red)
                },
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.White,
                    unfocusedBorderColor = Color(0xFF80D7F8),
                    focusedBorderColor = Color(0xFF80D7F8),
                )
            )


            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = ""
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Contraseña") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = "Contraseña", tint = Color.Black)
                },
                supportingText = {
                    if (passwordError.isNotEmpty()) Text(passwordError, color = Color.Red)
                },
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedBorderColor = Color(0xFF80D7F8),
                    focusedBorderColor = Color(0xFF80D7F8),
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    emailError = ""
                    passwordError = ""

                    if (!email.endsWith("@unab.edu.co")) {
                        emailError = "Solo se permite correo @unab.edu.co"
                        return@Button
                    }

                    if (email.isBlank() || password.isBlank()) {
                        if (email.isBlank()) emailError = "Campo requerido"
                        if (password.isBlank()) passwordError = "Campo requerido"
                        return@Button
                    }

                    isLoading = true

                    db.collection("usuarios")
                        .whereEqualTo("email", email)
                        .get()
                        .addOnSuccessListener { result ->
                            if (result.isEmpty) {
                                isLoading = false
                                emailError = "Correo no registrado"
                            } else {
                                val doc = result.documents[0]
                                val rawRole = doc.getString("rol") ?: ""
                                val capitalizedRole = rawRole.replaceFirstChar { it.uppercaseChar() }
                                val name = doc.getString("nombre") ?: ""
                                auth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener { task ->
                                        isLoading = false
                                        if (task.isSuccessful) {
                                            sessionViewModel.setUserSession(capitalizedRole, name, email)
                                            val targetRoute = when (rawRole) {
                                                "usuario" -> NavRoutes.UserHome
                                                "encargado" -> NavRoutes.EncargadoHome
                                                else -> NavRoutes.UserHome
                                            }
                                            navController.navigate(targetRoute) {
                                                popUpTo(NavRoutes.Login) { inclusive = true }
                                            }
                                        } else {
                                            passwordError = "Contraseña incorrecta"
                                        }
                                    }
                            }
                        }
                        .addOnFailureListener {
                            isLoading = false
                            emailError = "Error al validar el correo"
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
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
