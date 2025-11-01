package com.example.demokullu.presentation.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.demokullu.domain.model.Article
import com.example.demokullu.domain.usecase.GetBookmarksUseCase
import com.example.demokullu.domain.usecase.GetNewsUseCase
import com.example.demokullu.domain.usecase.SearchNewsUseCase
import com.example.demokullu.domain.usecase.ToggleBookmarkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase,
    private val searchNewsUseCase: SearchNewsUseCase,
    private val getBookmarksUseCase: GetBookmarksUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase
) : ViewModel() {

    /** ✅ Default paged news feed (no search) */
    val news: Flow<PagingData<Article>> = getNewsUseCase()
        .cachedIn(viewModelScope)

    /** ✅ Mutable search query */
    private val _query = MutableStateFlow("")

    /** ✅ Debounced search results (switches automatically) */
    @OptIn(FlowPreview::class)
    val searchResults: Flow<PagingData<Article>> = _query
        .debounce(400) // delay input handling
        .distinctUntilChanged() // skip identical queries
        .flatMapLatest { query ->
            if (query.isBlank()) {
                getNewsUseCase() // return default paged feed
            } else {
                searchNewsUseCase(query)
            }
        }
        .cachedIn(viewModelScope)

    /** ✅ Set new search query from UI */
    fun setQuery(query: String) {
        _query.value = query.trim()
    }

    /** ✅ Bookmarks Flow from Room (cached as StateFlow) */
    val bookmarks: StateFlow<List<Article>> = getBookmarksUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    /** ✅ Toggle bookmark add/remove */
    fun toggleBookmark(article: Article) {
        viewModelScope.launch {
            toggleBookmarkUseCase(article)
        }
    }

    /** ✅ Check bookmark state for given article */
    fun isBookmarked(article: Article): Boolean {
        return bookmarks.value.any { it.id == article.id }
    }
}
