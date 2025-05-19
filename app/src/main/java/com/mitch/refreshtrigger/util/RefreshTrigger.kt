package com.mitch.refreshtrigger.util

import kotlinx.coroutines.channels.Channel

class RefreshTrigger private constructor(private val channel: Channel<Unit>) : Channel<Unit> by channel {
    suspend fun refresh() {
        channel.send(Unit)
    }

    companion object {
        operator fun invoke() = RefreshTrigger(Channel(Channel.CONFLATED))
    }
}
