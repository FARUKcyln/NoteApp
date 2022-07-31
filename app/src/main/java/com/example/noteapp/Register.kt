package com.example.noteapp

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.noteapp.databinding.ActivityMainBinding
import com.example.noteapp.databinding.ActivityRegisterBinding
import com.example.noteapp.models.SecurityQuestion
import com.example.noteapp.models.User
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Register : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUp.setOnClickListener {

            register(
                binding.FullName.text.toString(),
                binding.EmailAddress.text.toString(),
                binding.Password.text.toString(),
                SecurityQuestion(
                    binding.spinner.selectedItem.toString(),
                    binding.questionAnswer.text.toString()
                )
            )
        }

        val questions: List<String> = listOf(
            "Choose security question",
            "What is your best friend name?",
            "What is your best colour?"
        )
        val adapter = ArrayAdapter(
            this,
            R.layout.simple_spinner_item, questions
        )
        binding.spinner.adapter = adapter
    }

    private fun register(
        fullName: String,
        email: String,
        password: String,
        securityQuestion: SecurityQuestion
    ) {

        val database = Firebase.database
        val myRef = database.reference.child("Users")

        myRef.get().addOnSuccessListener {
            val hashMap: HashMap<String, HashMap<String, String>> =
                it.value as HashMap<String, HashMap<String, String>>

            var found: Boolean = false
            for ((key, value) in hashMap) {
                if (value["email"].equals(email)) {
                    Toast.makeText(this, "User Already Exist", Toast.LENGTH_SHORT).show()
                    binding.EmailAddress.text.clear()
                    found = true
                    break
                }
            }
            if (!found) {
                myRef.push().setValue(User(fullName, email, password, securityQuestion))
                Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show()
                this.finish()
            }
        }.addOnFailureListener {
            println("Error getting data $it")
        }
    }
}