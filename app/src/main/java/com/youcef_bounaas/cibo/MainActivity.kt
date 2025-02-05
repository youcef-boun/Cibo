package com.youcef_bounaas.cibo

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.youcef_bounaas.cibo.data.repository.SupabaseClientProvider.supabase
import com.youcef_bounaas.cibo.features.navigation.AppNavHost
import com.youcef_bounaas.cibo.ui.theme.CiboTheme
import dagger.hilt.android.AndroidEntryPoint
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
 //private val authViewModel: AuthViewModel by viewModels()

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





