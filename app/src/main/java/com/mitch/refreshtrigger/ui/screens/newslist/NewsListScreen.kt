package com.mitch.refreshtrigger.ui.screens.newslist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .then(modifier),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column {
                    Text(
                        text = "Latest news",
                        modifier = Modifier.semantics { heading() },
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    LazyColumn {
                        itemsIndexed(uiState.news) { index, news ->
                            Text(text = "#${index + 1} ${news.title}")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onAddNews) {
                    Text(text = "Add news")
                }
                Button(onClick = onRefreshNews) {
                    Text(text = "Refresh news")
                }
            }
        }
    }

}
