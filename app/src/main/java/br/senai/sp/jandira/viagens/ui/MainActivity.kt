package br.senai.sp.jandira.viagens.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.senai.sp.jandira.viagens.R
import br.senai.sp.jandira.viagens.adapter.DestinoRecenteAdapter
import br.senai.sp.jandira.viagens.api.DestinosRecentesCall
import br.senai.sp.jandira.viagens.api.RetrofitApi
import br.senai.sp.jandira.viagens.model.DestinosRecentes
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var rvDestinosRecentes: RecyclerView
    lateinit var adapterDestinosRecentes: DestinoRecenteAdapter
    lateinit var btnSignOut : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        rvDestinosRecentes = findViewById(R.id.rv_destinos_recentes)

        tv_signout.setOnClickListener {
            signOut()
        }

        rvDestinosRecentes.layoutManager =
            LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL, false)

        adapterDestinosRecentes =
            DestinoRecenteAdapter(this)

        rvDestinosRecentes.adapter = adapterDestinosRecentes


        exibirProfile()


        carrgarListaDestinosRecentes()

    }

    private fun carrgarListaDestinosRecentes() {

        var destinosRecentes: List<DestinosRecentes> = listOf<DestinosRecentes>()

        val retrofit = RetrofitApi.getRetrofit()
        val destinosRecentesCall = retrofit.create(DestinosRecentesCall::class.java)

        val call = destinosRecentesCall.getDestinosRecentes()

        call.enqueue(object : Callback<List<DestinosRecentes>> {

            override fun onFailure(call: Call<List<DestinosRecentes>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Ops! Acho que ocorreu um problema.", Toast.LENGTH_SHORT).show()
                Log.e("ERRO_CONEXAO", t.message.toString())
            }

            override fun onResponse(call: Call<List<DestinosRecentes>>, response: Response<List<DestinosRecentes>>) {
                destinosRecentes = response.body()!! // Double BANG!!
                adapterDestinosRecentes.updateListaRecente(destinosRecentes)
            }

        })

    }
    private fun signOut() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        googleSignInClient.signOut()
        finish()
    }
    private fun exibirProfile() {
        val dados = getSharedPreferences("dados_usuario", Context.MODE_PRIVATE)
        display_name.text = dados.getString("display_name", "Nome do Usuario")
        val url = dados.getString("url_photo", null)
        Glide.with(this).load(url).into(iv_profile)

    }

}