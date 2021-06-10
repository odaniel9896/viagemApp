package br.senai.sp.jandira.viagens.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.senai.sp.jandira.viagens.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton

const val RC_SIGN_IN = 1500

class LoginActivy : AppCompatActivity(), View.OnClickListener {

    lateinit var btnLogin : SignInButton
    lateinit var mGoogleSignInClient : GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_activy)

        btnLogin = findViewById(R.id.sign_in_button)
        btnLogin.setOnClickListener(this)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

       mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    override fun onStart() {
        super.onStart()

        val account = GoogleSignIn.getLastSignedInAccount(this)

        if(account != null) {
            updateUI(account)
        }
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onClick(v: View) {
        if(v.id == R.id.sign_in_button) {
            signIn()
        }
    }

    private fun signIn() {
       val intent = mGoogleSignInClient.signInIntent
        startActivityForResult(intent, RC_SIGN_IN)
    }
}