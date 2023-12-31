package com.example.jokesapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jokesapp.databinding.ActivityMainBinding
import com.example.jokesapp.databinding.JokeLayoutBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewPagerAdapter : RecyclerView.Adapter<ViewPagerAdapter.JokeViewHolder>() {
    private  var jokesList  = ArrayList<Joke>()


    inner class JokeViewHolder( val binding: JokeLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(joke: Joke ){
            binding.tvCategory.text = "-${joke.category}"
            binding.tvSetup.text = joke.setup
            binding.tvDelivery.text = joke.delivery
            var clickCount  =0

            binding.root.setOnClickListener(){
                if(clickCount !=2 ){
                    clickCount++
                }
                else{
                    clickCount=0
                    binding.likeanim.apply {
                        visibility = View.VISIBLE
                        playAnimation()
                        CoroutineScope(Dispatchers.Default).launch{
                            delay(1800)
                            withContext(Dispatchers.Main){
                                binding.likeanim.visibility = View.GONE
                            }
                        }
                    }
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JokeViewHolder {
        val myBinding = JokeLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return JokeViewHolder(myBinding)
    }

    override fun getItemCount(): Int {
        return jokesList.size
    }

    override fun onBindViewHolder(holder: JokeViewHolder, position: Int) {
        holder.bind(jokesList[position])
        holder.binding.tvPosition.text = (position+1).toString()

    }

    fun addJokes(jokes: List<Joke>){
        jokesList.addAll(jokes)
        notifyDataSetChanged()
    }

    fun refreshJokes(jokes:List<Joke>){
        jokesList.clear()
        jokesList.addAll(jokes)
        notifyDataSetChanged()
    }

}