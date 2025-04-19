package com.leydymacareo.encontrandou.screens.login

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onBack: () -> Unit = {},
    onNext: () -> Unit = {}
) {
    var selectedRole by remember { mutableStateOf("usuario") }

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
        val scrollState = rememberScrollState()

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

            RegistroCampo("Nombre", Icons.Default.Person)
            Spacer(modifier = Modifier.height(24.dp))
            RegistroCampo("Apellido", Icons.Default.Person)
            Spacer(modifier = Modifier.height(24.dp))
            RegistroCampo("Correo Electrónico", Icons.Default.Email)
            Spacer(modifier = Modifier.height(24.dp))
            RegistroCampo("Contraseña", Icons.Default.Lock)
            Spacer(modifier = Modifier.height(24.dp))
            RegistroCampo("Confirmar Contraseña", Icons.Default.Lock)

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
                RegistroCampo("Código (Solo para Encargados)", Icons.Default.Lock)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onNext,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00AFF1))
            ) {
                Text(
                    text = "Registrarse",
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
fun RegistroCampo(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    OutlinedTextField(
        value = "",
        onValueChange = {},
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
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = Color(0xFFBFEBFB),
            unfocusedBorderColor = Color(0xFF80D7F8)
        )
    )
}

@Preview(showSystemUi = true)
@Composable
fun RegisterScreenPreview() {
    //RegisterScreen()
}
