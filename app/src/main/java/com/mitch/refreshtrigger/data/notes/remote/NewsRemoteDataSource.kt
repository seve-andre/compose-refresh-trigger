package com.mitch.refreshtrigger.data.notes.remote

class NewsRemoteDataSource {

    private var remoteNews: List<NewsApiModel> = listOf(
        NewsApiModel(1, "First news"),
        NewsApiModel(2, "Second news"),
        NewsApiModel(3, "Third news")
    )

    fun fetchLatestNews(): List<NewsApiModel> {
        return remoteNews
    }

    fun postNews(title: String) {
        val lastId = remoteNews.last().id
        remoteNews += NewsApiModel(lastId + 1, title)
    }
}
