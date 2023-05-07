package com.example.storybaru.feature.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.storybaru.Data.AuthDataStore
import com.example.storybaru.R
import com.example.storybaru.ViewModelFactory
import com.example.storybaru.databinding.ActivityLoginBinding
import com.example.storybaru.feature.beranda.Beranda
import com.example.storybaru.feature.register.Register
import com.example.storybaru.responses.LoginResponse
import com.example.storybaru.responses.LoginResult


class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val view : ViewModelFactory = ViewModelFactory.getInstance(this)
        val loginview : LoginViewModel by viewModels {view}
        loginview.apply {
            getToken().observe(this@Login){token->
                if (token != null){
                    Toast.makeText(this@Login,R.string.login,Toast.LENGTH_SHORT).show()
                    toBeranda()
                }
            }
        }
        binding.apply {

            loginbutton.setOnClickListener {
                onClickButtonLogin()
            }


            toRegister.setOnClickListener {
                onClicktoRegister()
            }
        }

    }

    private fun onClickButtonLogin(){
        val viewModelFactory : ViewModelFactory = ViewModelFactory.getInstance(this)
        val loginViewModel : LoginViewModel by viewModels{ viewModelFactory}

        val emailValue = binding.emaillogin.text.toString().trim()
        val passwordValue = binding.passwordlogin.text.toString().trim()

        loginViewModel.getUserLogin(emailValue,passwordValue).observe(this@Login){login->
            when (login){

                is com.example.storybaru.repositories.Result.Loading ->{
                    showLoading(true)
                }
                is com.example.storybaru.repositories.Result.Success ->{
                    Toast.makeText(this@Login,R.string.login,Toast.LENGTH_SHORT).show()
                    showLoading(false)
                    toBeranda()
                }
                is com.example.storybaru.repositories.Result.Error->{
                    Toast.makeText(this@Login,R.string.errorlogin,Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }
            }
        }

    }

    private fun toBeranda(){
        val home = Intent(this@Login, Beranda::class.java)
        startActivity(home)
        finish()
    }
    private fun onClicktoRegister(){
        val intent = Intent(this@Login, Register::class.java)
        startActivity(intent)
        finish()
    }

    private fun showLoading(isLoading : Boolean){
        if (isLoading){
            binding.progresslogin.visibility = View.VISIBLE
        }else{
            binding.progresslogin.visibility = View.INVISIBLE
        }
    }

}