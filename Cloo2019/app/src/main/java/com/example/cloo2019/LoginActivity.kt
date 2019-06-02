package com.example.cloo2019

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth

import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

class LoginActivity : AppCompatActivity() {

    private val LOGIN_REQUEST_CODE: Int = 1234

    lateinit var providers : List<AuthUI.IdpConfig>



    private val TAG = "LoginActivity"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(toolbar)


        providers = Arrays.asList<AuthUI.IdpConfig>(
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build()
        )

//        showSignInOptions()

        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build(), LOGIN_REQUEST_CODE
        )


        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        finish()
        
        if(requestCode == LOGIN_REQUEST_CODE) {
            val response = IdpResponse.fromResultIntent(data)

            if(resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
                Toast.makeText(this, "" + user!!.email, Toast.LENGTH_SHORT).show()
                Thread.sleep(2000)

                CurrentUser.setCurrentUser(user.displayName!!, user.uid)
                val f = Intent(this, LandingActivity::class.java)
                startActivity(f)
                finish()
            }
            else{
                val err = response?.error?.errorCode
                Log.d(TAG, "Error in Login Process = $err")
                Toast.makeText(this, "Sign in Failed", Toast.LENGTH_SHORT).show()

            }
        }
    }


//    private fun showSignInOptions() {
////        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
//            .setAvailableProviders(providers)
//            .build(), LOGIN_REQUEST_CODE
//        )
//
//
//    }

}
