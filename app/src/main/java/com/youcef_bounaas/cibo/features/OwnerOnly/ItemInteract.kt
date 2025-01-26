package com.youcef_bounaas.cibo.features.OwnerOnly

import android.content.Context
import android.util.Log
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel



@Composable
fun AddMenuItemDialog(
    ownerViewModel: OwnerViewModel = hiltViewModel()
    ,
    context: Context,
    onDismiss: () -> Unit
) {
    var itemName by remember { mutableStateOf("") }
    var itemDescription by remember { mutableStateOf("") }
    var itemPrice by remember { mutableStateOf("") }
    var itemCategory by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        imageUri = it
    }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Add New Menu Item") },
        text = {
            Column {
                OutlinedTextField(
                    value = itemName,
                    onValueChange = { itemName = it },
                    label = { Text("Name") }
                )
                OutlinedTextField(
                    value = itemDescription,
                    onValueChange = { itemDescription = it },
                    label = { Text("Description") }
                )
                OutlinedTextField(
                    value = itemPrice,
                    onValueChange = { itemPrice = it },
                    label = { Text("Price") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = itemCategory,
                    onValueChange = { itemCategory = it },
                    label = { Text("Category") }
                )
                Button(onClick = { launcher.launch("image/*") }) {
                    Text("Select Image")
                }
                Text(text = imageUri?.toString() ?: "No image selected")
            }
        },
        confirmButton = {
            Button(onClick = {
                ownerViewModel.uploadData(
                    context = context,
                    name = itemName,
                    description = itemDescription,
                    price = itemPrice.toDoubleOrNull() ?: 0.0,
                    category = itemCategory,
                    imageUri = imageUri,
                    onSuccess = { onDismiss() },
                    onError = { Log.e("Error", it) }
                )
            }) {
                Text("Submit")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    )
}









