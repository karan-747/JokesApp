package com.example.jokesapp

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.TokenWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.liveData
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.jokesapp.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.lang.Exception

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
        binding.lottieSwipeUp.isVisible = false

        binding.imageView.setOnClickListener {
            refreshJokes =true
            loadMoreJokes()
        }



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

        //loadMoreJokes()


        binding.viewPager.registerOnPageChangeCallback(object: OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.lottieSwipeUp.isVisible = false
                if(position%10 == 9){
                    loadMoreJokes()

                }
                if(position == 0){
                    lifecycleScope.launch {
                        delay(4000)
                        withContext(Dispatchers.Main){
                            binding.lottieSwipeUp.isVisible = true
                        }
                    }
                }
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                binding.lottieSwipeUp.isVisible = false
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if(state ==  ViewPager2.SCROLL_STATE_IDLE){
                    lifecycleScope.launch {
                        delay(4000)
                        withContext(Dispatchers.Main){
                            binding.lottieSwipeUp.isVisible = true
                        }
                    }
                }


            }

        })





    }

    override fun onResume() {
        super.onResume()
        loadMoreJokes()
        checkMode()
    }



    fun loadMoreJokes(){
        lifecycleScope.launch {
            val response = try {
                mainActivityViewModel.getJokeResponse()

            }catch (e:IOException){
                withContext(Dispatchers.Main){

                    //Log.d("TAGY-IOException",e.cause.toString())
                    Toast.makeText(this@MainActivity,"Network Error",Toast.LENGTH_LONG).show()
                }
                return@launch
            }
            catch (e:HttpException){
                withContext(Dispatchers.Main){

                    //Log.d("TAGY-HttpException",e.cause.toString())
                    Toast.makeText(this@MainActivity, e.cause.toString(),Toast.LENGTH_LONG).show()
                }
                return@launch
            }
            catch (e:Exception){
                withContext(Dispatchers.Main){
                    //Log.d("TAGY-Exception",e.cause.toString())
                    Toast.makeText(this@MainActivity,e.message.toString(),Toast.LENGTH_LONG).show()
                }
                return@launch
            }
            myJokesLiveData.value = response.body()?.jokes


        }
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