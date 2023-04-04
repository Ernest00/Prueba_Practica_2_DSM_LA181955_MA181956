package org.example.pruebapractica2dsm_la181955_ma181956

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var btnLogin: Button

    private lateinit var mAuth: FirebaseAuth
    //escuchador de firebaseauth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        btnLogin = findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener {
            val email = findViewById<EditText>(R.id.editTextEmailAddress).text.toString()
            val password = findViewById<EditText>(R.id.txtPassword).text.toString()

            if (email.toString().isNotEmpty() && password.toString().isNotEmpty()) {
                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "User login successful")
                            Menu()
                        } else {
                            Log.e(TAG, "User login failed", task.exception)
                        }
                    }

                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "User registration successful")
                            Menu()
                        } else {
                            Log.e(TAG, "User registration failed", task.exception)
                        }
                    }
            } else {
                Toast.makeText(this, "Ingrese sus credenciales, por favor", Toast.LENGTH_LONG).show()
            }
        }

    }


    fun Menu(){
        val intent = Intent( this, ProductosActivity::class.java)
        startActivity(intent)
        finish()
    }
}