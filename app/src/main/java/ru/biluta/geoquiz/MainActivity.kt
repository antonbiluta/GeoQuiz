package ru.biluta.geoquiz

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider

private const val KEY_INDEX = "index"
private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var btnTrue : Button
    private lateinit var btnFalse: Button
    private lateinit var nextButton: Button
    private lateinit var prevButton: Button
    private lateinit var questionTextView: TextView
    private lateinit var cheatButton: Button

    private val quizViewModel: QuizViewModel by lazy {
        val provider = ViewModelProvider(this)
        provider.get(QuizViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        quizViewModel.currentIndex = savedInstanceState?.getInt(........)

        btnTrue = findViewById(R.id.true_button)
        btnFalse = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        questionTextView = findViewById(R.id.question)
        cheatButton = findViewById(R.id.cheat_button)

        var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    quizViewModel.isCheater = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
                }
        }

        cheatButton.setOnClickListener {
            val intent = CheatActivity.newIntent(this)
            resultLauncher.launch(intent)
        }

        btnTrue.setOnClickListener {
            checkAnswer(true)
        }

        btnFalse.setOnClickListener {
            checkAnswer(false)
        }

        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            quizViewModel.isCheater = false
            updateQuestion()
        }
        updateQuestion()
    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgement_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "OnSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currectIndex)
    }
}