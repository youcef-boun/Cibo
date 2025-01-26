package com.youcef_bounaas.cibo

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.youcef_bounaas.cibo.features.mealmenu.MenuScreen
import com.youcef_bounaas.cibo.features.mealmenu.MenuViewModel
import com.youcef_bounaas.cibo.features.navigation.AppNavHost
import com.youcef_bounaas.cibo.features.profile.data.ProfileViewModel
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

                val viewModel by viewModels<MenuViewModel>()
                val context = LocalContext.current
                val navController = rememberNavController()
                Log.d("MainActivity", "NavController initialized")



                AppNavHost(navController)

            }
        }
    }
}