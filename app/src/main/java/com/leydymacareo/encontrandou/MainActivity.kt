package com.leydymacareo.encontrandou



import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.leydymacareo.encontrandou.navigation.AppNavHost
import com.leydymacareo.encontrandou.screens.NuevaSolicitudScreen
import com.leydymacareo.encontrandou.screens.NuevoObjetoScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
           AppNavHost()
        }
    }
}
