package com.youcef_bounaas.cibo.data.repository

import com.youcef_bounaas.cibo.data.model.MenuItem

interface MenuRepository {
    suspend fun getMenu(): List<MenuItem>
    suspend fun getMenuByCategory(category: String): List<MenuItem>
    suspend fun searchMenu(query: String): List<MenuItem>
}