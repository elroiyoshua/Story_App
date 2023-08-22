package com.example.storybaru.feature.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.storybaru.R
import com.example.storybaru.ViewModelFactory
import com.example.storybaru.databinding.ActivityDetailBinding
import com.example.storybaru.feature.beranda.Beranda.Companion.ID
import com.example.storybaru.feature.beranda.BerandaViewModel
import com.example.storybaru.responses.Story

class Detail : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)

        val viewModelFactory = ViewModelFactory.getInstance(this)
        val detailViewModel : DetailViewModel by viewModels { viewModelFactory }

        supportActionBar?.hide()
        val  id = intent.getStringExtra(ID)
        setContentView(binding.root)

        detailViewModel.apply {
            getToken().observe(this@Detail){token ->
                if(token != null && id!= null){
                    getDetailStory(token, id).observe(this@Detail){
                        when (it){
                            is com.example.storybaru.repositories.Result.Loading ->{
                                showLoading(true)
                            }
                            is com.example.storybaru.repositories.Result.Success->{
                                showDetail(it.data.story)
                                showLoading(false)
                            }
                            is com.example.storybaru.repositories.Result.Error ->{
                                Toast.makeText(this@Detail,R.string.errorgetdetail, Toast.LENGTH_SHORT).show()
                                showLoading(false)
                            }
                        }
                    }
                }
            }
        }

    }
    private fun showDetail(story:Story){
        binding.apply {
            usernamedetail.text = story.name
            descdetail.text = story.description
            Glide.with(this@Detail).load(story.photoUrl).centerCrop().into(imagedetail)
        }
    }


    private fun showLoading(isLoading : Boolean){
        if (isLoading){
            binding.progressdetail.visibility = View.VISIBLE
        }else{
            binding.progressdetail.visibility = View.INVISIBLE
        }
    }

}