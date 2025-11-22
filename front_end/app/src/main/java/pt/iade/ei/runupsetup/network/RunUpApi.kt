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

    @POST("/usuarios/cadastrar")
    suspend fun cadastrarUsuario(
        @Body usuario: UserRegisterDto
    ): retrofit2.Response<Void>

    @POST("/usuarios/login")
    suspend fun login(
        @Body loginRequest: LoginRequestDto
    ): retrofit2.Response<LoginResponseDto>

}