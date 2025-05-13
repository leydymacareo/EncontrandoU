package com.leydymacareo.encontrandou.screens.user

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.leydymacareo.encontrandou.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import android.util.Log;

// Estado del formulario
data class SolicitudFormState(
    var nombreObjeto: String = "",
    var lugar: String = "",
    var fecha: String = "",
    var hora: String = "",
    var categoria: String = "",
    var color: String = "",
    var marca: String = "",
    var descripcion: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevaSolicitudScreen() {
    val context = LocalContext.current
    var formState by remember { mutableStateOf(SolicitudFormState()) }
    var fechaEnMillis by remember { mutableStateOf<Long?>(null) }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var showPicker by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> uri?.let { imageUri = it } }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success -> if (!success) imageUri = null }

    fun launchCamera() {
        val file = File(
            context.cacheDir,
            "IMG_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())}.jpg"
        )
        val uri = FileProvider.getUriForFile(
            context,
            "com.leydymacareo.encontrandou.fileprovider",
            file
        )
        imageUri = uri
        cameraLauncher.launch(uri)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Nueva Solicitud", fontWeight = FontWeight.Bold, fontSize = 22.sp) },
                navigationIcon = {
                    IconButton(onClick = { /* acción volver */ }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            LabeledField("Nombre del objeto*", "Ej: Mochila Negra", formState.nombreObjeto) {
                formState = formState.copy(nombreObjeto = it)
            }

            LabeledDropdown("Lugar de la pérdida*", listOf("Seleccionar", "Biblioteca", "Cafetería"), formState.lugar) {
                formState = formState.copy(lugar = it)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    LabeledDatePickerField(
                        label = "Fecha Aproximada*",
                        selectedDate = fechaEnMillis,
                        onDateSelected = {
                            fechaEnMillis = it
                            formState = formState.copy(fecha = convertMillisToDate(it))
                        }
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    LabeledTimePicker("Hora Aproximada*", formState.hora) {
                        formState = formState.copy(hora = it)
                    }
                }
            }

            LabeledDropdown("Categoría del Objeto*", listOf("Seleccionar", "Bolsos", "Tecnología", "Ropa"), formState.categoria) {
                formState = formState.copy(categoria = it)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    LabeledDropdown("Color Principal*", listOf("Seleccionar", "Negro", "Rojo", "Azul"), formState.color) {
                        formState = formState.copy(color = it)
                    }
                }
                Box(modifier = Modifier.weight(1f)) {
                    LabeledField("Marca o Modelo", "Ej: Adidas", formState.marca) {
                        formState = formState.copy(marca = it)
                    }
                }
            }

            LabeledField("Descripción Adicional", "Ej: Tiene un sticker", formState.descripcion) {
                formState = formState.copy(descripcion = it)
            }

            OutlinedButton(
                onClick = { showPicker = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFF00AFF1))
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.photo_camera),
                    contentDescription = "Adjuntar",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Adjuntar Foto", color = Color.Gray)
            }

            if (imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(model = imageUri),
                    contentDescription = "Imagen seleccionada",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
            }

            if (showPicker) {
                AlertDialog(
                    onDismissRequest = { showPicker = false },
                    confirmButton = {},
                    title = { Text("Selecciona una opción") },
                    text = {
                        Column {
                            TextButton(onClick = {
                                showPicker = false
                                galleryLauncher.launch("image/*")
                            }) {
                                Text("Elegir de galería")
                            }
                            TextButton(onClick = {
                                showPicker = false
                                launchCamera()
                            }) {
                                Text("Tomar foto")
                            }
                        }
                    }
                )
            }

            Text(
                text = "Tu información será revisada por el personal. Recibirás una notificación cuando tengamos novedades.",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Button(
                onClick = {
                    println("Formulario: $formState\nImagen: $imageUri")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00AFF1)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Enviar Solicitud", fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(90.dp))
        }
    }
}

@Composable
fun LabeledDatePickerField(
    label: String,
    selectedDate: Long?,
    onDateSelected: (Long) -> Unit
) {
    var showModal by remember { mutableStateOf(false) }

    val formattedDate = selectedDate?.let { convertMillisToDate(it) } ?: ""

    OutlinedTextField(
        value = formattedDate,
        onValueChange = { },
        label = { Text(label) },
        placeholder = { Text("DD/MM/AAAA") },
        readOnly = true,
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
        },
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            }
    )

    if (showModal) {
        DatePickerModal(
            onDateSelected = {
                onDateSelected(it)
                showModal = false
            },
            onDismiss = { showModal = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                // Gets the selected date in millis and subtracts the UTC offset so we get the proper date
                val dateInMillis = (datePickerState.selectedDateMillis ?: 0) - getUtcOffsetMillis()
                onDateSelected(dateInMillis)
            }) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        title = { Text("Selecciona una fecha") },
        text = {
            DatePicker(
                title = {Text("")},
                state = datePickerState,
                showModeToggle = false
            )
        }
    )
}

/**
 * Gets the offset in millis between our timezone and UTC
 * So we can convert the UTC date returned by the datepicker to local date
 */
fun getUtcOffsetMillis(): Long {
    val currentTimeZone = TimeZone.getDefault()
    val now = Calendar.getInstance()
    return currentTimeZone.getOffset(now.timeInMillis).toLong()
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabeledDropdown(
    label: String,
    options: List<String>,
    selected: String,
    onSelectedChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(6.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selected,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Seleccionar") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    unfocusedBorderColor = Color(0xFF00AFF1),
                    focusedBorderColor = Color(0xFF00AFF1),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedPlaceholderColor = Color.Gray,
                    unfocusedPlaceholderColor = Color.Gray
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = {
                            onSelectedChange(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
@Composable
fun LabeledField(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                unfocusedBorderColor = Color(0xFF00AFF1),
                focusedBorderColor = Color(0xFF00AFF1),
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedPlaceholderColor = Color.Gray,
                unfocusedPlaceholderColor = Color.Gray
            )
        )
    }
}
@Composable
fun LabeledTimePicker(
    label: String,
    value: String,
    onTimeSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                onTimeSelected(selectedTime)
                showDialog = false
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            placeholder = { Text("HH:MM") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDialog = true },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                unfocusedBorderColor = Color(0xFF00AFF1),
                focusedBorderColor = Color(0xFF00AFF1),
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedPlaceholderColor = Color.Gray,
                unfocusedPlaceholderColor = Color.Gray
            )
        )
    }
}
