package com.example.storybaru.feature.register

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.storybaru.R
import com.example.storybaru.ViewModelFactory
import com.example.storybaru.databinding.ActivityRegisterBinding
import com.example.storybaru.feature.login.Login
import com.example.storybaru.repositories.Result

class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        binding.apply {
            registerButton.setOnClickListener {
                onClickButtonRegister()
            }
            toLogin.setOnClickListener{
                onClicktoLogin()
            }

        }
        showAnimation()
    }

    private fun onClickButtonRegister(){
        val viewModelFactory : ViewModelFactory = ViewModelFactory.getInstance(this)
        val registerViewModel : RegisterViewModel by viewModels{ viewModelFactory}
        val nameValue  = binding.namaRegister.text.toString().trim()
        val emailValue = binding.emailRegister.text!!.toString().trim()
        val passwordValue = binding.passwordRegister.text!!.toString().trim()
        if (passwordValue.length < 8 ){
            Toast.makeText(this@Register,R.string.errorregister,Toast.LENGTH_SHORT).show()
        }else{
            registerViewModel.saveUserRegister(nameValue,emailValue,passwordValue).observe(this@Register){
                when (it) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        Toast.makeText(this@Register, R.string.register, Toast.LENGTH_SHORT).show()
                        showLoading(false)
                        onClicktoLogin()
                    }
                    is Result.Error -> {
                        Toast.makeText(this@Register, R.string.errorregister, Toast.LENGTH_SHORT).show()
                        showLoading(false)

                    }
                }
            }
        }

    }

    private fun onClicktoLogin(){
        val intent = Intent(this@Register, Login::class.java)
        startActivity(intent)
        finish()
    }

    private fun showLoading(isLoading : Boolean){
        if (isLoading){
            binding.progressregister.visibility = View.VISIBLE
        }else{
            binding.progressregister.visibility = View.INVISIBLE
        }
    }
    private fun showAnimation() {
        ObjectAnimator.ofFloat(binding.welcomeregis, View.TRANSLATION_X, -75f, 75f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }
}