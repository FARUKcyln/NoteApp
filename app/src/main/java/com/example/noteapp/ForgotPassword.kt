package com.example.noteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import com.example.noteapp.databinding.ActivityForgotPasswordBinding
import com.example.noteapp.databinding.ActivityMainBinding
import com.example.noteapp.models.SecurityQuestion
import com.google.firebase.database.FirebaseDatabase

class ForgotPassword : AppCompatActivity() {

    lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.forgotMail.setOnFocusChangeListener { view, b ->
            getSecurityQuestion(
                binding.forgotMail.text.toString(),
                binding.securityQuestion
            )
        }


    }

    private fun getSecurityQuestion(email: String, questionTextView: TextView): String {

        var securityQuestionHashMap: HashMap<String, String>
        var securityQuestionAnswer: String? = null
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
                    securityQuestionAnswer = securityQuestionHashMap["question"].toString()
                    break
                }
            }

            if (!found) {
                Toast.makeText(this, "User Doesn't Exist", Toast.LENGTH_SHORT).show()
            }

        }.addOnFailureListener {
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
        }
        return if (securityQuestionAnswer == null) {
            questionTextView.text = ""
            "null"
        } else {
            securityQuestionAnswer as String
        }

    }
}