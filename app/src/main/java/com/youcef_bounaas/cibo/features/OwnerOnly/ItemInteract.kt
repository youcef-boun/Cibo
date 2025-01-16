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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import com.youcef_bounaas.cibo.data.model.MenuItem



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








@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteOrUpdateDialog(
    menuItem: MenuItem,
    onDismiss: () -> Unit,
    onDelete: (Int) -> Unit, // Only menuItemId needed for delete
    onUpdate: (MenuItem) -> Unit // Entire updatedMenuItem for update
) {
    var updatedName by remember { mutableStateOf(menuItem.name) }
    var updatedPrice by remember { mutableStateOf(menuItem.price.toString()) }
    var updatedDescription by remember { mutableStateOf(menuItem.description) }
    var updatedCategory by remember { mutableStateOf(menuItem.category) }
    var updatedImageUrl by remember { mutableStateOf(menuItem.imageUrl) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Update or Delete Item") },
        text = {
            Column {
                // Editable Name
                TextField(
                    value = updatedName,
                    onValueChange = { updatedName = it },
                    label = { Text("Update Name") }
                )
                // Editable Price
                TextField(
                    value = updatedPrice,
                    onValueChange = { updatedPrice = it },
                    label = { Text("Update Price") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                // Editable Description
                TextField(
                    value = updatedDescription,
                    onValueChange = { updatedDescription = it },
                    label = { Text("Update Description") },
                    maxLines = 3
                )
                // Editable Category
                TextField(
                    value = updatedCategory,
                    onValueChange = { updatedCategory = it },
                    label = { Text("Update Category") }
                )
                // Editable Image URL
                updatedImageUrl?.let {
                    TextField(
                        value = it,
                        onValueChange = { updatedImageUrl = it },
                        label = { Text("Update Image URL") }
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                // Prepare updatedMenuItem with values from TextFields
                val updatedMenuItem = menuItem.copy(
                    name = updatedName,
                    price = updatedPrice.toDoubleOrNull() ?: menuItem.price, // Ensure valid double
                    description = updatedDescription,
                    category = updatedCategory,
                    imageUrl = updatedImageUrl
                )
                onUpdate(updatedMenuItem) // Update item
                onDismiss()
            }) {
                Text("Update")
            }
        },
        dismissButton = {
            Button(onClick = {
                // Delete the item if the user clicks delete button
                onDelete(menuItem.id!!)
                onDismiss()
            }) {
                Text("Delete")
            }
        }
    )
}
