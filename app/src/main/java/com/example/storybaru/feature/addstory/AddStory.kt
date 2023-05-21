package com.example.storybaru.feature.addstory

import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import com.example.storybaru.R
import com.example.storybaru.Utils.createCustomTempFile
import com.example.storybaru.Utils.reduceFileImage
import com.example.storybaru.ViewModelFactory
import com.example.storybaru.databinding.ActivityAddStoryBinding
import com.example.storybaru.Utils.uriToFile
import com.example.storybaru.feature.beranda.Beranda
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class AddStory : AppCompatActivity() {
    private  lateinit var binding: ActivityAddStoryBinding
    private val viewModelFactory = ViewModelFactory.getInstance(this)
    private val addStoryViewModel : AddStoryViewModel by viewModels { viewModelFactory }
    private var getFile: File? = null
    private lateinit var currentPhotoPath: String



    private val launcherIntentGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
      if (it.resultCode == RESULT_OK){
          val selectedImg = it.data?.data as Uri
          selectedImg.let {Uri ->
              val myFile = uriToFile(Uri,this@AddStory)
              getFile = myFile
              binding.imageadd.setImageURI(Uri)
          }
      }

    }
    private val launcherIntentCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)

            myFile.let { file ->
                getFile = file
                binding.imageadd.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        addStoryViewModel.getToken().observe(this){token ->
            binding.buttonadd.setOnClickListener {
                if (token != null) {
                    uploadImage(token)
                }
            }
        }

        binding.apply {
            buttongallery.setOnClickListener{
                startGallery()
            }
            buttoncamera.setOnClickListener {
                startTakePhoto()
            }
        }
    }

    private fun startGallery(){
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent,"Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }
    private fun startTakePhoto(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoUri :Uri = FileProvider.getUriForFile(this@AddStory,"com.example.storybaru",it)
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun uploadImage(token : String){
        showLoading(true)
        val desc = binding.descripstionadd.text.toString()

        if (getFile == null){
            Toast.makeText(this@AddStory, getString(R.string.errorimage), Toast.LENGTH_SHORT).show()
            showLoading(false)
        }else if (desc.isEmpty()){
            Toast.makeText(this@AddStory, getString(R.string.errordesc), Toast.LENGTH_SHORT).show()
            showLoading(false)
        }else if (token != null ){
            val file = reduceFileImage(getFile as File)
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart : MultipartBody.Part = MultipartBody.Part.createFormData("photo",file.name,requestImageFile)

            addStoryViewModel.addStory(token,desc,imageMultipart).observe(this){
                when (it){
                    is com.example.storybaru.repositories.Result.Loading ->{
                        showLoading(true)
                        Toast.makeText(this@AddStory, getString(R.string.uploadwait), Toast.LENGTH_SHORT).show()
                    }
                    is com.example.storybaru.repositories.Result.Success ->{
                        showLoading(false)
                        Toast.makeText(this@AddStory, getString(R.string.succesaddstory), Toast.LENGTH_SHORT).show()
                        toBeranda()

                    }
                    is com.example.storybaru.repositories.Result.Error ->{
                        showLoading(false)
                        Toast.makeText(this@AddStory, getString(R.string.failedaddstory), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun toBeranda(){
        val intent = Intent(this@AddStory,Beranda::class.java)
        startActivity(intent)
        finish()
    }
    private fun showLoading(isLoading : Boolean){
        if (isLoading){
            binding.progressadd.visibility = View.VISIBLE
        }else{
            binding.progressadd.visibility = View.INVISIBLE
        }
    }

}