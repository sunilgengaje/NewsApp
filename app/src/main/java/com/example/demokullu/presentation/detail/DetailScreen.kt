package com.example.demokullu.presentation.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.demokullu.domain.model.Article
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.demokullu.presentation.news.NewsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(article: Article, viewModel: NewsViewModel = hiltViewModel()) {
    val context = LocalContext.current

    // observe bookmark state for this article
    val bookmarks by viewModel.bookmarks.collectAsState()
    val isBookmarked = remember(bookmarks) { bookmarks.any { it.id == article.id } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(article.title) },
                actions = {
                    IconButton(onClick = {
                        viewModel.toggleBookmark(article)
                    }) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                            contentDescription = if (isBookmarked) "Unbookmark" else "Bookmark"
                        )
                    }

                    IconButton(onClick = {
                        val shareText = article.contentUrl ?: "No link"
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        context.startActivity(Intent.createChooser(intent, "Share via"))
                    }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(text = article.description ?: "No description", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    article.contentUrl?.let {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                        context.startActivity(intent)
                    }
                },
                enabled = !article.contentUrl.isNullOrEmpty(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Open in Browser")
            }
        }
    }
}
