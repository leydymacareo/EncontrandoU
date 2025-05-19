package com.leydymacareo.encontrandou.screens.staff

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.leydymacareo.encontrandou.NavRoutes
import com.leydymacareo.encontrandou.R
import com.leydymacareo.encontrandou.viewmodels.ConfiguracionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfiguracionEncargadoScreen(navController: NavController) {
    val viewModel: ConfiguracionViewModel = viewModel()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    var selectedTab by remember { mutableStateOf("Categorias") }
    val categorias by viewModel.categorias.collectAsState()
    val lugares by viewModel.lugares.collectAsState()
    val colores by viewModel.colores.collectAsState()

    var editIndex by remember { mutableStateOf<Int?>(null) }
    var editText by remember { mutableStateOf("") }

    val contenidoActual = when (selectedTab) {
        "Categorias" -> categorias
        "Lugares" -> lugares
        "Colores" -> colores
        else -> emptyList()
    }

    val placeholderMap = mapOf(
        "Categorias" to "Nueva categoría",
        "Lugares" to "Nuevo lugar",
        "Colores" to "Nuevo color"
    )

    val onAddItem = {
        if (editText.isNotBlank()) {
            viewModel.agregarElemento(selectedTab.lowercase(), editText)
            editText = ""
            editIndex = null
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
                Text(
                    text = "Ajustes",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, bottom = 20.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                    listOf("Categorias", "Lugares", "Colores").forEach { tab ->
                        val isSelected = tab == selectedTab
                        Button(
                            onClick = {
                                selectedTab = tab
                                editIndex = null
                                editText = ""
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
                    EncargadoNavItem("Inventario", R.drawable.inventory, currentRoute == NavRoutes.EncargadoHome) {
                        if (currentRoute != NavRoutes.EncargadoHome) navController.navigate(NavRoutes.EncargadoHome)
                    }
                    EncargadoNavItem("Solicitudes", R.drawable.list, currentRoute == NavRoutes.EncargadoSolicitudes) {
                        if (currentRoute != NavRoutes.EncargadoSolicitudes) navController.navigate(NavRoutes.EncargadoSolicitudes)
                    }
                    EncargadoNavItem("Perfil", R.drawable.person, currentRoute == NavRoutes.EncargadoProfile) {
                        if (currentRoute != NavRoutes.EncargadoProfile) navController.navigate(NavRoutes.EncargadoProfile)
                    }
                    EncargadoNavItem("Ajustes", R.drawable.settings, currentRoute == NavRoutes.EncargadoAjustes) {
                        if (currentRoute != NavRoutes.EncargadoAjustes) navController.navigate(NavRoutes.EncargadoAjustes)
                    }
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
                    "Categorias" -> "Categorías Registradas"
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
                                viewModel.editarElemento(selectedTab.lowercase(), item, editText)
                                editIndex = null
                                editText = ""
                            }
                        }) {
                            Icon(Icons.Default.Check, contentDescription = "Guardar")
                        }
                        IconButton(onClick = {
                            viewModel.eliminarElemento(selectedTab.lowercase(), item)
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
                                viewModel.eliminarElemento(selectedTab.lowercase(), item)
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = editText,
                onValueChange = { editText = it },
                placeholder = { Text(placeholderMap[selectedTab] ?: "Nuevo elemento") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFFBFEBFB),
                    focusedIndicatorColor = Color(0xFF00AFF1),
                    unfocusedIndicatorColor = Color.Gray,
                    cursorColor = Color(0xFF00AFF1),

                )
            )


            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onAddItem,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00AFF1)),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Añadir a ${selectedTab}", color = Color.White)
            }

            Spacer(modifier = Modifier.height(90.dp))
        }
    }
}
