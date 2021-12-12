package app.christopher.applauncherapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.christopher.applauncherapplication.data.WeatherData
import app.christopher.applauncherapplication.databinding.ActivityMainBinding
import app.christopher.applauncherapplication.databinding.WeatherItemBinding

class WeatherAdapter() : RecyclerView.Adapter<WeatherAdapter.WeatherVH>() {

    inner class WeatherVH(val binding: WeatherItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(weatherData: WeatherData){
            binding.apply {
                temperatureData.text = weatherData.temperature
                windData.text = weatherData.wind
                descriptionData.text = weatherData.description
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<WeatherData>() {
        override fun areItemsTheSame(oldItem: WeatherData, newItem: WeatherData): Boolean {
            return oldItem.description == newItem.description
        }

        override fun areContentsTheSame(oldItem: WeatherData, newItem: WeatherData): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var weatherList: List<WeatherData>
        get() = differ.currentList
        set(value) { differ.submitList(value) }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherVH {
        val binding = WeatherItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherVH(binding)
    }

    override fun onBindViewHolder(holder: WeatherVH, position: Int) {
        val modelData = weatherList[position]
        holder.bind(modelData)
    }

    override fun getItemCount() = weatherList.size
}