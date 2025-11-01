/*
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
*/


package com.example.demokullu.presentation.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.demokullu.domain.model.Article
import com.example.demokullu.presentation.news.NewsViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    article: Article,
    viewModel: NewsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val bookmarks by viewModel.bookmarks.collectAsState()
    val isBookmarked = remember(bookmarks) { bookmarks.any { it.id == article.id } }

    val animatedAlpha = animateFloatAsState(if (isBookmarked) 1f else 0.6f, label = "")
    val animatedTint = animateColorAsState(
        if (isBookmarked) MaterialTheme.colorScheme.primary else Color.Gray, label = ""
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Article Detail") },
                actions = {
                    IconButton(onClick = {
                        scope.launch { viewModel.toggleBookmark(article) }
                    }) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                            contentDescription = null,
                            tint = animatedTint.value,
                            modifier = Modifier.alpha(animatedAlpha.value)
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
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // 🖼️ Hero image with gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                AsyncImage(
                    model = article.imageUrl ?: "https://picsum.photos/600/400?random=${article.id}",
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)),
                                startY = 100f
                            )
                        )
                )
                Text(
                    text = article.title,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                )
            }

            // 📰 Article details
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = article.source ?: "Unknown Source",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = article.description ?: "No description available",
                    style = MaterialTheme.typography.bodyLarge,
                    lineHeight = 22.sp,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                Button(
                    onClick = {
                        article.contentUrl?.let {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                            context.startActivity(intent)
                        }
                    },
                    enabled = !article.contentUrl.isNullOrEmpty(),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                ) {
                    Text("Open Full Article", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }

                Text(
                    text = "Thank you for reading ✨",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
