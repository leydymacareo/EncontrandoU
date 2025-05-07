import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leydymacareo.encontrandou.R

@Composable
fun DetalleSolicitudUsuarioScreen() {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 40.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Detalle de la Solicitud",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFCF7)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    // Estado de la solicitud
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .background(Color(0xFFF3EAD4), shape = RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.accesstime),
                            contentDescription = null,
                            tint = Color(0xFF5C5C5C),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("En Espera", fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    InfoItem("Código de la Solicitud", "67001")
                    InfoItem("Fecha de la Solicitud", "01/04/2025")
                    InfoItem("Nombre del objeto", "Mochila Negra")
                    InfoItem("Lugar de la perdida", "Edificio L5-1")
                    InfoItem("Fecha Aproximada", "30/03/2025")
                    InfoItem("Hora Aproximada", "10:45 a.m.")
                    InfoItem("Categoría del Objeto", "Bolsos")
                    InfoItem("Color Principal", "Negro")
                    InfoItem("Marca o Modelo", "Totto")

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Descripcion Adicional", fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(8.dp))

                    Row {
                        Image(
                            painter = painterResource(id = R.drawable.logo_unab), // reemplaza con tu imagen
                            contentDescription = "Imagen del objeto",
                            modifier = Modifier
                                .size(80.dp)
                                .background(Color.White)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Tiene un llavero de Pikachu y una etiqueta roja con mi nombre.",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))


                }
            }
            Button(
                onClick = { /* acción cancelar */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00AFF1)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Cancelar Solicitud", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun InfoItem(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(label, fontWeight = FontWeight.Bold)
        Text(value)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDetalleSolicitudUsuarioScreen() {
    MaterialTheme {
        DetalleSolicitudUsuarioScreen()
    }
}
