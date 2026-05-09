package com.example.ankib

class HeadSetButton(
    private val audioPlayer: AudioPlayer,
    private val store: Store
) {
    fun onShortPress() {
        // MainButtonShort: find current playing Word, reduce it's Word.srs rating by SRS.learned, Store.save new rating
        val folder = audioPlayer.currentFolder
        val index = audioPlayer.wordIndex
        if (folder != null && index in folder.words.indices) {
            val word = folder.words[index]
            word.box = SRS.learned(word)
            store.saveSrs(word.base, word.box)
        }
    }

    fun onLongPress() {
        // MainButtonLong: AudioPlayer pause/resume. All application should be stopped.
        if (audioPlayer.isPaused || !audioPlayer.isPlaying()) {
            audioPlayer.resume()
        } else {
            audioPlayer.pause()
        }
    }
}
