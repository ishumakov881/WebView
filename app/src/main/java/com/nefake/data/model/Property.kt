package com.nefake.data.model

data class Property(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val address: String = "",
    val totalArea: Double = 0.0,
    val kitchenArea: Double = 0.0,
    val imageUrl: String = "",
    val publishedAt: String = "",
    val type: PropertyType = PropertyType.SALE
)

enum class PropertyType {
    SALE, RENT
} 