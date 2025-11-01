package com.example.demokullu.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {

    @Query("SELECT * FROM bookmarked_articles")
    fun getBookmarks(): Flow<List<ArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: ArticleEntity)

    @Delete
    suspend fun delete(article: ArticleEntity)

    @Query("SELECT * FROM bookmarked_articles WHERE id = :id LIMIT 1")
    suspend fun getArticleById(id: Int): ArticleEntity?
}
