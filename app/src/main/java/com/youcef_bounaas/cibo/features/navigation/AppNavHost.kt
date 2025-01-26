package com.youcef_bounaas.cibo.features.navigation


import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.youcef_bounaas.cibo.data.model.MenuItem
import com.youcef_bounaas.cibo.data.model.SessionManager
import com.youcef_bounaas.cibo.data.model.UserSession
import com.youcef_bounaas.cibo.data.repository.Auth.AuthViewModel
import com.youcef_bounaas.cibo.data.repository.Auth.AuthViewModelFactory
import com.youcef_bounaas.cibo.features.Authentication.AuthScreen
import com.youcef_bounaas.cibo.features.Settings.SettingsScreen
import com.youcef_bounaas.cibo.features.mealmenu.MenuScreen
import com.youcef_bounaas.cibo.features.mealmenu.OrderScreen
import com.youcef_bounaas.cibo.features.profile.data.ProfileViewModel
import com.youcef_bounaas.cibo.features.profile.presentation.ProfileScreen
import kotlinx.serialization.json.Json



@Composable
fun AppNavHost() {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager.getInstance(context) }
    val userSession by sessionManager.userSession.collectAsState(initial = UserSession())
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(context))
    val navController = rememberNavController()

    Log.d("AppNavHost", "User session: $userSession")

    val isSessionLoading by sessionManager.isSessionLoading.collectAsState(initial = true)
    if (isSessionLoading) {
        CircularProgressIndicator()
        // Show a loading screen or splash screen while session is being fetched
        return
    }

    NavHost(
        navController = navController,
        startDestination = if (userSession.isLoggedIn) "menuList" else "authScreen"
    ) {
        composable("authScreen") {
            Log.d("AppNavHost", "Navigating to authScreen")
            AuthScreen(viewModel = authViewModel, navController = navController)
        }

        composable("menuList") {
            Log.d("AppNavHost", "Navigating to menuList")
            MenuScreen(navController = navController, context = context)

            BackHandler {
                // Do nothing - this prevents going back to auth screen
            }
        }

        composable("orderScreen/{menuItem}") { navBackStackEntry ->
            val json = navBackStackEntry.arguments?.getString("menuItem")
            val menuItem = json?.let {
                Json.decodeFromString(MenuItem.serializer(), it)
            }
            if (menuItem != null) {
                Log.d("AppNavHost", "Navigating to orderScreen with menuItem: $menuItem")
                OrderScreen(menuItem)
            }
        }

        composable("profileScreen") {
            Log.d("AppNavHost", "Navigating to profileScreen")
            SettingsScreen(navController = navController, viewModel = authViewModel)
        }

        composable("userInfoScreen") {
            Log.d("AppNavHost", "Navigating to userInfoScreen")
            val profileViewModel: ProfileViewModel = hiltViewModel()
            ProfileScreen(navController = navController, profileViewModel)
        }

    }



// Watch for auth state changes
LaunchedEffect(authViewModel.authState.value) {
    Log.d("AppNavHost", "Auth state changed: ${authViewModel.authState.value}")
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

