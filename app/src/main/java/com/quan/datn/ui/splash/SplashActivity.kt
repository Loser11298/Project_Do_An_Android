package com.quan.datn.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.quan.datn.MainActivity
import com.quan.datn.R
import com.quan.datn.databinding.ActivitySplashBinding
import com.quan.datn.ui.login.LoginActivity
import com.quan.datn.ui.profile.ProfileViewModel
import com.quan.datn.ui.utils.DataManager

class SplashActivity : AppCompatActivity() , SplashCallBack{
    private lateinit var viewModel: SplashViewModel
    private lateinit var binding: ActivitySplashBinding
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
        viewModel.callBack = this
        mAuth = FirebaseAuth.getInstance()
        if (mAuth?.currentUser != null && DataManager.getPhoneLogin(this).isNotBlank()){
            viewModel.getInfoBenhNhan(this)
        }else{
            pushToLogin()
        }
    }

    private fun pushToMain(){
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(
                android.R.anim.slide_out_right,
                android.R.anim.slide_in_left
            )
            finish()
        }, 200)
    }

    private fun pushToLogin(){
        Handler(Looper.getMainLooper()).postDelayed({
            FirebaseAuth.getInstance().signOut()
            DataManager.removeLoginSession(this)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(
                android.R.anim.slide_out_right,
                android.R.anim.slide_in_left
            )
            finish()
        }, 200)
    }

    override fun error(message: String?) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    override fun loginFalse() {
        pushToLogin()
    }

    override fun success() {
        pushToMain()
    }

}