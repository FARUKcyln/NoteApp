package com.example.noteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.noteapp.databinding.ActivityMainBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAnalytics: FirebaseAnalytics


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // FCM Token
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                println("Fetching FCM registration token failed")
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            println(token)
            Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
        })

        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = Firebase.analytics


        binding.button.setOnClickListener {
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
        }

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
        buttonNumber: String,
        buttonText: String,
        analytics: FirebaseAnalytics
    ) {
        println("Button $buttonNumber is clicked.")
        val bundle = Bundle()
        bundle.putString(buttonNumber, buttonText)
        analytics.logEvent(buttonText, null)
    }
}