package com.youcef_bounaas.cibo.features.mealmenu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.youcef_bounaas.cibo.R

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





