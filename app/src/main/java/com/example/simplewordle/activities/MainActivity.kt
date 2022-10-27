package com.example.simplewordle.activities

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
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

        gameLoop(false)


    }

    /**
     * Calculates the seed used in random number generator based on calendar date.
     * This ensures that every user has the same word of the day.
     * @return seed which is a sum of the year, month and day from the device calendar
     */
    private fun getTodaySeed(): Int {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        return year + month + day
    }

    /**
     * Generates random seed for getRandomWordIndex if user re-rolls his daily word.
     * Seed values ranges from 0 to until param value.
     * @param until maximum possible random number.
     * @return random integer from 0 to until param value.
     */
    private fun getRandomSeed(until: Int): Int {
        return Random.nextInt(from = 0, until)
    }

    /**
     * Generates random index for word list using provided seed.
     * @param seed seed used for random number generation. Ensures consisted random number with the same seed.
     * @param until until maximum possible random number.
     * @return random index used for getting word from word list.
     */
    private fun getRandomWordIndex(seed: Int, until: Int): Int {
        return Random(seed).nextInt(from = 0, until)
    }

    /**
     * Function grabs assets json file with words.
     * @param jsonFileName name of the assets file with words in json format
     * @return serialized json value
     */
    private fun getWordList(jsonFileName: String): List<String>? {
        val wordList: List<String>?
        try {
            val inputStream: InputStream = assets.open(jsonFileName)
            val jsonWords = inputStream.bufferedReader().use { it.readText() }
            wordList = Gson().fromJson(jsonWords, WordleWord::class.java).words

        } catch (ex: Exception) {
            ex.printStackTrace()
            return null
        }
        return wordList
    }

    /**
     * Function gets user input word from given row.
     * @return word provided by the user
     */
    private fun getUserInputWord(correctWord: String, currentRow: LinearLayout): String {

        var userWord = ""

        val inputRow = currentRow
        //val childrenCount = inputRow.childCount
        for (i in 0 until inputRow.childCount) {
            val editText = inputRow.getChildAt(i) as EditText
            val currentLetter = editText.text.toString().lowercase()

            if (currentLetter.isNotBlank()) {
                if (correctWord[i].toString() == currentLetter) {
                    editText.background =
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.letter_correct_rightplace,
                            null
                        )

                } else if (correctWord.contains(currentLetter)) {
                    editText.background =
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.letter_correct_wrongplace,
                            null
                        )

                } else {
                    editText.background =
                        ResourcesCompat.getDrawable(resources, R.drawable.letter_wrong, null)
                }
                editText.isEnabled = false
                userWord += currentLetter
            }
        }
        return userWord
    }

    /**
     * Function to check, if word provided by the user matches correct drawn word.
     * @param userWord word provided by the user
     * @param correctWord correctWord provided from word list
     * @return boolean value if userWord matches correct word. True if so, else false
     */
    private fun checkWordCorrectness(userWord: String, correctWord: String): Boolean {
        return (userWord == correctWord)
    }

    /**
     *Function to check if word provided by the user is in the word list. If word is not in the word list user doesn't loose his attempt and can retype the word.
     * @param userWord word provided by the user
     * @param wordList word list with all words available in the game.
     * @return boolean value if userWord is in the word list. True if so, else false.
     */
    private fun checkIfInWordList(userWord: String, wordList: List<String>): Boolean {
        return (userWord == wordList.find { word -> word == userWord })
    }

    private fun gameLoop(gameStatus: Boolean) {

        //        var currentRow = 0
//        var userWord: String? = null
//        while (gameStatus) {
////            userWord = getUserInputWord()
//
//
//        }

        val wordsJsonFileName = "words.json"
        val wordsList = getWordList(wordsJsonFileName)
        val inputLayout = findViewById<LinearLayout>(R.id.inputLayout)

        val lastWordIdx = wordsList!!.size
        val seedThisDay = getTodaySeed()
        val todayIdx = getRandomWordIndex(seedThisDay, lastWordIdx)
        val todayWord = wordsList[todayIdx]
        Log.d("today word", todayWord)

        var currentRow = 0


        val checkBtn = findViewById<Button>(R.id.check_btn)
        checkBtn.setOnClickListener {

            val nowRow = inputLayout.getChildAt(currentRow) as LinearLayout

            val userInputWord = getUserInputWord(todayWord, nowRow)
            val isInWordList = checkIfInWordList(userInputWord, wordsList)


            if (isInWordList) {
                val win = checkWordCorrectness(userInputWord, todayWord)
                if (win) {
                    val toast =
                        Toast.makeText(this, "Congratulations", Toast.LENGTH_LONG)
                    toast.show()
                } else {
                    val toast = Toast.makeText(this, "Not this time", Toast.LENGTH_LONG)
                    toast.show()
                }
                currentRow += 1
            } else {
                val toast = Toast.makeText(this, "Not in word list", Toast.LENGTH_LONG)
                toast.show()
            }

        }


        val reRollBtn = findViewById<Button>(R.id.re_roll_btn)
        reRollBtn.setOnClickListener {
            val randomWordIdx = getRandomWordIndex(getRandomSeed(todayIdx), todayIdx)
            Log.d("random word", wordsList[randomWordIdx])
        }

    }
}