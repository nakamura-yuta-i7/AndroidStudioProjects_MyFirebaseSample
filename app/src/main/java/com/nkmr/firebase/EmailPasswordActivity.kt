package com.nkmr.firebase

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.android.synthetic.main.activity_email_password.*
import android.content.Intent
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import com.google.firebase.auth.GoogleAuthProvider


class EmailPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_password)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        var mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        val account = GoogleSignIn.getLastSignedInAccount(this)
//        updateUI(account)
        Log.i("TAG1", account.toString())

        sign_in_button.setOnClickListener {

            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        nextButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut();
        }
    }

    val RC_SIGN_IN = 9001
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }


    private lateinit var mAuth: FirebaseAuth
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
//            updateUI(account)
            Log.i("TAG2", account.email)

            mAuth = FirebaseAuth.getInstance();
            val currentUser = mAuth.getCurrentUser();

            firebaseAuthWithGoogle(account);
            Log.i("TAG2-2", account.email)

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TAG3", "signInResult:failed code=" + e.statusCode)
//            updateUI(null)
        }

    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d("TAG4", "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG4-2", "signInWithCredential:success")
                    val user = mAuth.getCurrentUser()
//                    updateUI(user)
                    Log.d("TAG4-2-2", user!!.email)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG4-3", "signInWithCredential:failure", task.exception)
                    Toast.makeText(this@EmailPasswordActivity, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
//                    updateUI(null)

                }

                // ...
            }
    }
}
