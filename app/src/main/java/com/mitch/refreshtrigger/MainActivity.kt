package com.mitch.refreshtrigger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mitch.refreshtrigger.data.notes.NewsRepository
import com.mitch.refreshtrigger.data.notes.remote.NewsRemoteDataSource
import com.mitch.refreshtrigger.ui.screens.newslist.NewsListRoute
import com.mitch.refreshtrigger.ui.screens.newslist.NewsListViewModel
import com.mitch.refreshtrigger.ui.theme.RefreshTriggerTheme
import com.mitch.refreshtrigger.util.viewModelProviderFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val remoteNewsDataSource by lazy { NewsRemoteDataSource() }
        val newsRepository by lazy { NewsRepository(newsRemoteDataSource = remoteNewsDataSource) }
        setContent {
            RefreshTriggerTheme {
                Scaffold { innerPadding ->
                    NewsListRoute(
                        viewModel = viewModel(
                            factory = viewModelProviderFactory {
                                NewsListViewModel(newsRepository = newsRepository)
                            },
                        ),
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
