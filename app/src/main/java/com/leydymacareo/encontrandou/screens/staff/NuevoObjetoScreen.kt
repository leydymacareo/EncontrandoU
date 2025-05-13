package com.leydymacareo.encontrandou.screens

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import com.leydymacareo.encontrandou.components.FormularioObjeto
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoObjetoScreen() {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Nuevo Objeto", fontWeight = FontWeight.Bold, fontSize = 22.sp) },
                navigationIcon = {
                    IconButton(onClick = { /* acción volver */ }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        FormularioObjeto(
            titulo = "Nuevo Objeto",
            textoBoton = "Registrar Objeto",
            imagenObligatoria = true,
            onSubmit = { formData, imageUri ->
                println("[Encargado] Registro: $formData\nImagen: $imageUri")
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}
