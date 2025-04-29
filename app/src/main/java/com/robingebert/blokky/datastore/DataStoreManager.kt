package com.robingebert.blokky.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDateTime


/*
private val Context.contentBlockersDataStore: DataStore<ContentBlockers> by dataStore(
    fileName = "content_blockers.pb",
    serializer = ContentBlockersSerializer
)

class DataStoreManager(
    context: Context,
    scope: CoroutineScope
) {
    private val ds = context.contentBlockersDataStore

    // ---- StateFlows der kompletten Blocker-Objekte ----
    val instagramBlockerState: StateFlow<InstagramBlocker> = ds.data
        .map { it.instagramBlocker }
        .stateIn(scope, SharingStarted.WhileSubscribed(5_000), InstagramBlocker.getDefaultInstance())

    val youtubeBlockerState: StateFlow<YoutubeBlocker> = ds.data
        .map { it.youtubeBlocker }
        .stateIn(scope, SharingStarted.WhileSubscribed(5_000), YoutubeBlocker.getDefaultInstance())

    val tiktokBlockerState: StateFlow<TikTokBlocker> = ds.data
        .map { it.tiktokBlocker }
        .stateIn(scope, SharingStarted.WhileSubscribed(5_000), TikTokBlocker.getDefaultInstance())

    // ---- StateFlows der einzelnen Felder (optional, weil ViewModel ja auch mappen kann) ----
    val instagramBlocked: StateFlow<Boolean> = instagramBlockerState
        .map { it.blocked }
        .stateIn(scope, SharingStarted.WhileSubscribed(5_000), false)

    val instagramBlockedStart: StateFlow<LocalDateTime> = instagramBlockerState
        .map { it.blockedStart.toLocalDateTime() }
        .stateIn(scope, SharingStarted.WhileSubscribed(5_000), LocalDateTime.MIN)

    val instagramBlockedEnd: StateFlow<LocalDateTime> = instagramBlockerState
        .map { it.blockedEnd.toLocalDateTime() }
        .stateIn(scope, SharingStarted.WhileSubscribed(5_000), LocalDateTime.MIN)

    val instagramBlockedTimer: StateFlow<Int> = instagramBlockerState
        .map { it.blockedTimer }
        .stateIn(scope, SharingStarted.WhileSubscribed(5_000), 0)

    // (analog auch für YouTube und TikTok, falls du sie auf Feldebene brauchst)

    // ---- Update-Methoden ----
    suspend fun updateInstagram(
        blocked: Boolean,
        start: LocalDateTime,
        end: LocalDateTime,
        timer: Int
    ) {
        ds.updateData { current ->
            current.toBuilder()
                .setInstagramBlocker(
                    InstagramBlocker.newBuilder()
                        .setBlocked(blocked)
                        .setBlockedStart(start.toTimestamp())
                        .setBlockedEnd(end.toTimestamp())
                        .setBlockedTimer(timer)
                )
                .build()
        }
    }

    // ... updateYoutube(), updateTikTok() analog wie vorher ...
}*/