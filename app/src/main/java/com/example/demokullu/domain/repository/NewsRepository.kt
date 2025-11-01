package com.example.demokullu.domain.repository

import androidx.paging.PagingData
import com.example.demokullu.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getNews(): Flow<PagingData<Article>>
    fun searchNews(query: String): Flow<PagingData<Article>>
    fun getBookmarks(): Flow<List<Article>>
    suspend fun toggleBookmark(article: Article)
}
