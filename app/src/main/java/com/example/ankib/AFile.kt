package com.example.ankib

class AFile(val f: F) {
    fun play(audioPlayer: AudioPlayer) {
        audioPlayer.play(f.filename)
    }
}
