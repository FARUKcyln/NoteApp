package com.example.noteapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.Toast
import com.example.noteapp.databinding.ActivityForgotPasswordBinding
import com.example.noteapp.databinding.ActivityRegisterBinding
import com.example.noteapp.databinding.ActivityResetPasswordBinding
import com.example.noteapp.models.User
import com.google.firebase.database.FirebaseDatabase

class ResetPassword : AppCompatActivity() {
    lateinit var binding: ActivityResetPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.reset.setOnClickListener {
            if (binding.newPassword.text.isNotEmpty() && binding.newPasswordConfirm.text.isNotEmpty()) {
                if (binding.newPassword.text.toString() == binding.newPasswordConfirm.text.toString()) {
                    val intent = this.intent
                    val key = intent.getStringExtra("Key")
                    val database =
                        FirebaseDatabase.getInstance().getReference("Users").child(key.toString())
                    database.get().addOnSuccessListener {
                        val user: HashMap<String, Any> =
                            it.value as HashMap<String, Any>
                        println(user["fullName"])
                        user["password"] = binding.newPassword.text.toString()
                        database.updateChildren(user)
                        Toast.makeText(this, "Successfully Changed", Toast.LENGTH_SHORT).show()
                        this.finish()
                        val newIntent = Intent(this, MainActivity::class.java)
                        startActivity(newIntent)
                    }.addOnFailureListener {
                        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }
}