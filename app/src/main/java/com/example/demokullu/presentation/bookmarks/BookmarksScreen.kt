package com.example.demokullu.presentation.bookmarks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.demokullu.domain.model.Article
import com.example.demokullu.presentation.news.NewsViewModel
import com.example.demokullu.presentation.news.components.ArticleCard

@Composable
fun BookmarksScreen(
    viewModel: NewsViewModel = hiltViewModel(),
    onArticleClick: (Article) -> Unit
) {
    val bookmarks = viewModel.bookmarks.collectAsState().value

    if (bookmarks.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No bookmarks yet")
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(bookmarks) { article ->
                ArticleCard(
                    article = article,
                    isBookmarked = true,
                    onClick = { onArticleClick(article) },
                    onBookmarkClick = { viewModel.toggleBookmark(article) }
                )
            }
        }
    }
}
