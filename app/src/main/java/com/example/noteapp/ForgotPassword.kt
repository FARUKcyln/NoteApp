package com.example.noteapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.example.noteapp.databinding.ActivityForgotPasswordBinding
import com.google.firebase.database.FirebaseDatabase

class ForgotPassword : AppCompatActivity() {

    lateinit var binding: ActivityForgotPasswordBinding
    private var questionAnswer: String = ""
    private var userKey: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.forgotMail.setOnFocusChangeListener { view, b ->
            if (!b) {
                getSecurityQuestion(
                    binding.forgotMail.text.toString(),
                    binding.securityQuestion,
                    binding.questionAnswer
                )
            }
        }

        binding.resetPassword.setOnClickListener {
            if (binding.questionAnswer.text.isNotEmpty() && !binding.forgotMail.isFocused) {
                println(binding.questionAnswer.text.toString())
                println(questionAnswer)
                if (binding.questionAnswer.text.toString() == questionAnswer) {
                    val intent = Intent(this, ResetPassword::class.java)
                    intent.putExtra("Key", userKey)
                    startActivity(intent)
                }
            }
        }


    }

    private fun getSecurityQuestion(
        email: String,
        questionTextView: TextView,
        questionAnswerTextView: TextView,
    ) {

        var securityQuestionHashMap: HashMap<String, String>
        val database = FirebaseDatabase.getInstance().getReference("Users")
        database.get().addOnSuccessListener {
            val hashMap: HashMap<String, HashMap<String, String>> =
                it.value as HashMap<String, HashMap<String, String>>

            var found: Boolean = false
            for ((key, value) in hashMap) {
                if (value["email"].equals(email)) {
                    found = true
                    securityQuestionHashMap = value["securityQuestion"] as HashMap<String, String>
                    questionTextView.text = securityQuestionHashMap["question"]
                    questionAnswer = securityQuestionHashMap["answer"] as String
                    userKey = key
                    break
                }
            }

            if (!found) {
                Toast.makeText(this, "User Doesn't Exist", Toast.LENGTH_SHORT).show()
            }

        }.addOnFailureListener {
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
        }
        if (questionAnswer == "") {
            questionAnswerTextView.text = ""
            questionTextView.text = ""
            userKey = ""
        }

    }
}