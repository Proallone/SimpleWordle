package com.example.simplewordle.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.simplewordle.R
import java.util.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val lastWordIdx = 100
        getRandomWordIndex(lastWordIdx)
    }
    private fun getRandomWordIndex(until: Int): Int {

        val c = Calendar.getInstance()

        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val seed = year+month+day
        val from = 0

        Log.d("date","Current Date and Time is: $year-$month-$day")
        val wordIndex = Random(seed).nextInt(from,until)
        Log.d("rand","Random word index for today is: $wordIndex")
        return wordIndex
    }
    private fun getWordList() {

    }
}