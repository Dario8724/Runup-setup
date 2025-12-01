package pt.iade.ei.runupsetup.network

import pt.iade.ei.runupsetup.models.HistoryItemDto
import pt.iade.ei.runupsetup.models.CorridaDetalheDto
import pt.iade.ei.runupsetup.models.CorridaGeradaDto
import pt.iade.ei.runupsetup.models.GenerateCorridaRequestDto
import pt.iade.ei.runupsetup.models.FinalizarCorridaRequestDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.*
import retrofit2.Response

interface RunUpApi{
    @Headers("Content-Type: application/json")


    @POST("/usuarios/cadastrar")
    suspend fun cadastrarUsuario(
        @Body usuario: UserRegisterDto
    ): Response<Void>

    @POST("/usuarios/login")
    suspend fun login(
        @Body loginRequest: LoginRequestDto
    ): Response<LoginResponseDto>

    @GET("/api/corridas/historico")
    suspend fun getHistorico(
        @Query("userId") userId: Int
    ): Response<List<HistoryItemDto>>

    @GET("/api/corridas/{corridaId}")
    suspend fun getCorridaDetalhe(
        @Path("corridaId") corridaId: Int
    ): Response<CorridaDetalheDto>

    @POST("/api/corridas/gerar")
    suspend fun gerarCorrida(
        @Body request: GenerateCorridaRequestDto
    ) : Response<CorridaGeradaDto>

    @POST("api/corridas/{id}/finalizar")
    suspend fun finalizarCorrida(
        @Path("id") corridaId: Int,
        @Body body: FinalizarCorridaRequestDto
    ) : Response<Void>

}