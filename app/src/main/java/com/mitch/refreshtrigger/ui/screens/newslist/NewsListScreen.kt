package com.mitch.refreshtrigger.ui.screens.newslist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun NewsListRoute(viewModel: NewsListViewModel, modifier: Modifier = Modifier) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NewsListScreen(
        uiState = uiState,
        onAddNews = viewModel::addNews,
        onRefreshNews = viewModel::refreshNews,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewsListScreen(
    uiState: NewsListScreenUiState,
    onAddNews: () -> Unit,
    onRefreshNews: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        NewsListScreenUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is NewsListScreenUiState.Success -> {
            val state = rememberPullToRefreshState()
            var isRefreshing by rememberSaveable { mutableStateOf(false) }
            val coroutineScope = rememberCoroutineScope()
            val onRefresh: () -> Unit = {
                isRefreshing = true
                coroutineScope.launch {
                    onRefreshNews()
                    delay(500)
                    isRefreshing = false
                }
            }

            PullToRefreshBox(
                state = state,
                isRefreshing = isRefreshing,
                onRefresh = onRefresh,
                modifier = modifier
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    item {
                        Column {
                            Text(
                                text = "Latest news",
                                modifier = Modifier.semantics { heading() },
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                            )
                            for ((index, news) in uiState.news.withIndex()) {
                                key(index) {
                                    Text(text = "#${index + 1} ${news.title}")
                                }
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    item {
                        Button(onClick = onAddNews) {
                            Text(text = "Add news")
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                    item {
                        Button(onClick = onRefresh) {
                            Text(text = "Refresh news")
                        }
                    }
                }
            }
        }
    }
}
