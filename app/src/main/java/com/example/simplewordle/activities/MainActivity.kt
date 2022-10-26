package com.example.simplewordle.activities

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.simplewordle.R
import com.example.simplewordle.classes.WordleWord
import com.google.gson.Gson
import java.io.InputStream
import java.util.*
import kotlin.random.Random


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val wordsJsonFileName = "words.json"
        val wordsJson = getJsonWordList(wordsJsonFileName)

        val wordsListArray = getWordList(wordsJson.toString())

        val lastWordIdx = wordsListArray.size
        val seedThisDay = getTodaySeed()
        val todayIdx = getRandomWordIndex(seedThisDay, lastWordIdx)
        val todayWord = wordsListArray[todayIdx]

        Log.d("today word", todayWord)

        val reRollBtn = findViewById<Button>(R.id.re_roll_btn)
        reRollBtn.setOnClickListener {
            val randomWordIdx= getRandomWordIndex(getRandomSeed(todayIdx), todayIdx)
            Log.d("random word", wordsListArray[randomWordIdx])
        }

        val checkBtn = findViewById<Button>(R.id.check_btn)
        checkBtn.setOnClickListener {
            var win = false
            val userInputWord = getUserInputWord()
            val isInWordList = checkIfInWordList(userInputWord,wordsListArray)
            Log.d("Isinlist", isInWordList.toString())
            if(isInWordList) {
                win = checkWordCorrectness(userInputWord, todayWord)
            }
            Log.d("GameOver", win.toString())
        }

    }

    private fun getTodaySeed(): Int {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        return year + month + day
    }

    private fun getRandomSeed(until: Int): Int {
        return Random.nextInt(from=0, until)
    }

    private fun getRandomWordIndex(seed: Int, until: Int): Int {
        return Random(seed).nextInt(from = 0, until)
    }

    private fun getWordList(wordsJson: String): List<String> {
        val gson = Gson()
        val wordsObj = gson.fromJson(wordsJson, WordleWord::class.java)
        return wordsObj.words
    }

    private fun getJsonWordList(jsonFileName: String): String? {
        var json: String? = null
        try {
            val inputStream: InputStream = assets.open(jsonFileName)
            json = inputStream.bufferedReader().use { it.readText() }
        } catch (ex: Exception) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    private fun getUserInputWord(): String {

        val et00=findViewById<EditText>(R.id.userInput00)
        val et01=findViewById<EditText>(R.id.userInput01)
        val et02=findViewById<EditText>(R.id.userInput02)
        val et03=findViewById<EditText>(R.id.userInput03)
        val et04=findViewById<EditText>(R.id.userInput04)

        val char00 = et00.text.toString().lowercase()
        val char01 = et01.text.toString().lowercase()
        val char02 = et02.text.toString().lowercase()
        val char03 = et03.text.toString().lowercase()
        val char04 = et04.text.toString().lowercase()

        val userInputWord = char00 + char01 + char02 + char03 + char04
//        val firstLine = findViewById<LinearLayout>(R.id.firstLineLayout)
//
//        val u00 = firstLine.getChildAt(0)

        return userInputWord
    }

    private fun checkWordCorrectness(userWord: String, correctWord: String): Boolean {
        return (userWord == correctWord)
    }

    private fun checkIfInWordList(userWord: String, wordList:List<String>):Boolean {
        return (userWord == wordList.find { word -> word==userWord })
    }
}