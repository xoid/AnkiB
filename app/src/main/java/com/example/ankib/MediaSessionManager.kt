package com.example.ankib

import android.content.Context
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat

class MediaSessionManager(
    private val context: Context,
    private val headSetButton: HeadSetButton
) {
    private var mediaSession: MediaSessionCompat? = null

    fun init() {
        mediaSession = MediaSessionCompat(context, "AnkiB").apply {
            setCallback(object : MediaSessionCompat.Callback() {
                override fun onPlay() {
                    headSetButton.onLongPress()
                }

                override fun onPause() {
                    headSetButton.onLongPress()
                }

                override fun onSkipToNext() {
                    headSetButton.onShortPress()
                }
            })
            setPlaybackState(
                PlaybackStateCompat.Builder()
                    .setActions(
                        PlaybackStateCompat.ACTION_PLAY or
                                PlaybackStateCompat.ACTION_PAUSE or
                                PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                    )
                    .build()
            )
            isActive = true
        }
    }

    fun release() {
        mediaSession?.release()
    }
}
