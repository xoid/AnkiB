package com.example.ankib

import java.io.File as JFile

class Folder(val path: String) {
    var words: MutableList<Word> = mutableListOf()

    init {
        scanFiles()
    }

    private fun scanFiles() {
        val dir = JFile(path)
        val jFiles = dir.listFiles { file -> file.isFile } ?: emptyArray()
        
        jFiles.forEach { jFile ->
            val f = F.parse(jFile.absolutePath)
            val aFile = AFile(f)
            
            val existingWord = words.find { it.base == f.base }
            if (existingWord != null) {
                existingWord.files.add(aFile)
            } else {
                val newWord = Word(f.base)
                newWord.files.add(aFile)
                words.add(newWord)
            }
        }
    }

    fun play(audioPlayer: AudioPlayer, store: Store, config: Config) {
        // Store.load words box ratings into this.words
        words.forEach { word ->
            word.box = store.loadSrs(word.base)
        }
        
        // Folder.words should be sorted by this algo (Leitner)
        words.sortWith { a, b -> SRS.cmp(a, b) }
        
        audioPlayer.currentFolder = this
        
        // Word.play this.words one by one
        words.forEachIndexed { index, word ->
            if (audioPlayer.isPaused) return@forEachIndexed
            audioPlayer.wordIndex = index
            word.play(audioPlayer, config)
        }
    }

    fun pause(audioPlayer: AudioPlayer) {
        audioPlayer.pause()
    }

    // For UI display
    val name: String get() = JFile(path).name
}
