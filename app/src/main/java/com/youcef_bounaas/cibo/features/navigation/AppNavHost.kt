package com.youcef_bounaas.cibo.features.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.youcef_bounaas.cibo.data.model.MenuItem
import com.youcef_bounaas.cibo.data.model.SessionManager
import com.youcef_bounaas.cibo.data.model.UserSession
import com.youcef_bounaas.cibo.data.repository.Auth.AuthViewModel
import com.youcef_bounaas.cibo.features.Authentication.AuthScreen
import com.youcef_bounaas.cibo.features.Settings.SettingsScreen
import com.youcef_bounaas.cibo.features.mealmenu.MenuScreen
import com.youcef_bounaas.cibo.features.mealmenu.OrderScreen
import com.youcef_bounaas.cibo.features.profile.presentation.ProfileScreen

@Composable
fun AppNavHost() {
    val context = LocalContext.current
    val navController = rememberNavController()

    // Use Singleton to ensure one instance of SessionManager
    val sessionManager = remember { SessionManager.getInstance(context) }
    val authViewModel = remember { AuthViewModel(context) }


    // Collect session state
    val userSession by sessionManager.userSession.collectAsState(initial = UserSession())

    NavHost(
        navController = navController,
        startDestination = if (userSession.isLoggedIn) "menuList" else "authScreen"
    ) {
        composable("authScreen") {
            AuthScreen(viewModel = authViewModel, navController = navController)
        }

        composable("menuList") {
            MenuScreen(navController = navController, context = context)

            BackHandler {
                // Do nothing - this prevents going back to auth screen
            }
        }

        composable(
            "orderScreen/{menuItem}"
        ) { navBackStackEntry ->
            val json = navBackStackEntry.arguments?.getString("menuItem")
            val menuItem = json?.let {
                kotlinx.serialization.json.Json.decodeFromString(MenuItem.serializer(), it)
            }
            if (menuItem != null) OrderScreen(menuItem)
        }

        composable("profileScreen") {
            SettingsScreen(navController = navController, viewModel = authViewModel)
        }

        composable("userInfoScreen") {
            ProfileScreen(navController = navController, viewModel = authViewModel)
        }




    }

    // Watch for auth state changes
    LaunchedEffect(authViewModel.authState.value) {
        when {
            authViewModel.authState.value.startsWith("Sign in successful") ||
                    authViewModel.authState.value.startsWith("Signup successful") -> {
                navController.navigate("menuList") {
                    popUpTo("authScreen") { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }
}
