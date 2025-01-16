package com.youcef_bounaas.cibo.features.OwnerOnly

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youcef_bounaas.cibo.data.model.MenuItem
import com.youcef_bounaas.cibo.data.repository.SupabaseClientProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import javax.inject.Inject
import io.github.jan.supabase.storage.storage


@HiltViewModel
class OwnerViewModel @Inject constructor() : ViewModel() {
    private val supabaseClient = SupabaseClientProvider.supabase

    fun uploadData(
        context: Context,
        name: String,
        description: String,
        price: Double,
        category: String,
        imageUri: Uri?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val fileName = "${System.currentTimeMillis()}.jpg"
                val inputStream = imageUri?.let { context.contentResolver.openInputStream(it) }
                if (inputStream == null) {
                    onError("Image selection failed")
                    return@launch
                }

                val byteArray = inputStream.readBytes()

                Log.d("OwnerViewModel", "Uploading image: $fileName")

                val bucket = supabaseClient.storage.from("menu-images")
                bucket.upload(fileName, byteArray) {
                    upsert = false
                }

                Log.d("OwnerViewModel", "File uploaded successfully: $fileName")

                val imageUrl = supabaseClient.storage.from("menu-images").publicUrl(fileName)

                val menuItem = MenuItem(
                    name = name,
                    description = description,
                    price = price,
                    category = category,
                    imageUrl = imageUrl
                )

                Log.d("OwnerViewModel", "Inserting into database: $menuItem")

                supabaseClient.from("menus")
                    .insert(menuItem)

                Log.d("OwnerViewModel", "Successfully inserted item into database.")
                onSuccess()
            } catch (e: Exception) {
                Log.e("OwnerViewModel", "Error uploading data: ${e.message}")
                onError(e.message ?: "Unknown error")
            }
        }
    }


    fun deleteItem(
        menuItem: MenuItem,
        menuItemId: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                if (menuItemId <= 0) {
                    onError("Invalid Item ID")
                    return@launch
                }

                Log.d("OwnerViewModel", "Attempting to delete item with ID: $menuItemId")

                val result = supabaseClient.from("menus")
                    .delete {
                        filter { eq("id", menuItemId) }
                    }


                Log.d("OwnerViewModel", "Supabase response: ${result.data}")

                if (result.data.isNullOrEmpty()) {
                    Log.e("OwnerViewModel", "Failed to delete item, no data was affected.")
                    onError("Deletion failed. No data was deleted.")
                } else {
                    Log.d("OwnerViewModel", "Item deleted successfully.")
                    onSuccess()  // Notify success on successful deletion
                }
            } catch (e: Exception) {
                Log.e("OwnerViewModel", "Error deleting item: ${e.message}")
                onError(e.message ?: "Unknown error")
            }
        }
    }


    fun updateItem(
        context: Context,
        menuItemId: Int,
        updatedMenuItem: MenuItem,
        newImageUri: Uri?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Make sure ID is valid
                if (menuItemId <= 0) {
                    onError("Invalid Item ID")
                    return@launch
                }

                // Only handle image upload if a new image is provided
                var finalMenuItem = updatedMenuItem
                if (newImageUri != null) {
                    val fileName = "${System.currentTimeMillis()}.jpg"
                    val inputStream = context.contentResolver.openInputStream(newImageUri)
                    if (inputStream == null) {
                        onError("Image selection failed")
                        return@launch
                    }

                    val byteArray = inputStream.readBytes()

                    Log.d("OwnerViewModel", "Uploading new image: $fileName")

                    val bucket = supabaseClient.storage.from("menu-images")
                    bucket.upload(fileName, byteArray) {
                        upsert = false
                    }

                    Log.d("OwnerViewModel", "New file uploaded successfully: $fileName")

                    val newImageUrl = supabaseClient.storage.from("menu-images").publicUrl(fileName)

                    // Update the MenuItem with the new image URL
                    finalMenuItem = updatedMenuItem.copy(imageUrl = newImageUrl)
                }

                val result = supabaseClient.from("menus")
                    .update(finalMenuItem) {
                        filter { eq("id", menuItemId) }
                    }

                if (result.data.isEmpty()) {
                    val error = "Failed to update item, no data was affected."
                    Log.e("OwnerViewModel", error)
                    onError(error)
                } else {
                    Log.d("OwnerViewModel", "Successfully updated item: $result")
                    onSuccess()
                }
            } catch (e: Exception) {
                Log.e("OwnerViewModel", "Error updating item: ${e.message}")
                onError(e.message ?: "Unknown error")
            }
        }
    }
}


