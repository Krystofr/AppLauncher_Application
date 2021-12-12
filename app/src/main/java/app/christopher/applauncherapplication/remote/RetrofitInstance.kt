package app.christopher.applauncherapplication.remote

import app.christopher.applauncherapplication.data.WeatherData
import retrofit2.Response
import retrofit2.http.GET

interface RetrofitInstance {

    @GET("/weather/London")
    suspend fun getWeatherApi(): List<WeatherData>
}