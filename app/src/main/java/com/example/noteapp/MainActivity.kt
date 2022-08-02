package com.example.noteapp

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.noteapp.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private val RC_SIGN_IN: Int = 1

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var gso: GoogleSignInOptions
    private lateinit var mGoogleSignInClient: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = Firebase.analytics

        binding.forgotPassword.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        binding.forwardSignUp.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        binding.forwardSignUp.setOnClickListener {
            buttonActionEvent("sign_up", firebaseAnalytics)
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        binding.signIn.setOnClickListener {
            buttonActionEvent("sign_in", firebaseAnalytics)
            login(binding.Email.text.toString(), binding.signInPassword.text.toString())
        }

        binding.forgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPassword::class.java)
            startActivity(intent)
        }

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            revokeAccess()
        }


        binding.googleLogin.setOnClickListener {

            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            println(account.email)
            Toast.makeText(this, "Successfully Sign In", Toast.LENGTH_SHORT).show()

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            println("signInResult:failed code=" + e.statusCode)

        }
    }

    private fun revokeAccess() {
        mGoogleSignInClient.revokeAccess()
            .addOnCompleteListener(this) {
                // ...
            }
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