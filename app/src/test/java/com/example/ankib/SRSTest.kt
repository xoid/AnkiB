package com.example.ankib

import org.junit.Test
import org.junit.Assert.*

class SRSTest {
    @Test
    fun testLearnedIncrementsBox() {
        val word = Word("test")
        word.box = 0
        word.box = SRS.learned(word)
        assertEquals(1, word.box)
    }

    @Test
    fun testLearnedMaxBox() {
        val word = Word("test")
        word.box = SRS.MAX_BOX
        word.box = SRS.learned(word)
        assertEquals(SRS.MAX_BOX, word.box)
    }

    @Test
    fun testForgottenResetsToZero() {
        val word = Word("test")
        word.box = 3
        word.box = SRS.forgotten(word)
        assertEquals(0, word.box)
    }

    @Test
    fun testCmpPrioritizesLowerBoxes() {
        val word1 = Word("a").apply { box = 1 }
        val word2 = Word("b").apply { box = 0 }
        assertTrue(SRS.cmp(word1, word2) > 0)
        assertTrue(SRS.cmp(word2, word1) < 0)
        assertEquals(0, SRS.cmp(word1, word1))
    }
}
