package com.youcef_bounaas.cibo.features.Settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.youcef_bounaas.cibo.R
import com.youcef_bounaas.cibo.data.repository.Auth.AuthViewModel
import com.youcef_bounaas.cibo.ui.theme.BabyBlue


@Composable
fun SettingsScreen(navController: NavController, viewModel: AuthViewModel){
    Column (
        modifier = Modifier.fillMaxSize()
            .padding(16.dp)
    ){

        SettingsItems(
            context = LocalContext.current,
            navController = navController
        )
        SignOutButton(navController,viewModel)
        Spacer(modifier = Modifier.weight(1f))

    }

}

@Composable
fun SignOutButton(navController: NavController, viewModel: AuthViewModel) {
    FloatingActionButton(
        onClick = {
            viewModel.signOut { // Pass a callback to handle navigation only after sign-out completes
                navController.navigate("authScreen") {
                    popUpTo(0) { inclusive = true }
                }
            }
        },
        modifier = Modifier.fillMaxWidth().padding(16.dp).clip(RoundedCornerShape(15.dp)),
        shape = RectangleShape,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = BabyBlue,
        elevation = FloatingActionButtonDefaults.elevation(8.dp),
    ) {
        Text(
            "SIGN OUT",
            fontSize = 15.sp,
            fontFamily = FontFamily(Font(R.font.feather_bold))
        )
    }
}
