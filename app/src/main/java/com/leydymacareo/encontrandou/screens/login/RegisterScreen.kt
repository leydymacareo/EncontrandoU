package com.leydymacareo.encontrandou.screens.login

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
import com.leydymacareo.encontrandou.utils.validarCodigoEncargado
import com.leydymacareo.encontrandou.utils.validarConfirmacion
import com.leydymacareo.encontrandou.utils.validarContrasena
import com.leydymacareo.encontrandou.utils.validarCorreo
import com.leydymacareo.encontrandou.utils.validarNombre
import com.leydymacareo.encontrandou.viewmodel.SessionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    sessionViewModel: SessionViewModel,
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

    var errorNombre by remember { mutableStateOf("") }
    var errorApellido by remember { mutableStateOf("") }
    var errorEmail by remember { mutableStateOf("") }
    var errorPassword by remember { mutableStateOf("") }
    var errorConfirm by remember { mutableStateOf("") }
    var errorCodigo by remember { mutableStateOf("") }
    var errorGeneral by remember { mutableStateOf("") }

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

            RegistroCampo("Nombre", Icons.Default.Person, nombre, errorNombre) { nombre = it }
            RegistroCampo("Apellido", Icons.Default.Person, apellido, errorApellido) { apellido = it }
            RegistroCampo("Correo Electrónico", Icons.Default.Email, email, errorEmail) { email = it }
            RegistroCampo("Contraseña", Icons.Default.Lock, password, errorPassword, true) { password = it }
            RegistroCampo("Confirmar Contraseña", Icons.Default.Lock, confirmPassword, errorConfirm, true) { confirmPassword = it }

            Text(
                text = "Rol",
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 4.dp)
            )

            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start) {
                RadioButton(
                    selected = selectedRole == "usuario",
                    onClick = { selectedRole = "usuario" },
                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFFF9900))
                )
                Text("Usuario")
            }

            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start) {
                RadioButton(
                    selected = selectedRole == "encargado",
                    onClick = { selectedRole = "encargado" },
                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFFF9900))
                )
                Text("Encargado")
            }

            if (selectedRole == "encargado") {
                RegistroCampo(
                    label = "Código (Solo para Encargados)",
                    icon = Icons.Default.Lock,
                    value = codigoEncargado,
                    errorText = errorCodigo
                ) { codigoEncargado = it }
            }

            if (errorGeneral.isNotEmpty()) {
                Text(errorGeneral, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val (okNombre, msgNombre) = validarNombre(nombre)
                    val (okApellido, msgApellido) = validarNombre(apellido)
                    val (okCorreo, msgCorreo) = validarCorreo(email)
                    val (okPass, msgPass) = validarContrasena(password)
                    val (okConfirm, msgConfirm) = validarConfirmacion(password, confirmPassword)
                    val (okCodigo, msgCodigo) = if (selectedRole == "encargado") validarCodigoEncargado(codigoEncargado) else true to ""

                    errorNombre = msgNombre
                    errorApellido = msgApellido
                    errorEmail = msgCorreo
                    errorPassword = msgPass
                    errorConfirm = msgConfirm
                    errorCodigo = msgCodigo

                    if (okNombre && okApellido && okCorreo && okPass && okConfirm && okCodigo) {
                        isLoading = true
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val uid = auth.currentUser?.uid ?: ""
                                    val userData = mapOf(
                                        "nombre" to "$nombre $apellido",
                                        "email" to email,
                                        "rol" to selectedRole
                                    )
                                    db.collection("usuarios").document(uid).set(userData)
                                        .addOnSuccessListener {
                                            isLoading = false
                                            navController.navigate(NavRoutes.AccountCreated)
                                        }
                                        .addOnFailureListener {
                                            isLoading = false
                                            errorGeneral = "Error al guardar los datos."
                                        }
                                } else {
                                    isLoading = false
                                    errorGeneral = task.exception?.localizedMessage ?: "Error desconocido"
                                }
                            }
                    } else {
                        errorGeneral = "Corrige los errores para continuar"
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
    errorText: String = "",
    isPassword: Boolean = false,
    onChange: (String) -> Unit
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
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
                unfocusedBorderColor = Color(0xFF80D7F8),
                focusedBorderColor = Color(0xFF00AFF1)
            )
        )

        if (errorText.isNotEmpty()) {
            Text(
                text = errorText,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
            )
        }
    }
}
