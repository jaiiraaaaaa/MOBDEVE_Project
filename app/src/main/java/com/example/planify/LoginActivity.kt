package com.example.planify

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.planify.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding: ActivityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.loginBtnLogin.setOnClickListener {
            val intent = Intent(this, OverviewActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            this.startActivity(intent)
            finish()
        }
    }
}
