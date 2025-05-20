package com.leydymacareo.encontrandou.components

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import coil.compose.rememberAsyncImagePainter
import com.leydymacareo.encontrandou.R
import com.leydymacareo.encontrandou.models.SolicitudFormState
import com.leydymacareo.encontrandou.screens.user.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leydymacareo.encontrandou.viewmodels.ConfiguracionViewModel

@SuppressLint("UnrememberedMutableState")
@Composable
fun FormularioObjeto(
    titulo: String,
    textoBoton: String,
    imagenObligatoria: Boolean = false,
    onSubmit: (SolicitudFormState, Uri?) -> Unit,
    modifier: Modifier = Modifier
) {
    val configViewModel: ConfiguracionViewModel = viewModel()
    val categorias by configViewModel.categorias.collectAsState()
    val lugares by configViewModel.lugares.collectAsState()
    val colores by configViewModel.colores.collectAsState()

    val context = LocalContext.current
    var formState by remember { mutableStateOf(SolicitudFormState()) }
    var fechaEnMillis by remember { mutableStateOf<Long?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var showPicker by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> uri?.let {

        imageUri = it
    } }

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

    val camposValidos by derivedStateOf {
        formState.nombreObjeto.isNotBlank() &&
                formState.lugar.isNotBlank() &&
                formState.fechaAproximada.isNotBlank() &&
                formState.horaAproximada.isNotBlank() &&
                formState.categoria.isNotBlank() &&
                formState.color.isNotBlank() &&
                (!imagenObligatoria || imageUri != null)
    }

    Column(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        LabeledField("Nombre del objeto*", "Ej: Mochila Negra", formState.nombreObjeto) {
            formState = formState.copy(nombreObjeto = it)
        }

        LabeledDropdown("Lugar de la pérdida*", listOf("Seleccionar") + lugares, formState.lugar) {
            formState = formState.copy(lugar = it)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(modifier = Modifier.weight(1f)) {
                LabeledDatePickerField(
                    label = "Fecha Aproximada*",
                    selectedDate = fechaEnMillis,
                    onDateSelected = {
                        fechaEnMillis = it
                        formState = formState.copy(fechaAproximada = convertMillisToDate(it))
                    }
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                LabeledTimePickerField("Hora Aproximada*", formState.horaAproximada) {
                    formState = formState.copy(horaAproximada = it)
                }
            }
        }

        LabeledDropdown("Categoría del Objeto*", listOf("Seleccionar") + categorias, formState.categoria) {
            formState = formState.copy(categoria = it)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(modifier = Modifier.weight(1f)) {
                LabeledDropdown("Color Principal*", listOf("Seleccionar") + colores, formState.color) {
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
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
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
                    .size(400.dp),
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
        if (!camposValidos) {
            Text(
                text = "Por favor, completa todos los campos obligatorios.",
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        Button(
            onClick = { onSubmit(formState, imageUri) },
            enabled = camposValidos,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (camposValidos) Color(0xFF00AFF1) else Color.Gray
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(textoBoton, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(90.dp))
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

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = formattedDate,
            onValueChange = {},
            readOnly = true,
            placeholder = { Text("DD/MM/AAAA") },
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
                },
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
    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                return utcTimeMillis <= calendar.timeInMillis
            }
        }
    )

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
@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabeledTimePickerField(
    label: String,
    value: String,
    onTimeSelected: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

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
                .pointerInput(Unit) {
                    awaitEachGesture {
                        awaitFirstDown(pass = PointerEventPass.Initial)
                        val up = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                        if (up != null) {
                            showDialog = true
                        }
                    }
                },
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

    if (showDialog) {
        DialWithDialogExample(
            onConfirm = { timeState ->
                val formatted = "%02d:%02d".format(timeState.hour, timeState.minute)
                onTimeSelected(formatted)
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialWithDialogExample(
    onConfirm: (TimePickerState) -> Unit,
    onDismiss: () -> Unit,
) {
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )

    TimePickerDialog(
        onDismiss = onDismiss,
        onConfirm = { onConfirm(timePickerState) }
    ) {
        TimePicker(state = timePickerState)
    }
}

@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Aceptar")
            }
        },
        text = { content() }
    )
}

fun obtenerFechaActual(): String {
    val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
    return sdf.format(java.util.Date())
}
