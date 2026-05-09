package com.example.ankib

import android.content.Context
import android.media.MediaPlayer
import android.media.PlaybackParams
import android.net.Uri
import java.io.File

class AudioPlayer(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null
    
    var currentFolder: Folder? = null
    var wordIndex: Int = -1
    var isPaused: Boolean = false
    var speed: Float = 1.0f

    fun play(audioPath: String) {
        stop()
        isPaused = false
        mediaPlayer = MediaPlayer().apply {
            setDataSource(context, Uri.fromFile(File(audioPath)))
            prepare()
            // AudioPlayer should use Config.speed:float starting with every Afile.play
            val params = PlaybackParams()
            params.speed = speed
            playbackParams = params
            start()
        }
    }

    fun pause() {
        isPaused = true
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
            }
        }
    }

    fun resume() {
        isPaused = false
        mediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
            }
        }
    }

    fun stop() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }
}
