package com.leydymacareo.encontrandou.screens.login

import com.leydymacareo.encontrandou.utils.traducirErrorFirebase
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.leydymacareo.encontrandou.NavRoutes
import com.leydymacareo.encontrandou.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    val scrollState = rememberScrollState()
    var selectedRole by remember { mutableStateOf("usuario") }

    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var codigoEncargado by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 30.dp)
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Image(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(150.dp),
                colorFilter = ColorFilter.tint(Color(0xFFFF9900))
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Registrarse",
                fontSize = 24.sp,
                color = Color(0xFFFF9900),
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            RegistroCampo("Nombre", Icons.Default.Person, nombre) { nombre = it }
            Spacer(modifier = Modifier.height(24.dp))

            RegistroCampo("Apellido", Icons.Default.Person, apellido) { apellido = it }
            Spacer(modifier = Modifier.height(24.dp))

            RegistroCampo("Correo Electrónico", Icons.Default.Email, email) { email = it }
            Spacer(modifier = Modifier.height(24.dp))

            RegistroCampo("Contraseña", Icons.Default.Lock, password, true) { password = it }
            Spacer(modifier = Modifier.height(24.dp))

            RegistroCampo("Confirmar Contraseña", Icons.Default.Lock, confirmPassword, true) { confirmPassword = it }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Rol",
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                RadioButton(
                    selected = selectedRole == "usuario",
                    onClick = { selectedRole = "usuario" },
                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFFF9900))
                )
                Text("Usuario")
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                RadioButton(
                    selected = selectedRole == "encargado",
                    onClick = { selectedRole = "encargado" },
                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFFF9900))
                )
                Text("Encargado")
            }

            if (selectedRole == "encargado") {
                Spacer(modifier = Modifier.height(16.dp))
                RegistroCampo("Código (Solo para Encargados)", Icons.Default.Lock, codigoEncargado) { codigoEncargado = it }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (
                        nombre.isBlank() || apellido.isBlank() || email.isBlank() || password.isBlank()
                        || confirmPassword.isBlank()
                        || (selectedRole == "encargado" && codigoEncargado != "MOVILES2025-1")
                    ) {
                        Toast.makeText(context, "Por favor completa los campos correctamente", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    if (password != confirmPassword) {
                        Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    isLoading = true
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val userId = auth.currentUser?.uid ?: ""
                                val userMap = hashMapOf(
                                    "nombre" to "$nombre $apellido",
                                    "email" to email,
                                    "rol" to selectedRole
                                )
                                db.collection("usuarios").document(userId)
                                    .set(userMap)
                                    .addOnSuccessListener {
                                        isLoading = false
                                        navController.navigate(NavRoutes.AccountCreated)
                                    }
                                    .addOnFailureListener {
                                        isLoading = false
                                        Toast.makeText(context, "Error al guardar datos", Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                isLoading = false
                                val msg = traducirErrorFirebase(context, task.exception?.message)
                                Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                            }
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00AFF1))
            ) {
                Text(
                    text = if (isLoading) "Registrando..." else "Registrarse",
                    fontSize = 18.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroCampo(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    isPassword: Boolean = false,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.padding(end = 20.dp)
            )
        },
        label = {
            Text(
                text = label,
                color = Color.Black,
                modifier = Modifier.padding(start = 8.dp),
                fontSize = 18.sp
            )
        },
        shape = RoundedCornerShape(12.dp),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = Color(0xFFBFEBFB),
            unfocusedBorderColor = Color(0xFF80D7F8)
        )
    )
}
