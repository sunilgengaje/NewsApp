package com.example.demokullu.data.remote.dto

data class NewsResponse(
    val posts: List<PostDto>,
    val total: Int,
    val skip: Int,
    val limit: Int
)

data class PostDto(
    val id: Int,
    val title: String,
    val body: String?,
    val userId: Int,
    val tags: List<String>?,
    val reactions: ReactionsDto
)

data class ReactionsDto(
    val likes: Int,
    val dislikes: Int
)
