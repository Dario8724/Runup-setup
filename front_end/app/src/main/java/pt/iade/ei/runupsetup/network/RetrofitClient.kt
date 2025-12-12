package pt.iade.ei.runupsetup.network


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient{

    private const val BASE_URL = "http://localhost:8080/"

    val instance: RunUpApi by  lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RunUpApi::class.java)
    }
}