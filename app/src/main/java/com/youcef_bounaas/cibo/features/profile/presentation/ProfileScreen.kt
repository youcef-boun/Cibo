package com.youcef_bounaas.cibo.features.profile.presentation


import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.youcef_bounaas.cibo.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import com.youcef_bounaas.cibo.data.repository.SupabaseClientProvider.supabase
import com.youcef_bounaas.cibo.ui.theme.BabyBlue
import com.youcef_bounaas.cibo.ui.theme.BloodRed
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put


@Composable
fun ProfileScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val isDarkTheme = isSystemInDarkTheme()
    val backgroundColor = if (isDarkTheme) Color.DarkGray else Color.White
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val inputBackgroundColor = if (isDarkTheme) Color.Gray else Color.LightGray

    val profileFont = FontFamily(Font(R.font.feather_bold))
    val context = LocalContext.current

    val user = supabase.auth.currentUserOrNull()
    val userId = user?.id




    // State for user data
    var name by remember { mutableStateOf("") }
    var useremail by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Fetch user data on launch
    LaunchedEffect(Unit) {
        try {

            if (user == null) {
                // Redirect to login screen if user is not logged in
                navController.navigate("authScreen")
            } else {
                name = user.userMetadata?.get("display_name")?.jsonPrimitive?.contentOrNull ?: ""
                useremail = user.email ?: ""
            }
        } catch (e: Exception) {
            errorMessage = "Failed to fetch user data: ${e.message}"
            Log.e("ProfileScreen", "Error fetching user data", e)
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        containerColor = backgroundColor,
        contentColor = textColor
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { navController.navigateUp() }
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

                        Spacer(modifier = Modifier.width(48.dp))
                    }
                    Divider(color = textColor.copy(alpha = 0.3f))
                    Spacer(modifier = Modifier.height(20.dp))
                }

                // Profile Fields
                item {
                    ProfileTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = "Name",
                        fontFamily = profileFont,
                        backgroundColor = inputBackgroundColor,
                    )

                    ProfileTextField(
                        value = useremail,
                        onValueChange = { useremail = it },
                        label = "Email",
                        fontFamily = profileFont,
                        keyboardType = KeyboardType.Email,
                        backgroundColor = inputBackgroundColor,
                    )

                    ProfileTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = "New Password (leave blank to keep current)",
                        fontFamily = profileFont,
                        backgroundColor = inputBackgroundColor,
                        isPassword = true
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }

                // Save Button
                item {
                    Button(
                        onClick = {
                            isLoading = true
                            CoroutineScope(Dispatchers.IO).launch {
                                try {

                                    if (user == null) {
                                        withContext(Dispatchers.Main) {
                                            Toast.makeText(context, "No authenticated user found", Toast.LENGTH_SHORT).show()
                                        }
                                        return@launch
                                    }

                                    var emailChanged = false

                                    // Check if email change is requested
                                    if (useremail.isNotEmpty() && useremail != user.email) {
                                        emailChanged = true
                                    }

                                    // Update user info
                                    supabase.auth.updateUser {
                                        data = buildJsonObject {
                                            put("display_name", name)
                                        }

                                        if (newPassword.isNotEmpty()) {
                                            password = newPassword
                                        }

                                        if (emailChanged) {
                                            email = useremail
                                        }
                                    }

                                    // Show success message on UI thread
                                    withContext(Dispatchers.Main) {
                                        if (emailChanged) {
                                            Toast.makeText(context, "Info updated! Check your email to confirm.", Toast.LENGTH_LONG).show()
                                        } else {
                                            Toast.makeText(context, "Your info has been updated successfully!", Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                } catch (e: Exception) {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(context, "Error updating info: ${e.message}", Toast.LENGTH_LONG).show()
                                    }
                                    Log.e("ProfileScreen", "Error updating user data", e)
                                } finally {
                                    withContext(Dispatchers.Main) {
                                        isLoading = false
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = BabyBlue),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "Save Changes",
                            fontFamily = profileFont,
                            fontSize = 20.sp
                        )
                    }


                    Spacer(modifier = Modifier.height(20.dp))
                }

                // Delete Account Button
                item {
                    Button(
                        onClick = {
                            isLoading = true
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    supabase.auth.admin.deleteUser(uid = userId.toString())

                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(context, "Account deleted successfully!", Toast.LENGTH_SHORT).show()
                                        navController.navigate("authScreen") // Navigate to login screen
                                    }
                                } catch (e: Exception) {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(context, "Failed to delete account: ${e.message}", Toast.LENGTH_LONG).show()
                                    }
                                    Log.e("DeleteUser", "Error deleting user", e)
                                } finally {
                                    isLoading = false
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isDarkTheme) Color.White else BloodRed,
                            contentColor = if (isDarkTheme) BloodRed else Color.White
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "Delete Account",
                            fontFamily = profileFont,
                            fontSize = 20.sp
                        )
                    }
                }
            }
            // Loading Indicator
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = BabyBlue
                )
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
    isPassword: Boolean = false // Add this parameter
) {
    var passwordVisible by remember { mutableStateOf(false) }

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
                keyboardType = if (isPassword) KeyboardType.Password else keyboardType
            ),
            visualTransformation = if (isPassword && !passwordVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            trailingIcon = {
                if (isPassword) {
                    IconButton(
                        onClick = { passwordVisible = !passwordVisible }
                    ) {
                        Icon(
                            imageVector = if (passwordVisible) {
                                Icons.Default.VisibilityOff
                            } else {
                                Icons.Default.Visibility
                            },
                            contentDescription = if (passwordVisible) {
                                "Hide password"
                            } else {
                                "Show password"
                            }
                        )
                    }
                }
            }
        )
    }
}