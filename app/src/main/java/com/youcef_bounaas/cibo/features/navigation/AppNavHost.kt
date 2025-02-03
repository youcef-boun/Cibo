package com.youcef_bounaas.cibo.features.navigation


import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.youcef_bounaas.cibo.features.Tracking.presentation.TrackingScreen
import com.youcef_bounaas.cibo.features.cart.presentation.CartScreen
import com.youcef_bounaas.cibo.features.favourites.presentation.FavouriteScreen
import com.youcef_bounaas.cibo.features.mealmenu.MenuScreen
import com.youcef_bounaas.cibo.features.mealmenu.OrderScreen
import com.youcef_bounaas.cibo.features.profile.data.ProfileViewModel
import com.youcef_bounaas.cibo.features.profile.presentation.ProfileScreen
import kotlinx.serialization.json.Json
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import com.youcef_bounaas.cibo.features.roles.data.UsersViewModel
import com.youcef_bounaas.cibo.features.roles.presentation.UsersScreen


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

    val items = listOf(
        BottomNavigationItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            hasNews = false
        ),
        BottomNavigationItem(
            title = "My Order",
            selectedIcon = Icons.Filled.Map,
            unselectedIcon = Icons.Outlined.Map,
            hasNews = false
        ),
        BottomNavigationItem(
            title = "My Cart",
            selectedIcon = Icons.Filled.ShoppingCart,
            unselectedIcon = Icons.Outlined.ShoppingCart,
            hasNews = false
        ),
        BottomNavigationItem(
            title = "Saved",
            selectedIcon = Icons.Filled.Favorite,
            unselectedIcon = Icons.Outlined.FavoriteBorder,
            hasNews = false
        )
    )

    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }

    // Update selectedItemIndex based on the current destination
    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            selectedItemIndex = when (destination.route) {
                "menuList" -> 0
                "TrackingOrder" -> 1
                "CartScreen" -> 2
                "Favourite" -> 3
                else -> selectedItemIndex
            }
        }
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
            ScaffoldWithBottomBar(navController, items, selectedItemIndex) {
                MenuScreen(navController = navController, context = context)
            }
        }

        composable("CartScreen") {
            ScaffoldWithBottomBar(navController, items, selectedItemIndex) {
                CartScreen(navController)
            }
        }

        composable("TrackingOrder") {
            ScaffoldWithBottomBar(navController, items, selectedItemIndex) {
                TrackingScreen(navController)
            }
        }

        composable("Favourite") {
            ScaffoldWithBottomBar(navController, items, selectedItemIndex) {
                FavouriteScreen(navController)
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


            composable("users") {
                val viewModel = remember {
                    UsersViewModel(context, SavedStateHandle())
                }
                UsersScreen(
                    viewModel = viewModel,
                    navController = navController
                )
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

    BackHandler {
        navController.navigate("menuList") {
            popUpTo(navController.graph.startDestinationId) { inclusive = true }
        }
    }
}

@Composable
fun ScaffoldWithBottomBar(
    navController: NavController,
    items: List<BottomNavigationItem>,
    selectedItemIndex: Int,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = {
                            when (index) {
                                0 -> navController.navigate("menuList") {
                                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                }
                                1 -> navController.navigate("TrackingOrder") {
                                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                }
                                2 -> navController.navigate("CartScreen") {
                                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                }
                                3 -> navController.navigate("Favourite") {
                                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                }
                            }
                        },
                        label = { Text(text = item.title) },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (item.badgeCount != null) {
                                        Text(text = item.badgeCount.toString())
                                    } else if (item.hasNews) {
                                        Badge()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (index == selectedItemIndex) {
                                        item.selectedIcon
                                    } else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}

data class BottomNavigationItem(
    val title : String,
    val selectedIcon : ImageVector,
    val unselectedIcon : ImageVector,
    val badgeCount: Int? = null,
    val hasNews : Boolean

)