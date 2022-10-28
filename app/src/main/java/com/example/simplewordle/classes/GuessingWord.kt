package com.example.simplewordle.classes

import android.util.Log
import java.util.*
import kotlin.random.Random

object GuessingWord {
    private var word: String = String()
    private var guessed: Boolean = false

    fun getWord(): String {
        return this.word
    }

    fun getGuessed(): Boolean {
        return this.guessed
    }

    fun checkCorrectness(userWord: String) {
        if (userWord == this.getWord()){
            this.guessed = true
        }
    }

    private fun setWord(newWord: String) {
        this.word = newWord
    }

    fun setRandomWord(wordList: List<String>) {
        val until = wordList.size
        val randomIdx = Random.nextInt(from = 0, until)
        val newWord = wordList[randomIdx]
        this.setWord(newWord)
        Log.d("RandomWord", this.getWord())
    }

    fun setTodayWord(wordList: List<String>) {
        val until = wordList.size
        val seed = getTodaySeed()
        val todayIdx = Random(seed).nextInt(from = 0, until)
        val todayWord = wordList[todayIdx]
        this.setWord(todayWord)
        Log.d("TodayWord", this.getWord())
    }

    private fun getTodaySeed(): Int {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        return year + month + day
    }
}