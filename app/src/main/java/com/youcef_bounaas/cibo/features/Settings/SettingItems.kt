package com.youcef_bounaas.cibo.features.Settings

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.NavController
import com.youcef_bounaas.cibo.R
import com.youcef_bounaas.cibo.features.notifications.showLocalNotification


@Composable
fun SettingsItems(navController: NavController,context: Context) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Settings",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))

        SettingsSection(title = "Account") {

            SettingsItem("Preferences"){ } // all roles
            SettingsItem("Profile"){ navController.navigate("userInfoScreen") } // all roles
            SettingsItem("Notifications"){ showLocalNotification(context) } // costumer , staff
            SettingsItem("roles management"){ navController.navigate("users")} // Admin only



        }
        Spacer(modifier = Modifier.height(16.dp))


    }
}

@Composable
fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(BorderStroke(1.dp, Color.Gray), shape = RoundedCornerShape(8.dp))
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        content()
    }
}

@Composable
fun SettingsItem(title: String, onClick: () -> Unit) {
    Column {
        Divider()
        Text(
            text = title,
            fontFamily = FontFamily(Font(R.font.feather_bold)),
            fontSize = 18.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable {
                    onClick()
                }
        )
    }
}



