package com.example.demokullu.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.demokullu.domain.model.Article

@Entity(tableName = "bookmarked_articles")
data class ArticleEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String?,
    val imageUrl: String?,
    val source: String?,
    val publishedAt: String?,
    val contentUrl: String?
) {
    fun toDomain(): Article = Article(
        id = id,
        title = title,
        description = description,
        imageUrl = imageUrl,
        source = source,
        publishedAt = publishedAt,
        contentUrl = contentUrl,
        isBookmarked = true
    )

    companion object {
        fun fromDomain(a: Article) = ArticleEntity(
            id = a.id,
            title = a.title,
            description = a.description,
            imageUrl = a.imageUrl,
            source = a.source,
            publishedAt = a.publishedAt,
            contentUrl = a.contentUrl
        )
    }
}
