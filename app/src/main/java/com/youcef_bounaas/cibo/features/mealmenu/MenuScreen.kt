package com.youcef_bounaas.cibo.features.mealmenu




import android.content.Context
import android.content.res.Configuration
import android.net.Uri
import android.util.Log
import android.view.MotionEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.youcef_bounaas.cibo.R
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.input.pointer.pointerInput
import androidx.navigation.NavController
import com.youcef_bounaas.cibo.data.model.MenuItem
import com.youcef_bounaas.cibo.features.OwnerOnly.AddMenuItemDialog
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import com.youcef_bounaas.cibo.features.OwnerOnly.OwnerViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    viewModel: MenuViewModel = hiltViewModel(),
    navController: NavController,
    context: Context
) {
    val searchQuery = rememberSaveable { mutableStateOf("") }
    var showDialog by rememberSaveable { mutableStateOf(false) }
    val ownerViewModel: OwnerViewModel = hiltViewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "MENU",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    Image(
                        painterResource(id = R.drawable.user1),
                        contentDescription = "User Image Icon",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(100))
                            .clickable { navController.navigate("profileScreen") }
                    )
                }
            )
            if (showDialog) {
                AddMenuItemDialog(
                    ownerViewModel = ownerViewModel,
                    context = context,
                    onDismiss = { showDialog = false }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Meal")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(100)),
                value = searchQuery.value,
                onValueChange = { newText ->
                    searchQuery.value = newText
                    viewModel.searchMenu(newText)
                },
                placeholder = { Text("Search menu") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search Icon"
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedPlaceholderColor = Color.DarkGray,
                    unfocusedPlaceholderColor = Color.DarkGray,
                    focusedContainerColor = Color.LightGray,
                    unfocusedContainerColor = Color.LightGray,
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CategoriesList(viewModel = viewModel)

                Spacer(modifier = Modifier.height(20.dp))

                MenuList(navController = navController)
            }
        }
    }
}


@Composable
fun MenuList(
    menuViewModel: MenuViewModel = hiltViewModel(),
    ownerViewModel: OwnerViewModel = hiltViewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val menuItems by menuViewModel.menu.collectAsState()
    var isLoading by remember { mutableStateOf(true) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<MenuItem?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePicker = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }

    LaunchedEffect(Unit) {
        isLoading = false
    }

    if (isLoading) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center))
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(menuItems, key = { menuItem -> menuItem.id!! }) { menuItem ->
                if (menuItem.id != null) {
                    Card(
                        modifier = Modifier
                            .size(150.dp, 200.dp)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = {
                                        val jsonMenuItem = Json.encodeToString(MenuItem.serializer(), menuItem)
                                        val encodedMenuItem = Uri.encode(jsonMenuItem)
                                        navController.navigate("orderScreen/$encodedMenuItem")
                                    },
                                    onLongPress = {
                                        selectedItem = menuItem
                                        showDialog = true
                                    }
                                )
                            }
                    ) {
                        Column {
                            AsyncImage(
                                model = menuItem.imageUrl,
                                contentDescription = "Dish Image for ${menuItem.name}",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp),
                                contentScale = ContentScale.Crop
                            )
                            Text(menuItem.name)
                            Text("€${menuItem.price}", color = Color.Yellow)
                        }
                    }
                }
            }
        }

        if (showDialog && selectedItem != null) {
            DeleteOrUpdateDialog(
                menuItem = selectedItem!!,
                onDismiss = {
                    showDialog = false
                    selectedItem = null
                    imageUri = null
                },
                onDelete = { menuItemId ->
                    scope.launch {
                        ownerViewModel.deleteItem(
                            selectedItem!!,
                            menuItemId,
                            onSuccess = {
                                showDialog = false
                                selectedItem = null
                                Toast.makeText(context, "Item deleted successfully", Toast.LENGTH_SHORT).show()
                            },
                            onError = { errorMessage ->
                                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                            }
                        )
                    }
                },
                onUpdate = { updatedMenuItem ->
                    scope.launch {
                        try {
                            ownerViewModel.updateItem(
                                context = context,
                                menuItemId = updatedMenuItem.id!!,
                                updatedMenuItem = updatedMenuItem,
                                newImageUri = imageUri,
                                onSuccess = {
                                    showDialog = false
                                    selectedItem = null
                                    imageUri = null
                                    Toast.makeText(context, "Item updated successfully", Toast.LENGTH_SHORT).show()
                                },
                                onError = { error ->
                                    Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                                }
                            )
                        } catch (e: Exception) {
                            Toast.makeText(context, e.message ?: "Unknown error", Toast.LENGTH_LONG).show()
                        }
                    }
                },
                onPickImage = { imagePicker.launch("image/*") },
                currentImageUri = imageUri
            )
        }
    }
}

@Composable
fun DeleteOrUpdateDialog(
    menuItem: MenuItem,
    onDismiss: () -> Unit,
    onDelete: (Int) -> Unit,
    onUpdate: (MenuItem) -> Unit,
    onPickImage: () -> Unit,
    currentImageUri: Uri?
) {
    var name by remember { mutableStateOf(menuItem.name) }
    var description by remember { mutableStateOf(menuItem.description) }
    var price by remember { mutableStateOf(menuItem.price.toString()) }
    var category by remember { mutableStateOf(menuItem.category) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Update or Delete Item") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") }
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") }
                )
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Price") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category") }
                )

                Button(
                    onClick = onPickImage,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (currentImageUri != null) "Change Image" else "Select New Image")
                }

                if (currentImageUri != null) {
                    AsyncImage(
                        model = currentImageUri,
                        contentDescription = "Selected Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        val updatedItem = menuItem.copy(
                            name = name,
                            description = description,
                            price = price.toDoubleOrNull() ?: menuItem.price,
                            category = category
                        )
                        onUpdate(updatedItem)
                    }
                ) {
                    Text("Update")
                }
                Button(
                    onClick = { onDelete(menuItem.id!!) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Delete")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun CategoriesList(
    viewModel: MenuViewModel = hiltViewModel()
) {
    var selectedCategory by remember { mutableStateOf("") }
    val categories by viewModel.categories.collectAsState()
    val initialMenuItems by viewModel.initialMenuItems.collectAsState()

    LaunchedEffect(categories) {
        println("Categories: $categories")
    }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(categories) { category ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val categoryImage = when (category) {

                    "All" -> R.drawable.food
                    "Pizza" -> R.drawable.ic_pizza
                    "Pasta" -> R.drawable.ic_pasta
                    else -> initialMenuItems.firstOrNull { it.category == category }?.imageUrl
                }



                Card(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .clickable {
                            selectedCategory = category
                            viewModel.fetchMenuByCategory(category)
                        }
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        AsyncImage(
                            model = categoryImage,
                            contentDescription = "Category $category",
                            modifier = Modifier
                                .size(75.dp)
                                .align(Alignment.Center)
                        )
                    }
                }

                Text(
                    category,
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    fontSize = 20.sp,
                    color = Color.DarkGray
                )
            }
        }
    }
}


