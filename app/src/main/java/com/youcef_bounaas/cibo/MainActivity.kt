package com.youcef_bounaas.cibo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.youcef_bounaas.cibo.features.navigation.AppNavHost
import com.youcef_bounaas.cibo.features.profile.presentation.ProfileScreen
import com.youcef_bounaas.cibo.ui.theme.CiboTheme
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CiboTheme {

           AppNavHost()

            }
        }
    }
}

