import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.leydymacareo.encontrandou.NavRoutes
import com.leydymacareo.encontrandou.R
import com.leydymacareo.encontrandou.screens.home.NavBarItem


@Composable
fun HelpScreen(navController: NavController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5))
                    .padding(top = 40.dp, bottom = 20.dp, start = 24.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    "Ayuda",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
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
                        .padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    NavBarItem(
                        icon = painterResource(id = R.drawable.home),
                        label = "Inicio",
                        isSelected = currentRoute == NavRoutes.UserHome,
                        onClick = {
                            if (currentRoute != NavRoutes.UserHome) {
                                navController.navigate(NavRoutes.UserHome)
                            }
                        }
                    )

                    NavBarItem(
                        icon = painterResource(id = R.drawable.help),
                        label = "Ayuda",
                        isSelected = currentRoute == NavRoutes.UserHelp,
                        onClick = {
                            if (currentRoute != NavRoutes.UserHelp) {
                                navController.navigate(NavRoutes.UserHelp)
                            }
                        }
                    )

                    NavBarItem(
                        icon = painterResource(id = R.drawable.person),
                        label = "Perfil",
                        isSelected = currentRoute == NavRoutes.UserProfile,
                        onClick = {
                            if (currentRoute != NavRoutes.UserProfile) {
                                navController.navigate(NavRoutes.UserProfile)
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color(0xFFF5F5F5))
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HelpCard(title = "¿Cómo funciona esta app?", content = listOf(
                "Reporta el objeto que perdiste desde la pantalla principal.",
                "El encargado del departamento revisará tu solicitud.",
                "Si encuentran tu objeto, te enviarán una notificación.",
                "Podrás pasar a recogerlo en la oficina de objetos perdidos."
            ))

            HelpCard(title = "Preguntas frecuentes", content = listOf(
                "¿Qué tipo de objetos puedo reportar?",
                "¿Cuánto tiempo dura mi solicitud?",
                "¿Puedo modificar una solicitud después de enviarla?",
                "¿Dónde recojo mi objeto si lo aprueban?"
            ))

            HelpCard(title = "Contacto", content = listOf(
                "Correo: objetosperdidos@unab.edu.co",
                "Teléfono: +57 300 123 4567",
                "Oficina: Edificio X, Piso 2"
            ))

            HelpCard(title = "Versión", content = listOf(
                "App: EncontradoU",
                "Versión: v1.0.0",
                "Última actualización: abril 2025"
            ))

            Spacer(modifier = Modifier.height(90.dp))
        }
    }
}

@Composable
fun HelpCard(title: String, content: List<String>) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFCF7)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            content.forEachIndexed { index, line ->
                Text(text = "${index + 1}. $line", fontSize = 14.sp)
            }
        }
    }
}
