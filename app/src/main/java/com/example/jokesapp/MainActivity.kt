package com.example.jokesapp

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.TokenWatcher
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.jokesapp.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private  lateinit var  mainActivityViewModel: MainActivityViewModel
    private lateinit var  viewPagerAdapter:ViewPagerAdapter

    private val myJokesLiveData= MutableLiveData<List<Joke>>()

    private var refreshJokes = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(
            LayoutInflater.from(this)
        )
        setContentView(binding.root)


        checkMode()
        mainActivityViewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
         viewPagerAdapter = ViewPagerAdapter()
        binding.viewPager.apply {
            adapter = viewPagerAdapter
            orientation = ViewPager2.ORIENTATION_VERTICAL

        }


        val jokesLiveData = liveData {
            val data = mainActivityViewModel.getJokeResponse()
            emit(data)
        }


        binding.imageView.setOnClickListener {
            refreshJokes =true
            loadMoreJokes()
        }


        jokesLiveData.observe(this, Observer {
            val jokeResponse = it?.body()
            if(it.isSuccessful){
                myJokesLiveData.value = jokeResponse?.jokes!!
                //viewPagerAdapter.addJokes(jokeResponse?.jokes!!)
            }
            else{
                Log.d("JOKES","Failure")
            }

        })

        myJokesLiveData.observe(this, Observer {

            if(refreshJokes){
                viewPagerAdapter.refreshJokes(it)
                binding.viewPager.currentItem = 0
                refreshJokes =false
            }
            else{
                viewPagerAdapter.addJokes(it)
            }

        })

        binding.viewPager.registerOnPageChangeCallback(object: OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if(position%10 == 8){
                    //Toast.makeText(this@MainActivity,"Hello",Toast.LENGTH_SHORT).show()
                    loadMoreJokes()
                }
            }

        })





    }

    override fun onResume() {
        super.onResume()

        checkMode()
    }



    fun loadMoreJokes(){
        val jokeLiveData = liveData {
            emit(mainActivityViewModel.getJokeResponse())
        }
        jokeLiveData.observe(this, Observer {
            val jokeResponse = it?.body()
            if(it.isSuccessful){
                myJokesLiveData.value = jokeResponse?.jokes!!
                //viewPagerAdapter.addJokes(jokeResponse?.jokes!!)
            }
            else{
                Log.d("JOKES","Failure")
            }

        })
    }


    fun checkMode (){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val darkModeStatus = resources.configuration.isNightModeActive
            if(darkModeStatus){
                binding.imageView.setImageDrawable(resources.getDrawable(R.drawable.baseline_refresh_white,null))
                window.statusBarColor = resources.getColor(com.google.android.material.R.color.m3_sys_color_dark_background)

            }else{
                binding.imageView.setImageDrawable(resources.getDrawable(R.drawable.baseline_refresh_24,null))
                window.statusBarColor = resources.getColor(com.google.android.material.R.color.m3_sys_color_light_background)
            }
        }
    }



}