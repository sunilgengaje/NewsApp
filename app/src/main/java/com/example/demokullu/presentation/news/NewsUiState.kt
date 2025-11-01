package com.example.demokullu.presentation.news

import com.example.demokullu.domain.model.Article

data class NewsUiState(
    val articles: List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isEmpty: Boolean = false
)
