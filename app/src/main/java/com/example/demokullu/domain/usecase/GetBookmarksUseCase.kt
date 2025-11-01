package com.example.demokullu.domain.usecase

import com.example.demokullu.domain.model.Article
import com.example.demokullu.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBookmarksUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    operator fun invoke(): Flow<List<Article>> {
        return repository.getBookmarks()
    }
}
