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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.youcef_bounaas.cibo.data.repository.Auth.AuthViewModel
import com.youcef_bounaas.cibo.features.Authentication.signUp
import com.youcef_bounaas.cibo.features.mealmenu.MenuScreen
import com.youcef_bounaas.cibo.features.mealmenu.MenuViewModel
import com.youcef_bounaas.cibo.features.navigation.AppNavHost
import com.youcef_bounaas.cibo.features.profile.data.ProfileViewModel
import com.youcef_bounaas.cibo.features.profile.presentation.ProfileScreen
import com.youcef_bounaas.cibo.features.roles.data.UsersViewModel
import com.youcef_bounaas.cibo.features.roles.presentation.UserItem
import com.youcef_bounaas.cibo.features.roles.presentation.UsersScreen
import com.youcef_bounaas.cibo.ui.theme.CiboTheme
import dagger.hilt.android.AndroidEntryPoint

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





