package com.youcef_bounaas.cibo.features.profile.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.youcef_bounaas.cibo.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.youcef_bounaas.cibo.features.profile.data.ProfileViewModel
import com.youcef_bounaas.cibo.ui.theme.BabyBlue
import com.youcef_bounaas.cibo.ui.theme.BloodRed


@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel ,
    modifier: Modifier = Modifier
) {

    // Collect state values
    val name by viewModel.name.collectAsState()
    val username by viewModel.username.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()

    // Profile font
    val profileFont = FontFamily(Font(R.font.feather_bold))

    // Determine current theme
    val isDarkTheme = isSystemInDarkTheme()

    // Color scheme adaptations
    val backgroundColor = if (isDarkTheme) Color.DarkGray else Color.White
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val inputBackgroundColor = if (isDarkTheme) Color.Gray else Color.LightGray

    BackHandler {

        navController.popBackStack()
    }

    Scaffold(
        containerColor = backgroundColor,
        contentColor = textColor
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top App Bar Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navController.navigateUp() },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "Back",
                            tint = textColor
                        )
                    }

                    Text(
                        text = "Profile",
                        fontFamily = profileFont,
                        color = textColor,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.width(36.dp))
                }

                Divider(color = textColor.copy(alpha = 0.3f))
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Profile Avatar Section
            item {
                Image(
                    painter = painterResource(R.drawable.user1),
                    contentDescription = "User Avatar",
                    modifier = Modifier
                        .size(75.dp)
                        .clip(RoundedCornerShape(100))
                        .clickable { /* Avatar change logic */ },
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = "CHANGE AVATAR",
                    fontFamily = profileFont,
                    color = BabyBlue,
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .clickable { /* Avatar change logic */ }
                )

                Spacer(modifier = Modifier.height(20.dp))
            }

            // Profile Input Fields
            item {
                ProfileTextField(
                    value = name,
                    onValueChange = { viewModel.updateName(it) },
                    label = "Name",
                    fontFamily = profileFont,
                    backgroundColor = inputBackgroundColor,
                )

                ProfileTextField(
                    value = username,
                    onValueChange = { viewModel.updateUsername(it) },
                    label = "Username",
                    fontFamily = profileFont,
                    backgroundColor = inputBackgroundColor,
                )

                ProfileTextField(
                    value = password,
                    onValueChange = { viewModel.updatePassword(it) },
                    label = "Password",
                    fontFamily = profileFont,
                    backgroundColor = inputBackgroundColor,
                )

                ProfileTextField(
                    value = email,
                    onValueChange = { viewModel.updateEmail(it) },
                    label = "Email",
                    fontFamily = profileFont,
                    keyboardType = KeyboardType.Email,
                    backgroundColor = inputBackgroundColor,
                )

                Spacer(modifier = Modifier.height(20.dp))
            }

            // Delete Account Button
            item {
                var deleteButtonText by remember { mutableStateOf("Delete Account") }
                Button(
                    onClick = {
                        deleteButtonText = "Account Deleted"
                        viewModel.deleteAccount()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDarkTheme) Color.White else BloodRed,
                        contentColor = if (isDarkTheme) BloodRed else Color.White
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = deleteButtonText,
                        fontFamily = profileFont,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    fontFamily: FontFamily,
    keyboardType: KeyboardType = KeyboardType.Text,
    backgroundColor: Color,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 5.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 40.dp, max = 65.dp),
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = backgroundColor,

            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            ),
        )
    }
}
