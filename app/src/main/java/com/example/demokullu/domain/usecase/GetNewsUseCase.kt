package com.example.demokullu.domain.usecase

import androidx.paging.PagingData
import com.example.demokullu.domain.model.Article
import com.example.demokullu.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    operator fun invoke(): Flow<PagingData<Article>> {
        return repository.getNews()
    }
}
