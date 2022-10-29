package com.example.code_fixturecontestsmanager.activities.login

import android.content.Intent
import android.content.IntentSender
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.code_fixturecontestsmanager.Constants
import com.example.code_fixturecontestsmanager.MainActivity
import com.example.code_fixturecontestsmanager.R
import com.example.code_fixturecontestsmanager.databinding.ActivityLoginBinding
import com.example.code_fixturecontestsmanager.models.UserDetailsContainer
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    lateinit var oneTapClient: SignInClient
    lateinit var signInRequest: BeginSignInRequest
    private val REQ_ONE_TAP = Constants.REQ_TAP_LOGIN
    private var showOneTapUI = true
    private var auth: FirebaseAuth = Firebase.auth
    val siteCollection = Firebase.firestore.collection(Constants.FIREBASE_COLLECTION_REFERENCE1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        supportActionBar?.hide()
        binding.registerButton.setOnClickListener {
            oneTapClient = Identity.getSignInClient(this)
            signInRequest = BeginSignInRequest.builder()
                .setPasswordRequestOptions(
                    BeginSignInRequest.PasswordRequestOptions.builder()
                        .setSupported(true)
                        .build()
                )
                .setGoogleIdTokenRequestOptions(
                    BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setFilterByAuthorizedAccounts(false)
                        .setServerClientId(getString(R.string.default_web_client_id))
                        .build()
                )
                .build()
            oneTapClient.beginSignIn(signInRequest).addOnSuccessListener(this) { result ->
                try {
                    startIntentSenderForResult(
                        result.pendingIntent.intentSender, REQ_ONE_TAP,
                        null, 0, 0, 0, null
                    )
                } catch (e: IntentSender.SendIntentException) {

                }
            }.addOnFailureListener(this) { e ->
                Toast.makeText(this, "No Google Accounts Found!", Toast.LENGTH_SHORT).show()
                Log.d("ffg", e.message.toString())
            }
        }

        binding.signInButton.setOnClickListener {
            oneTapClient = Identity.getSignInClient(this)
            signInRequest = BeginSignInRequest.builder()
                .setPasswordRequestOptions(
                    BeginSignInRequest.PasswordRequestOptions.builder()
                        .setSupported(true)
                        .build()
                )
                .setGoogleIdTokenRequestOptions(
                    BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setFilterByAuthorizedAccounts(true)
                        .setServerClientId(getString(R.string.default_web_client_id))
                        .build()
                )
                .build()
            oneTapClient.beginSignIn(signInRequest).addOnSuccessListener(this) { result ->
                try {
                    startIntentSenderForResult(
                        result.pendingIntent.intentSender, REQ_ONE_TAP,
                        null, 0, 0, 0, null
                    )
                } catch (e: IntentSender.SendIntentException) {

                }
            }.addOnFailureListener(this) { e ->
                Toast.makeText(this, "No Google Accounts Found!", Toast.LENGTH_SHORT).show()
                Log.d("ffg", e.message.toString())
            }
        }

    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            currentUser?.email?.let {
                siteCollection.document(it).get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        if (document != null && !document.exists()) {
                            siteCollection.document(it).set(UserDetailsContainer()) // Initialise user on Login with empty values
                        }
                    } else {
                        Toast.makeText(
                            this,
                            task.exception.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQ_ONE_TAP -> {
                try {
                    val credential = oneTapClient?.getSignInCredentialFromIntent(data)
                    val idToken = credential?.googleIdToken
                    val password = credential?.password
                    when {
                        idToken != null -> {
                            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                            auth.signInWithCredential(firebaseCredential)
                                .addOnCompleteListener(this) { task ->
                                    if (task.isSuccessful) {
                                        auth.currentUser?.email?.let {
                                            siteCollection.document(it).get().addOnCompleteListener { task ->
                                                if (task.isSuccessful) {
                                                    val document = task.result
                                                    if (document != null && !document.exists()) {
                                                        siteCollection.document(it).set(UserDetailsContainer()) // Initialise user on Login with empty values
                                                    }
                                                } else {
                                                    Toast.makeText(
                                                        this,
                                                        task.exception.toString(),
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                        }
                                        startActivity(Intent(this, MainActivity::class.java))
                                        finish()
                                    } else {
                                        Toast.makeText(this, "Try AGAIN!!", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }
                        }
                        password != null -> {}
                        else -> {}
                    }
                } catch (e: ApiException) {
                    when (e.statusCode) {
                        CommonStatusCodes.CANCELED -> {
                            showOneTapUI = false
                        }
                        CommonStatusCodes.NETWORK_ERROR -> {
                            Toast.makeText(this, "Network Error!!", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            Toast.makeText(this, "Some Error Occurred!!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}