package com.example.noteapp

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.widget.Toast
import com.example.noteapp.databinding.ActivityMainBinding
import com.example.noteapp.models.User
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAnalytics: FirebaseAnalytics


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = Firebase.analytics

        binding.forgotPassword.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        binding.forwardSignUp.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        binding.forwardSignUp.setOnClickListener{
            buttonActionEvent("sign_up", firebaseAnalytics)
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        binding.signIn.setOnClickListener {
            buttonActionEvent("sign_in", firebaseAnalytics)
            login(binding.Email.text.toString(), binding.signInPassword.text.toString())
        }

        binding.forgotPassword.setOnClickListener{
            val intent = Intent(this, ForgotPassword::class.java)
            startActivity(intent)
        }

        // FCM Token
        /*FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                println("Fetching FCM registration token failed")
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            println(token)
            Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
        })*/

        // Debug View
        /*binding.button.setOnClickListener {
             buttonActionEvent(
                 "Button_1",
                 "Button_1",
                 firebaseAnalytics
             )
         }
         binding.button2.setOnClickListener {
             buttonActionEvent(
                 "Button_2",
                 "Button_2",
                 firebaseAnalytics
             )
         }
         binding.button3.setOnClickListener {
             buttonActionEvent(
                 "Button_3",
                 "Button_3",
                 firebaseAnalytics
             )
         }*/

        // Test crash
        /*val crashButton = Button(this)
        crashButton.text = "Test Crash"
        crashButton.setOnClickListener {
            if (BuildConfig.DEBUG) {
                FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(false);
            }
            throw RuntimeException("Test Crash") // Force a crash
        }

        addContentView(
            crashButton, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )*/


    }

    private fun buttonActionEvent(
        buttonText: String,
        analytics: FirebaseAnalytics
    ) {
        val bundle = Bundle()
        bundle.putString("buttonNumber", buttonText)
        analytics.logEvent(buttonText, null)
    }


    private fun login(email: String, password: String) {



        val database = FirebaseDatabase.getInstance().getReference("Users")
        database.get().addOnSuccessListener {
            val hashMap: HashMap<String, HashMap<String, String>> =
                it.value as HashMap<String, HashMap<String, String>>

            var found: Boolean = false
            for ((key, value) in hashMap) {
                if (value["email"].equals(email) && value["password"].equals(password)) {
                    Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show()
                    found = true
                    break
                }
            }

            if (!found) {
                Toast.makeText(this, "User Doesn't Exist", Toast.LENGTH_SHORT).show()
            }

        }.addOnFailureListener {
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
        }
    }
}