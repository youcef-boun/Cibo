package com.youcef_bounaas.cibo.features.profile.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.youcef_bounaas.cibo.R
import com.youcef_bounaas.cibo.data.repository.Auth.AuthViewModel
import com.youcef_bounaas.cibo.ui.theme.BloodRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController, viewModel: AuthViewModel
){
    val profileFont = FontFamily(Font( R.font.feather_bold))

    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.White)
            .padding(top = 20.dp)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.clickable {
                    navController.navigate("profileScreen")
                },
                imageVector = Icons.Outlined.ArrowBack,
                contentDescription = "back button",
            )

            Text(
                text = "Profile",
                fontFamily = profileFont,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            // Empty spacer to balance the layout
            Spacer(modifier = Modifier.width(24.dp))
        }

        Divider()
Spacer(modifier = Modifier.size(40.dp))

        Image(

           painter =  painterResource(R.drawable.user1),
            "user avatar",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(80.dp)
                .clip(RoundedCornerShape(100))
        )

        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable {  },
            text = "CHANGE AVATAR",
            fontFamily = profileFont,
        )

        Spacer(modifier = Modifier.size(30.dp))

        Text(
            modifier = Modifier.padding(vertical = 5.dp),
            text = "Name",
            fontFamily = profileFont,
            fontWeight = FontWeight.Bold
        )

        var text = TextFieldValue("")
        OutlinedTextField(
            modifier = Modifier.height(30.dp)
                .fillMaxWidth(),
            value = text,
            onValueChange = { text = it },
            shape = RoundedCornerShape(10.dp),
            colors =   TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.LightGray, // Background color of the TextField container
                focusedBorderColor = Color.DarkGray, // Border color when focused
                unfocusedBorderColor = Color.Gray, // Border color when not focused

            )
        )
        Text(
            modifier = Modifier.padding(vertical = 5.dp),
            text = "Username",
            fontFamily = profileFont,
            fontWeight = FontWeight.Bold
        )


        OutlinedTextField(
            modifier = Modifier.height(30.dp).fillMaxWidth(),
            value = text,
            onValueChange = { text = it },
            shape = RoundedCornerShape(10.dp),
            colors =   TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.LightGray, // Background color of the TextField container
                focusedBorderColor = Color.DarkGray, // Border color when focused
                unfocusedBorderColor = Color.Gray, // Border color when not focused

            )
        )
        Text(
            modifier = Modifier.padding(vertical = 5.dp),
            text = "Password",
            fontFamily = profileFont,
            fontWeight = FontWeight.Bold
        )


        OutlinedTextField(
            modifier = Modifier.height(30.dp).fillMaxWidth(),
            value = text,
            onValueChange = { text = it },
            shape = RoundedCornerShape(10.dp),
            colors =   TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.LightGray, // Background color of the TextField container
                focusedBorderColor = Color.DarkGray, // Border color when focused
                unfocusedBorderColor = Color.Gray, // Border color when not focused

            )
        )
        Text(
            modifier = Modifier.padding(vertical = 5.dp),
            text = "Email",
            fontFamily = profileFont,
            fontWeight = FontWeight.Bold
        )


        OutlinedTextField(
            modifier = Modifier.height(30.dp).fillMaxWidth(),
            value = text,
            onValueChange = { text = it },
            shape = RoundedCornerShape(10.dp),
            colors =   TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.LightGray,
                focusedBorderColor = Color.DarkGray,
                unfocusedBorderColor = Color.Gray,

            )
        )

     Spacer(modifier=Modifier.size(20.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {},
            shape = RoundedCornerShape(10.dp),
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = BloodRed,
                disabledContainerColor = Color.LightGray,
                disabledContentColor = Color.Black
            ),


        ) {
            Text(
                text = "DELETE ACCOUNT",
                fontFamily = profileFont
            )
        }





    }




}


