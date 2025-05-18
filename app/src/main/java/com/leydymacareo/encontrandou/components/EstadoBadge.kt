package com.leydymacareo.encontrandou.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EstadoBadge(estado: String) {
    val (color, texto, icono) = when (estado.uppercase()) {
        "PENDIENTE"     -> Triple(Color(0xFFFEF3E2), "Pendiente", "⏳")
        "APROBADA"      -> Triple(Color(0xFFDDF6D2), "Aprobada", "✅")
        "RECHAZADA"     -> Triple(Color(0xFFFFD5D5), "Rechazada", "❌")
        "ENTREGADA" -> Triple(Color(0xFFAFDDFF), "Entregada", "📬")
        "CANCELADA"     -> Triple(Color.LightGray, "Cancelada", "🚫")
        "DISPONIBLE" -> Triple(Color(0xFFFEF3E2), "Disponible", "⭐")
        "ASIGNADO" -> Triple(Color(0xFFDDF6D2), "Asignado", "📍")
        "ENTREGADO" -> Triple(Color(0xFFAFDDFF), "Entregado", "📬")
        "RETIRADO"      -> Triple(Color.Gray, "Retirado", "📤")
        else            -> Triple(Color.LightGray, estado.capitalize(), "❓")
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(color = color, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = icono,
            fontSize = 14.sp,
            modifier = Modifier.padding(end = 4.dp)
        )
        Text(
            text = texto,
            color = Color.Black,
            style = MaterialTheme.typography.labelSmall
        )
    }
}
