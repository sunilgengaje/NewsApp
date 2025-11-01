package com.example.demokullu.domain.model

data class Article(
    val id: Int,
    val title: String,
    val description: String?,
    val imageUrl: String?,
    val source: String?,
    val publishedAt: String?,
    val contentUrl: String?,
    val isBookmarked: Boolean = false
)
