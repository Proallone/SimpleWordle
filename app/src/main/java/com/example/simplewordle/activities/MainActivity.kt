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
import com.example.simplewordle.classes.GuessingWord
import com.example.simplewordle.classes.WordleWordList
import com.google.gson.Gson
import java.io.InputStream


class MainActivity : AppCompatActivity() {
    private var currentRow: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gameLoop()
    }

    private fun changeCurrentRow(inputLayout: LinearLayout) {
        Log.d("changeRow", "Change current row from ${currentRow} to ${currentRow+1}")
        //Enable current row
        val currentRow = inputLayout.getChildAt(this.currentRow) as LinearLayout
        for (i in 0 until currentRow.childCount) {
            val input = currentRow.getChildAt(i) as EditText
            input.isEnabled = true
        }
        //Disable previous row
        if (this.currentRow !== 0) {
            val previousRow = inputLayout.getChildAt(this.currentRow - 1) as LinearLayout
            for (i in 0 until currentRow.childCount) {
                val input = previousRow.getChildAt(i) as EditText
                input.isEnabled = false
            }
        }
    }


    /**
     * Function grabs assets json file with words.
     * @param jsonFileName name of the assets file with words in json format
     * @return serialized json value
     */
    private fun getWordList(jsonFileName: String): List<String>? {
        val wordList: List<String>?
        try {
//            throw IllegalArgumentException(jsonFileName)
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
            } else {
                val toast =
                    Toast.makeText(this, "DSADSDASD", Toast.LENGTH_LONG)
                toast.show()
            }
        }
        return userWord
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

        val checkBtn = findViewById<Button>(R.id.check_btn)
        checkBtn.setOnClickListener {
            checkUserInput(inputLayout, wordsList)
            changeCurrentRow(inputLayout)
        }

        val reRollBtn = findViewById<Button>(R.id.re_roll_btn)
        reRollBtn.setOnClickListener {
            clearInputs()
            GuessingWord.setRandomWord(wordsList)
        }

    }

    private fun clearInputs() {
        Log.d("clear", "Clearing inputs")
        val layout = findViewById<LinearLayout>(R.id.inputLayout)
        this.currentRow = 0
        for (i in 0 until layout.childCount) {
            val inputRow = layout.getChildAt(i) as LinearLayout
            for (j in 0 until inputRow.childCount) {
                val input = inputRow.getChildAt(j) as EditText
                input.setText("")
                input.background =
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.rounded,
                        null
                    )
                input.isEnabled = true
            }
        }
    }

    private fun checkUserInput(inputLayout: LinearLayout, wordsList: List<String>) {
        val currentRow = inputLayout.getChildAt(this.currentRow) as LinearLayout
        val userInputWord = getUserInputWord(currentRow)
        val isInWordList = checkIfInWordList(wordsList)

        if (isInWordList) {
            GuessingWord.checkCorrectness(userInputWord)
            if (GuessingWord.getGuessed()) {
                val toast =
                    Toast.makeText(
                        this,
                        this.getString(R.string.correct_word_toast),
                        Toast.LENGTH_LONG
                    )
                toast.show()
            } else {
                Toast.makeText(
                    this,
                    this.getString(R.string.incorrect_word_toast),
                    Toast.LENGTH_LONG
                ).show()
            }
            this.currentRow += 1
        } else {
            Toast.makeText(this, this.getString(R.string.word_not_in_list), Toast.LENGTH_LONG)
                .show()
        }
    }

}