package com.example.simplewordle.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.example.simplewordle.R
import com.example.simplewordle.classes.GuessingWord
import com.example.simplewordle.classes.WordleWordList
import com.google.gson.Gson
import java.io.InputStream


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gameLoop()
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
            wordList = Gson().fromJson(jsonWords, WordleWordList::class.java).words

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
    private fun getUserInputWord(currentRow: LinearLayout): String {
        val correctWord = GuessingWord.getWord()

        var userWord = ""

        val inputRow = currentRow
        //val childrenCount = inputRow.childCount
        for (i in 0 until inputRow.childCount) {
            val editText = inputRow.getChildAt(i) as EditText
            val nextEditText = inputRow.getChildAt(i+1) as EditText
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
     * @param userWord word provided by the user.
     * @return boolean value if userWord matches correct word. True if so, else false
     */
    private fun checkWordCorrectness(userWord: String): Boolean {
        val correctWord = GuessingWord.getWord()
        return (userWord == correctWord)
    }

    /**
     *Function to check if word provided by the user is in the word list. If word is not in the word list user doesn't loose his attempt and can retype the word.
     * @param wordList word list with all words available in the game.
     * @return boolean value if userWord is in the word list. True if so, else false.
     */
    private fun checkIfInWordList(wordList: List<String>): Boolean {
        val userWord = GuessingWord.getWord()
        return (userWord == wordList.find { word -> word == userWord })
    }

    private fun gameLoop() {


        val wordsJsonFileName = "words.json"
        val wordsList = getWordList(wordsJsonFileName)!!
        val inputLayout = findViewById<LinearLayout>(R.id.inputLayout)

        GuessingWord.setTodayWord(wordsList)

        var currentRow = 0

        val checkBtn = findViewById<Button>(R.id.check_btn)
        checkBtn.setOnClickListener {

            val nowRow = inputLayout.getChildAt(currentRow) as LinearLayout
            val userInputWord = getUserInputWord(nowRow)
            val isInWordList = checkIfInWordList(wordsList)

            if (isInWordList) {
                val win = checkWordCorrectness(userInputWord)
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
            GuessingWord.setRandomWord(wordsList)
        }

    }

    private fun clearInputs(inputLayout: LinearLayout): Boolean {
//        val rowsNumber = inputLayout.childCount
//        for (i in 0 until rowsNumber) {
//            val currentRow = inputLayout
//        }

        return true
    }
}