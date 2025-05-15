package com.leydymacareo.encontrandou.screens

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import com.leydymacareo.encontrandou.components.FormularioObjeto
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.navigation.NavController
import androidx.navigation.NavHostController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevaSolicitudScreen(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Nueva Solicitud", fontWeight = FontWeight.Bold, fontSize = 22.sp) },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack() // ← Vuelve al Home
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        FormularioObjeto(
            titulo = "Nueva Solicitud",
            textoBoton = "Enviar Solicitud",
            imagenObligatoria = false,
            onSubmit = { formData, imageUri ->
                println("[Solicitud] Formulario: $formData\nImagen: $imageUri")
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}
