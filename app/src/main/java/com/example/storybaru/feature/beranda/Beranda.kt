package com.example.storybaru.feature.beranda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storybaru.R
import com.example.storybaru.ViewModelFactory
import com.example.storybaru.databinding.ActivityBerandaBinding
import com.example.storybaru.feature.addstory.AddStory
import com.example.storybaru.feature.beranda.loading.LoadingStoryAdapter
import com.example.storybaru.feature.detail.Detail
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
            if (token != null){
                setAllStories(berandaViewModel,token)
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
            R.id.mapmenu->{
                val intent = Intent(this@Beranda,com.example.storybaru.feature.maps.Map::class.java)
                startActivity(intent)
            }
            R.id.logout->{
                berandaViewModel.clearToken()
                backToLogin()
            }R.id.addstory->{
                val intent = Intent(this@Beranda,AddStory::class.java)
                startActivity(intent)
            }R.id.changelanguage->{
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun backToLogin(){
        val intent = Intent(this@Beranda,Login::class.java)
        startActivity(intent)
        finish()
    }
    private fun setAllStories(berandaViewModel: BerandaViewModel,token:String){
        val adapter = BerandaAdapter()
        adapter.setOnItemClick(object  : BerandaAdapter.OnItemClick{
            override fun onItemClicked(data: ListStoryItem) {
                val intent = Intent(this@Beranda,Detail::class.java)
                intent.putExtra(ID,data.id)
                startActivity(intent)
            }
        })
        binding.recyclerView.adapter = adapter.withLoadStateFooter(
            footer = LoadingStoryAdapter{
                adapter.retry()
            }
        )
        berandaViewModel.getAllStories(token).observe(this,{
            adapter.submitData(lifecycle,it)
        })
    }



    companion object{
        const val ID = "id"
    }
}


