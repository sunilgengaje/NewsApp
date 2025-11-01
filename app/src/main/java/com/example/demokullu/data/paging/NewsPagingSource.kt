package com.example.demokullu.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.demokullu.data.remote.NewsApi
import com.example.demokullu.domain.model.Article

class NewsPagingSource(
    private val api: NewsApi,
    private val query: String? // null = normal feed, not search
) : PagingSource<Int, Article>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: 0
        return try {
            val response = if (query.isNullOrBlank()) {
                api.getNews(limit = 10, skip = page * 10)
            } else {
                api.searchNews(query = query, limit = 10, skip = page * 10)
            }

            val articles = response.posts.map {
                Article(
                    id = it.id,
                    title = it.title,
                    description = it.body,
                    imageUrl = "https://picsum.photos/400?random=${it.id}",
                    source = it.tags?.joinToString(", "),
                    publishedAt = null,
                    contentUrl = "https://dummyjson.com/posts/${it.id}"
                )
            }

            LoadResult.Page(
                data = articles,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (articles.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { pos ->
            val anchorPage = state.closestPageToPosition(pos)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
