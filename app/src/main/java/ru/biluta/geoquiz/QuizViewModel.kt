package ru.biluta.geoquiz


private const val TAG = "QuizView"

class QuizViewModel {

    private val questionBank = listOf(
        Question("Ext1", answer = true),
        Question("Ext2", answer = true),
        Question("Ext3", answer = false),
        Question("Ext4", answer = false),
        Question("Ext5", answer = true),
    )

    var currentIndex = 0
    var isCheater = false

    val currentQuestionAnswer: Boolean
    get() = questionBank[currentIndex].answer
    val currentQuestionText: Int
    get() = questionBank[currentIndex].textResId

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrev() {
        currentIndex = (questionBank.size + currentIndex - 1) % questionBank.size
    }

}