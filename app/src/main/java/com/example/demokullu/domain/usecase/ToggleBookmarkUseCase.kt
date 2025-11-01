package com.example.demokullu.domain.usecase

import com.example.demokullu.domain.model.Article
import com.example.demokullu.domain.repository.NewsRepository
import javax.inject.Inject

class ToggleBookmarkUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(article: Article) {
        repository.toggleBookmark(article)
    }
}
