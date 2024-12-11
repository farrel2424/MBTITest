package com.example.mbtitest

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mbtitest.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var questionModelList: MutableList<QuestionModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getDataFromFirebase()

        binding.startBtn.setOnClickListener {
            if (questionModelList.isNotEmpty()) {
                val intent = Intent(this, QuizActivity::class.java)
                QuizActivity.questionModelList = questionModelList
                startActivity(intent)
            } else {
                Toast.makeText(this, "No questions available!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getDataFromFirebase() {
        questionModelList = mutableListOf()

        questionModelList.add(
            QuestionModel(
                "When you have free time, how do you prefer to spend it?",
                listOf(
                    "Being around a group of friends",
                    "Spending time alone to recharge",
                    "Having deep conversations with one person",
                    "Participating in a social event"
                ),
                listOf(20, 40, 30, 10)
            )
        )

        questionModelList.add(
            QuestionModel(
                "How do you prefer to process information?",
                listOf(
                    "Focusing on facts and details",
                    "Considering the bigger picture and possibilities",
                    "Paying attention to immediate experiences",
                    "Exploring abstract ideas and concepts"
                ),
                listOf(30, 40, 20, 10)
            )
        )

        questionModelList.add(
            QuestionModel(
                "What influences your decision-making process the most?",
                listOf(
                    "Logical analysis and objective reasoning",
                    "Personal values and how it affects others",
                    "Weighing the pros and cons carefully",
                    "Following your emotional response"
                ),
                listOf(40, 30, 20, 10)
            )
        )

        questionModelList.add(
            QuestionModel(
                "How do you prefer to plan your day?",
                listOf(
                    "I like to have a detailed schedule",
                    "I prefer to be spontaneous and flexible",
                    "A balance between planning and spontaneity",
                    "I go with the flow without any strict plans"
                ),
                listOf(40, 20, 30, 10)
            )
        )

        questionModelList.add(
            QuestionModel(
                "In a social gathering, you are more likely to:",
                listOf(
                    "Engage with as many people as possible",
                    "Stick to a few close friends or familiar faces",
                    "Listen and observe before participating",
                    "Look for quiet spaces to relax"
                ),
                listOf(30, 40, 20, 10)
            )
        )

        questionModelList.add(
            QuestionModel(
                "When learning something new, you prefer:",
                listOf(
                    "Practical, hands-on experience",
                    "Understanding the theory and underlying concepts",
                    "Detailed step-by-step instructions",
                    "Exploring the possibilities and making connections"
                ),
                listOf(30, 40, 20, 10)
            )
        )

        questionModelList.add(
            QuestionModel(
                "How do you usually approach conflicts?",
                listOf(
                    "By analyzing the situation logically",
                    "By considering everyone’s feelings",
                    "By trying to be fair and objective",
                    "By addressing the emotional aspects first"
                ),
                listOf(40, 30, 20, 10)
            )
        )

        questionModelList.add(
            QuestionModel(
                "When working on a project, you prefer to:",
                listOf(
                    "Have a clear structure and deadlines",
                    "Adapt and make changes as you go",
                    "Plan thoroughly before starting",
                    "Jump in and adjust along the way"
                ),
                listOf(40, 20, 30, 10)
            )
        )

        questionModelList.add(
            QuestionModel(
                "How do you feel after socializing for a long period?",
                listOf(
                    "Energized and excited",
                    "Drained and in need of alone time",
                    "Content but ready for some quiet time",
                    "Exhausted and needing to recharge alone"
                ),
                listOf(30, 40, 20, 10)
            )
        )

        questionModelList.add(
            QuestionModel(
                "When making a decision, you rely more on:",
                listOf(
                    "Concrete evidence and facts",
                    "Gut feelings and intuition",
                    "Analyzing the practical details",
                    "Imagining the future possibilities"
                ),
                listOf(30, 40, 20, 10)
            )
        )
    }
}


