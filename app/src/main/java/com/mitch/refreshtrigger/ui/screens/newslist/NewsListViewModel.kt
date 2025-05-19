package com.mitch.refreshtrigger.ui.screens.newslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mitch.refreshtrigger.data.notes.NewsRepository
import com.mitch.refreshtrigger.domain.News
import com.mitch.refreshtrigger.util.RefreshTrigger
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class NewsListViewModel(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val newsRefreshTrigger = RefreshTrigger()
    val uiState: StateFlow<NewsListScreenUiState> = newsRefreshTrigger.receiveAsFlow()
        .onStart { newsRefreshTrigger.refresh() }
        .transformLatest {
            val news = newsRepository.getLatestNews(true)
            emit(NewsListScreenUiState.Success(news = news))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NewsListScreenUiState.Loading
        )

    fun addNews() {
        newsRepository.addNews(News(title = "New news"))
    }

    fun refreshNews() {
        viewModelScope.launch {
            newsRefreshTrigger.refresh()
        }
    }
}

sealed interface NewsListScreenUiState {
    data object Loading : NewsListScreenUiState
    data class Success(val news: List<News>) : NewsListScreenUiState
}
