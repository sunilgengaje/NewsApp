package com.example.demokullu.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.demokullu.data.local.ArticleDao
import com.example.demokullu.data.local.ArticleEntity
import com.example.demokullu.data.paging.NewsPagingSource
import com.example.demokullu.data.remote.NewsApi
import com.example.demokullu.domain.model.Article
import com.example.demokullu.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val api: NewsApi,
    private val dao: ArticleDao
) : NewsRepository {

    // ✅ Fetch paginated news (default feed)
    override fun getNews(): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { NewsPagingSource(api, null) }
        ).flow
    }

    // ✅ Fetch paginated search results
    override fun searchNews(query: String): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { NewsPagingSource(api, query) }
        ).flow
    }

    // ✅ Flow of all bookmarks from Room
    override fun getBookmarks(): Flow<List<Article>> {
        return dao.getBookmarks().map { list ->
            list.map { it.toDomain() }
        }
    }

    // ✅ Add/remove bookmark toggle
    override suspend fun toggleBookmark(article: Article) {
        val existing = dao.getArticleById(article.id)
        if (existing != null) {
            dao.delete(existing)
        } else {
            dao.insert(article.toEntity())
        }
    }

    // ✅ Safe converters between Entity ↔ Domain
    private fun Article.toEntity() = ArticleEntity(
        id = id,
        title = title,
        description = description ?: "",
        imageUrl = imageUrl,
        source = source ?: "",
        publishedAt = publishedAt ?: "",
        contentUrl = contentUrl ?: ""
    )

    private fun ArticleEntity.toDomain() = Article(
        id = id,
        title = title,
        description = description,
        imageUrl = imageUrl,
        source = source,
        publishedAt = publishedAt,
        contentUrl = contentUrl,
        isBookmarked = true
    )
}
