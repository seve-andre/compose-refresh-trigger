package com.mitch.refreshtrigger.data.notes

import com.mitch.refreshtrigger.data.notes.remote.NewsRemoteDataSource
import com.mitch.refreshtrigger.domain.News
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class NewsRepository(private val newsRemoteDataSource: NewsRemoteDataSource) {

    private val latestNewsMutex = Mutex()

    private var latestNews: List<News> = emptyList()

    suspend fun getLatestNews(refresh: Boolean = false): List<News> {
        if (refresh || latestNews.isEmpty()) {
            val networkResult = newsRemoteDataSource.fetchLatestNews()
            // Thread-safe write to latestNews
            latestNewsMutex.withLock {
                this.latestNews = networkResult.map { News(title = it.title) }
            }
        }

        return latestNewsMutex.withLock { this.latestNews }
    }

    fun addNews(news: News) {
        newsRemoteDataSource.postNews(title = news.title)
    }
}
