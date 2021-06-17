package br.senai.sp.jandira.viagens.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import br.senai.sp.jandira.viagens.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

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
            updateUI()
        }
    }

    private fun updateUI() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN && data !== null) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            val usuario = task.getResult(ApiException::class.java)

            if(usuario != null) {
               val dadosUsuario = getSharedPreferences("dados_usuario", Context.MODE_PRIVATE)
                val editor = dadosUsuario.edit()
                editor.putString("display_name", usuario.displayName)
                editor.putString("email", usuario.email)
                editor.putString("url_photo", usuario.photoUrl.toString())
                editor.putString("id", usuario.id)

                editor.apply()

               updateUI()
            }
        }
    }
}