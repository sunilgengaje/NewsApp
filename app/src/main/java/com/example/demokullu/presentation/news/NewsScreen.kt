package com.example.demokullu.presentation.news

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.demokullu.domain.model.Article
import com.example.demokullu.presentation.news.components.ArticleCard
import kotlinx.coroutines.flow.Flow

@Composable
fun NewsScreen(
    onArticleClick: (Article) -> Unit,
    viewModel: NewsViewModel = hiltViewModel()
) {
    var search by remember { mutableStateOf(TextFieldValue("")) }

    // bookmarks from viewModel (StateFlow -> Compose State)
    val bookmarks by viewModel.bookmarks.collectAsState()

    // compute set of bookmarked ids for quick lookup
    val bookmarkedIds = remember(bookmarks) { bookmarks.map { it.id }.toSet() }

    val pagingFlow: Flow<PagingData<Article>> = remember(search.text) {
        if (search.text.isBlank()) viewModel.news else viewModel.searchResults
    }

    LaunchedEffect(search.text) { viewModel.setQuery(search.text) }

    val pagingItems = pagingFlow.collectAsLazyPagingItems()

    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            label = { Text("Search articles...") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        when {
            pagingItems.loadState.refresh is androidx.paging.LoadState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            pagingItems.itemCount == 0 &&
                    pagingItems.loadState.refresh is androidx.paging.LoadState.NotLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No results found")
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    // use index-based items to work with LazyPagingItems
                    items(
                        count = pagingItems.itemCount,
                        key = pagingItems.itemKey { it.id }
                    ) { index ->
                        val article = pagingItems[index]
                        if (article != null) {
                            val isBookmarked = bookmarkedIds.contains(article.id)
                            ArticleCard(
                                article = article,
                                isBookmarked = isBookmarked,
                                onClick = { onArticleClick(article) },
                                onBookmarkClick = { viewModel.toggleBookmark(article) }
                            )
                        }
                    }

                    if (pagingItems.loadState.append is androidx.paging.LoadState.Loading) {
                        item {
                            Box(Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
}
