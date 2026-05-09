package com.example.ankib

object SRS {
    const val MAX_BOX = 5

    /**
     * Leitner Promotion: When a word is learned, it moves to the next box.
     */
    fun learned(word: Word): Int {
        return if (word.box < MAX_BOX) word.box + 1 else MAX_BOX
    }

    /**
     * Leitner Demotion: If forgotten, it goes back to Box 0 for immediate review.
     */
    fun forgotten(word: Word): Int {
        return 0
    }

    /**
     * Comparator for the Leitner system.
     * Prioritizes lower boxes (0, 1, 2...) as they need more frequent review.
     */
    fun cmp(a: Word, b: Word): Int {
        return a.box.compareTo(b.box)
    }
}
