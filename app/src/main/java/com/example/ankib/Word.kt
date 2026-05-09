package com.example.ankib

class Word(val base: String) {
    var box: Int = 0
    val files: MutableList<AFile> = mutableListOf()

    fun play(audioPlayer: AudioPlayer, config: Config) {
        // Afile sorting is alfabetical (but reverse if Config.sortReverse is true)
        val sortedFiles = files.sortedBy { it.f.suffix }.let {
            if (config.sortReverse) it.reversed() else it
        }
        
        sortedFiles.forEachIndexed { index, aFile ->
            if (audioPlayer.isPaused) return@forEachIndexed
            
            // Apply Config.speed to every aFile.play
            aFile.play(audioPlayer)
            
            // Config.pause used only for pauses (user wants time to remember)
            // If it's not the last file, we might want to pause
            if (index < sortedFiles.size - 1) {
                Thread.sleep((config.pause * 1000).toLong())
            }
        }
    }
}
