package app.christopher.applauncherapplication.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherApi {
        val restApi: RetrofitInstance by lazy {
            Retrofit.Builder()
                .baseUrl("https://goweather.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RetrofitInstance::class.java)
        }

}