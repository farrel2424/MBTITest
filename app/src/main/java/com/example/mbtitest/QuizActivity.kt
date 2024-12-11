package com.example.mbtitest

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mbtitest.databinding.ActivityQuizBinding
import com.example.mbtitest.databinding.ScoreDialogBinding
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore


class QuizActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        var questionModelList: List<QuestionModel> = listOf()
    }

    lateinit var binding: ActivityQuizBinding
    var currentQuestionIndex = 0
    var selectedAnswer = ""
    var extraversionScore = 0
    var sensingScore = 0
    var thinkingScore = 0
    var judgingScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            btn0.setOnClickListener(this@QuizActivity)
            btn1.setOnClickListener(this@QuizActivity)
            btn2.setOnClickListener(this@QuizActivity)
            btn3.setOnClickListener(this@QuizActivity)
            nextBtn.setOnClickListener(this@QuizActivity)
        }
        loadQuestions()
    }

    private fun loadQuestions() {
        selectedAnswer = ""
        if (currentQuestionIndex == questionModelList.size) {
            return
        }

        binding.apply {
            questionIndicatorTextview.text = "Question ${currentQuestionIndex + 1}/ ${questionModelList.size}"
            questionProgressIndicator.progress =
                (currentQuestionIndex.toFloat() / questionModelList.size.toFloat() * 100).toInt()
            questionTextview.text = questionModelList[currentQuestionIndex].question
            btn0.text = questionModelList[currentQuestionIndex].options[0]
            btn1.text = questionModelList[currentQuestionIndex].options[1]
            btn2.text = questionModelList[currentQuestionIndex].options[2]
            btn3.text = questionModelList[currentQuestionIndex].options[3]
        }
    }

    override fun onClick(view: View?) {
        binding.apply {
            btn0.setBackgroundColor(getColor(R.color.gray))
            btn1.setBackgroundColor(getColor(R.color.gray))
            btn2.setBackgroundColor(getColor(R.color.gray))
            btn3.setBackgroundColor(getColor(R.color.gray))
        }

        val clickedBtn = view as Button
        if (clickedBtn.id == R.id.nextBtn) {
            val selectedAnswerIndex = when (selectedAnswer) {
                questionModelList[currentQuestionIndex].options[0] -> 0
                questionModelList[currentQuestionIndex].options[1] -> 1
                questionModelList[currentQuestionIndex].options[2] -> 2
                questionModelList[currentQuestionIndex].options[3] -> 3
                else -> -1
            }

            if (selectedAnswerIndex != -1) {
                val points = questionModelList[currentQuestionIndex].points[selectedAnswerIndex]
                when (currentQuestionIndex % 4) {
                    0 -> extraversionScore += points
                    1 -> sensingScore += points
                    2 -> thinkingScore += points
                    3 -> judgingScore += points
                }
            }

            Log.i("Extraversion Score", extraversionScore.toString())
            Log.i("Sensing Score", sensingScore.toString())
            Log.i("Thinking Score", thinkingScore.toString())
            Log.i("Judging Score", judgingScore.toString())

            if (currentQuestionIndex == questionModelList.size - 1) {
                showResultDialog()
            } else {
                currentQuestionIndex++
                loadQuestions()
            }
        } else {
            selectedAnswer = clickedBtn.text.toString()
            clickedBtn.setBackgroundColor(getColor(R.color.maroon))
        }
    }

    private fun showResultDialog() {
        val dialogBinding = ScoreDialogBinding.inflate(layoutInflater)
        dialogBinding.apply {
            val mbtiType = determineMBTIType(extraversionScore, sensingScore, thinkingScore, judgingScore)
            scoreTitle.text = "Your MBTI Type: $mbtiType"
            scoreTitle.setTextColor(Color.WHITE) // Set text color to white

            saveResultToFirebase(mbtiType)

            when (mbtiType) {
                "INTJ" -> characterImageView.setImageResource(R.drawable.intj_image)
                "INTP" -> characterImageView.setImageResource(R.drawable.intp_image)
                "ENTJ" -> characterImageView.setImageResource(R.drawable.entj_image)
                "ENTP" -> characterImageView.setImageResource(R.drawable.entp_image)
                "INFJ" -> characterImageView.setImageResource(R.drawable.infj_image)
                "INFP" -> characterImageView.setImageResource(R.drawable.infp_image)
                "ENFJ" -> characterImageView.setImageResource(R.drawable.enfj_image)
                "ENFP" -> characterImageView.setImageResource(R.drawable.enfp_image)
                "ISTJ" -> characterImageView.setImageResource(R.drawable.istj_image)
                "ISFJ" -> characterImageView.setImageResource(R.drawable.isfj_image)
                "ESTJ" -> characterImageView.setImageResource(R.drawable.estj_image)
                "ESFJ" -> characterImageView.setImageResource(R.drawable.esfj_image)
                "ISTP" -> characterImageView.setImageResource(R.drawable.istp_image)
                "ISFP" -> characterImageView.setImageResource(R.drawable.isfp_image)
                "ESTP" -> characterImageView.setImageResource(R.drawable.estp_image)
                "ESFP" -> characterImageView.setImageResource(R.drawable.esfp_image)
                else -> {
                    scoreTitle.text = "Error: Invalid MBTI Type Detected. Please retake the questionnaire."
                    scoreTitle.setTextColor(Color.RED)
                    finishBtn.text = "Retake"
                    finishBtn.setOnClickListener {
                        val retryIntent = Intent(this@QuizActivity, QuizActivity::class.java)
                        retryIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(retryIntent)
                        finish()
                    }
                    return
                }
            }

            saveResultToFirebase(mbtiType)

            finishBtn.text = "Finish"
            finishBtn.setOnClickListener {
                val intent = Intent(this@QuizActivity, MBTIActivity::class.java)
                intent.putExtra("extraversionScore", extraversionScore)
                intent.putExtra("sensingScore", sensingScore)
                intent.putExtra("thinkingScore", thinkingScore)
                intent.putExtra("judgingScore", judgingScore)
                startActivity(intent)
                finish()
            }
        }

        AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setCancelable(false)
            .show()
    }



    private fun determineMBTIType(extraversion: Int, sensing: Int, thinking: Int, judging: Int): String {
        val eOrI = if (extraversion > 50) "E" else "I"
        val sOrN = if (sensing > 50) "S" else "N"
        val tOrF = if (thinking > 50) "T" else "F"
        val jOrP = if (judging > 50) "J" else "P"
        return "$eOrI$sOrN$tOrF$jOrP"
    }
    private fun saveResultToFirebase(mbtiType: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val email = FirebaseAuth.getInstance().currentUser?.email


        if (userId != null && email != null) {
            val resultData = mapOf(
                "email" to email,
                "mbtiType" to mbtiType
            )


            val firestoreDb = FirebaseFirestore.getInstance()
            firestoreDb.collection("users").document(userId).set(resultData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Result saved to Firestore!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to save result: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "User is not authenticated!", Toast.LENGTH_SHORT).show()
        }
    }

}
