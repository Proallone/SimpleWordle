package com.example.simplewordle.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
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


        val wordsJsonFileName= "words.json"
        val wordsJson = getJsonWordList(wordsJsonFileName)

        val wordsListArray = getWordList(wordsJson.toString())
        val lastWordIdx = wordsListArray.size

        val generatedIdx = getRandomWordIndex(lastWordIdx)
        val generatedWord = wordsListArray[generatedIdx]

        Log.d("dupa", "todays word is $generatedWord")

        val reRollBtn = findViewById<Button>(R.id.re_roll_btn)
        reRollBtn.setOnClickListener{
            /*
            @TODO
            reroll function
             */
            getRandomWordIndex(lastWordIdx)
        }

    }

    private fun getRandomWordIndex(until: Int): Int {

        val c = Calendar.getInstance()

        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val seed = year+month+day
        val from = 0

        return Random(seed).nextInt(from,until)
    }
    private fun getWordList(wordsJson: String) : List<String>{
        val gson = Gson()
        val wordsObj = gson.fromJson(wordsJson, WordleWord::class.java)
        return wordsObj.words
    }

    private fun getJsonWordList(jsonFileName: String): String? {
        var json: String? = null
        try {
            val  inputStream: InputStream = assets.open(jsonFileName)
            json = inputStream.bufferedReader().use{it.readText()}
        } catch (ex: Exception) {
            ex.printStackTrace()
            return null
        }
        return json
    }
}