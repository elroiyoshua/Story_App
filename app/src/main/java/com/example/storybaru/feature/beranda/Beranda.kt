package com.example.storybaru.feature.beranda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storybaru.R
import com.example.storybaru.ViewModelFactory
import com.example.storybaru.databinding.ActivityBerandaBinding
import com.example.storybaru.feature.login.Login
import com.example.storybaru.responses.ListStoryItem

class Beranda : AppCompatActivity() {
    private lateinit var binding : ActivityBerandaBinding
    private val viewModelFactory = ViewModelFactory.getInstance(this)
    private val berandaViewModel : BerandaViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBerandaBinding.inflate(layoutInflater)

        val layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        val decoration = DividerItemDecoration(this,layoutManager.orientation)

        binding.apply {
            recyclerView.layoutManager = layoutManager
            recyclerView.addItemDecoration(decoration)

        }
        berandaViewModel.getToken().observe(this){token->
            if(token != null){
                berandaViewModel.getAllStories(token).observe(this){
                    when(it){
                        is com.example.storybaru.repositories.Result.Loading ->{
                            showLoading(true)
                        }
                        is com.example.storybaru.repositories.Result.Success -> {
                            setAllStories(it.data.listStory)
                            showLoading(false)
                        }
                        is com.example.storybaru.repositories.Result.Error ->{
                            Toast.makeText(this@Beranda,R.string.errorgetallstories, Toast.LENGTH_SHORT).show()
                            showLoading(true)
                        }
                    }
                }
            }

        }


        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logout->{
                berandaViewModel.clearToken()
                backToLogin()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun backToLogin(){
        val intent = Intent(this@Beranda,Login::class.java)
        startActivity(intent)
        finish()
    }
    private fun setAllStories(data : List<ListStoryItem>){
        val adapter = BerandaAdapter(data)
        binding.recyclerView.adapter = adapter
    }

    private fun showLoading(isLoading : Boolean){
        if (isLoading){
            binding.progressberanda.visibility = View.VISIBLE
        }else{
            binding.progressberanda.visibility = View.INVISIBLE
        }
    }


}