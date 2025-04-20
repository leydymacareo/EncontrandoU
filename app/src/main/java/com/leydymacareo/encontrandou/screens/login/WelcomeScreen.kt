package com.leydymacareo.encontrandou.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leydymacareo.encontrandou.R

@Composable
fun WelcomeScreen(
    onRegisterClick: () -> Unit = {},
    onLoginClick: () -> Unit = {}
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.logo_unab),
                contentDescription = "Logo Unab",
                modifier = Modifier.size(250.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Objetos Perdidos UNAB",
                fontSize = 24.sp,
                color = Color(0xFFFF9900),
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00AFF1))
            ) {
                Text("Iniciar Sesión", fontSize = 18.sp, color = Color.Black)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onRegisterClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBFEBFB))
            ) {
                Text("Registrarse", fontSize = 18.sp, color = Color.Black)
            }
        }
    }
}
