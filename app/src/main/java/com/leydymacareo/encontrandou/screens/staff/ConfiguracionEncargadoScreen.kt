package com.leydymacareo.encontrandou.screens.staff

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leydymacareo.encontrandou.R

@Composable
fun ConfiguracionEncargadoScreen() {
    var selectedTab by remember { mutableStateOf("Categoría") }
    var categorias by remember { mutableStateOf(mutableListOf("Celulares", "Audífonos", "Computadores")) }
    var lugares by remember { mutableStateOf(mutableListOf("Bloque A", "Biblioteca", "Cafetería")) }
    var colores by remember { mutableStateOf(mutableListOf("Rojo", "Azul", "Verde")) }
    var editIndex by remember { mutableStateOf<Int?>(null) }
    var editText by remember { mutableStateOf("") }

    val contenidoActual = when (selectedTab) {
        "Categoría" -> categorias
        "Lugares" -> lugares
        "Colores" -> colores
        else -> emptyList()
    }

    val onAddItem = {
        editIndex = contenidoActual.size
        editText = ""
        when (selectedTab) {
            "Categoría" -> categorias = categorias.toMutableList().apply { add("") }
            "Lugares" -> lugares = lugares.toMutableList().apply { add("") }
            "Colores" -> colores = colores.toMutableList().apply { add("") }
        }
    }

    Scaffold(
        containerColor = Color(0xFFF5F5F5),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = "Ajustes",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, bottom = 20.dp),)

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                    listOf("Categoría", "Lugares", "Colores").forEach { tab ->
                        val isSelected = tab == selectedTab
                        Button(
                            onClick = {
                                selectedTab = tab
                                editIndex = null
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) Color(0xFF00AFF1) else Color.LightGray
                            ),
                            modifier = Modifier.padding(horizontal = 4.dp)
                        ) {
                            Text(tab, color = Color.White)
                        }
                    }
                }
            }
        },
        bottomBar = {
            Surface(
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    EncargadoNavItem(
                        label = "Inventario",
                        icon = Icons.Default.Person,
                        selectedTab = selectedTab
                    ) { selectedTab = "Inventario" }

                    EncargadoNavItem(
                        label = "Solicitudes",
                        icon = Icons.Default.List,
                        selectedTab = selectedTab
                    ) { selectedTab = "Solicitudes" }

                    EncargadoNavItem(
                        label = "Perfil",
                        icon = Icons.Default.Person,
                        selectedTab = selectedTab
                    ) { selectedTab = "Perfil" }

                    EncargadoNavItem(
                        label = "Ajustes",
                        icon = Icons.Default.Settings,
                        selectedTab = selectedTab
                    ) { selectedTab = "Ajustes" }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = when (selectedTab) {
                    "Categoría" -> "Categorías Registradas"
                    "Lugares" -> "Lugares Registrados"
                    "Colores" -> "Colores Registrados"
                    else -> ""
                },
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            contenidoActual.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (editIndex == index) {
                        TextField(
                            value = editText,
                            onValueChange = { editText = it },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = {
                            if (editText.isNotBlank()) {
                                when (selectedTab) {
                                    "Categoría" -> categorias = categorias.toMutableList().apply { set(index, editText) }
                                    "Lugares" -> lugares = lugares.toMutableList().apply { set(index, editText) }
                                    "Colores" -> colores = colores.toMutableList().apply { set(index, editText) }
                                }
                                editIndex = null
                            }
                        }) {
                            Icon(Icons.Default.Check, contentDescription = "Guardar")
                        }
                        IconButton(onClick = {
                            when (selectedTab) {
                                "Categoría" -> categorias = categorias.toMutableList().apply { removeAt(index) }
                                "Lugares" -> lugares = lugares.toMutableList().apply { removeAt(index) }
                                "Colores" -> colores = colores.toMutableList().apply { removeAt(index) }
                            }
                            editIndex = null
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "Cancelar")
                        }
                    } else {
                        Text(item)
                        Row {
                            IconButton(onClick = {
                                editIndex = index
                                editText = item
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar")
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            IconButton(onClick = {
                                when (selectedTab) {
                                    "Categoría" -> categorias = categorias.toMutableList().apply { removeAt(index) }
                                    "Lugares" -> lugares = lugares.toMutableList().apply { removeAt(index) }
                                    "Colores" -> colores = colores.toMutableList().apply { removeAt(index) }
                                }
                                if (editIndex == index) editIndex = null
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onAddItem,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00AFF1)),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Añadir ${selectedTab}", color = Color.White)
            }

            Spacer(modifier = Modifier.height(90.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewConfiguracionEncargadoScreen() {
    MaterialTheme {
        ConfiguracionEncargadoScreen()
    }
}
