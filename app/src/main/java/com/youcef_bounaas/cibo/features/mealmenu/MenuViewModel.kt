package com.youcef_bounaas.cibo.features.mealmenu


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youcef_bounaas.cibo.data.model.MenuItem
import com.youcef_bounaas.cibo.data.repository.MenuRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val menuRepository: MenuRepository
) : ViewModel() {

    private val _menu = MutableStateFlow<List<MenuItem>>(emptyList())
    val menu: StateFlow<List<MenuItem>> = _menu

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories

    // Add this to store the initial menu items for category images
    private val _initialMenuItems = MutableStateFlow<List<MenuItem>>(emptyList())
    val initialMenuItems: StateFlow<List<MenuItem>> = _initialMenuItems


    init {
        fetchMenu()
        fetchCategories()
    }

    private fun fetchMenu() {
        viewModelScope.launch {
            try {
                val items = menuRepository.getMenu()
                _menu.emit(items)
                _initialMenuItems.emit(items)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            try {
                val menuItems = menuRepository.getMenu() // Retrieve all menu items
                val allCategory = "All"

                // Extract unique categories from menu items and include "All"
                val categoriesList = menuItems.map { it.category }.distinct()
                val allCategories = listOf(allCategory) + categoriesList

                _categories.emit(allCategories)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun fetchMenuByCategory(category: String) {
        viewModelScope.launch {
            try {
                val items = if (category == "All") {
                    // If "All" is selected, show all menu items
                    menuRepository.getMenu()
                } else {
                    // Otherwise, filter by the selected category
                    menuRepository.getMenuByCategory(category)
                }
                _menu.emit(items)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun searchMenu(query: String) {
        viewModelScope.launch {
            try {
                if (query.isNotEmpty()) {
                    val results = menuRepository.searchMenu(query)
                    _menu.emit(results) // Emit results for search
                } else {
                    _menu.emit(emptyList()) // Empty query should reset results
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun refreshMenu() {
        viewModelScope.launch {
            try {
                val items = menuRepository.getMenu()
                _menu.emit(items)
                _initialMenuItems.emit(items)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }




}
