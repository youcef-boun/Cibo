package com.youcef_bounaas.cibo.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MenuItem(
    val id: Int? = null,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    @SerialName("image_url")
    val imageUrl: String? = null
)


