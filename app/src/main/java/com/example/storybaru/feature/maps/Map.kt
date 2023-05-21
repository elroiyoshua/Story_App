package com.example.storybaru.feature.maps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.storybaru.R
import com.example.storybaru.ViewModelFactory
import com.example.storybaru.databinding.ActivityMapBinding
import com.example.storybaru.feature.beranda.Beranda
import com.example.storybaru.responses.ListStoryItem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

class Map : AppCompatActivity(),OnMapReadyCallback {
    private lateinit var mMap : GoogleMap
    private lateinit var binding : ActivityMapBinding
    private val boundsBuilder = LatLngBounds.Builder()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val openMapfragment = supportFragmentManager.findFragmentById(R.id.map)as SupportMapFragment
        openMapfragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        val viewModelFactory : ViewModelFactory = ViewModelFactory.getInstance(this)
        val mapViewModel : MapViewModel by viewModels{ viewModelFactory }

        mapViewModel.getToken().observe(this){token ->
            if(token != null){
                mapViewModel.getLocation(token).observe(this){
                    when(it){
                        is com.example.storybaru.repositories.Result.Loading ->{
                            showLoading(true)
                        }
                        is com.example.storybaru.repositories.Result.Success ->{
                            addMark(it.data.listStory)
                            showLoading(false)
                        }
                        is com.example.storybaru.repositories.Result.Error ->{
                            Toast.makeText(this@Map,"Failed To Get Location", Toast.LENGTH_SHORT).show()
                            showLoading(false)
                        }
                    }
                }
            }

        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.normal_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }
            R.id.satellite_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }
            R.id.terrain_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }
            R.id.hybrid_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
    private  fun addMark(listlocation : List<ListStoryItem>){
        listlocation.forEach{
            if (it.lat != null && it.lon != null ){
                val latLng = LatLng(it.lat as Double, it.lon as Double)

                mMap.addMarker(MarkerOptions().position(latLng).title(it.name).snippet(it.description))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                boundsBuilder.include(latLng)
            }
        }
        val bounds : LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(
        CameraUpdateFactory.newLatLngBounds(
            bounds,resources.displayMetrics.widthPixels,resources.displayMetrics.heightPixels,300)
        )
    }

    private fun showLoading(isLoading : Boolean){
        if (isLoading){
            binding.progressBarMap.visibility = View.VISIBLE
        }else{
            binding.progressBarMap.visibility = View.INVISIBLE
        }
    }
}