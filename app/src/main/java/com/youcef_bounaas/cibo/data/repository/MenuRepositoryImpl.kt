package com.youcef_bounaas.cibo.data.repository


import com.youcef_bounaas.cibo.data.model.MenuItem
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Inject




class MenuRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest
): MenuRepository {
    override suspend fun getMenu(): List<MenuItem> {
        return postgrest["menus"]
            .select()
            .decodeList<MenuItem>()    }

    override suspend fun getMenuByCategory(category: String): List<MenuItem> {
        return postgrest["menus"].select {
            filter {
                eq("category", category) // Use 'eq' within 'filter' block
            }
        }.decodeList<MenuItem>()
    }


    override suspend fun searchMenu(query: String): List<MenuItem> {
        return postgrest["menus"].select {
            filter {
               ilike("name","%$query%") // Use 'eq' within 'filter' block
            }
        }.decodeList<MenuItem>()
    }


}