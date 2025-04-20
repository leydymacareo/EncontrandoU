package com.leydymacareo.encontrandou

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.leydymacareo.encontrandou.navigation.AppNavHost
import com.leydymacareo.encontrandou.screens.user.LostFrormScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LostFrormScreen()
        }
    }
}
