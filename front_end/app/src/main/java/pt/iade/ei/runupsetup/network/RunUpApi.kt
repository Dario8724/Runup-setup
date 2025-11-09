package pt.iade.ei.runupsetup.network

import pt.iade.ei.runupsetup.models.RouteRequest
import pt.iade.ei.runupsetup.models.RouteResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RunUpApi{

    @Headers("Content-Type: application/json")
    @POST("/api/routes/generate")
    fun generateRoute(@Body request: RouteRequest): Call<RouteResponse>
}