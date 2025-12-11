package pt.iade.ei.runupsetup.network

import pt.iade.ei.runupsetup.models.HistoryItemDto
import pt.iade.ei.runupsetup.models.CorridaDetalheDto
import pt.iade.ei.runupsetup.models.CorridaGeradaDto
import pt.iade.ei.runupsetup.models.GenerateCorridaRequestDto
import pt.iade.ei.runupsetup.models.FinalizarCorridaRequestDto
import pt.iade.ei.runupsetup.models.PredefinedRouteDto
import retrofit2.Response
import retrofit2.http.*

interface RunUpApi {

    @Headers("Content-Type: application/json")
    @POST("api/auth/register")
    suspend fun register(
        @Body registerRequest: UserRegisterDto
    ): Response<LoginResponseDto>

    @POST("api/auth/login")
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
    ): Response<CorridaGeradaDto>

    @POST("api/corridas/{id}/finalizar")
    suspend fun finalizarCorrida(
        @Path("id") corridaId: Int,
        @Body body: FinalizarCorridaRequestDto
    ): Response<Void>

    @GET("/api/goals/{userId}")
    suspend fun getGoals(
        @Path("userId") userId: Long
    ): Response<List<GoalDto>>

    @PUT("/api/goals/users/{userId}")
    suspend fun updateGoals(
        @Path("userId") userId: Long,
        @Body body: UpdateGoalsRequestDto
    ): Response<Void>

    @GET("/api/usuario/{id}/stats")
    suspend fun getUserStats(
        @Path("id") id: Long
    ): Response<UserStatsDto>

    @GET("/api/usuario/{id}/weekly-stats")
    suspend fun getWeeklyStats(
        @Path("id") id: Long
    ): Response<WeeklyStatsDto>

    @GET("/api/usuario/{id}/records/distance")
    suspend fun getPersonalRecords(
        @Path("id") id: Long
    ): Response<PersonalRecordDto>

    @GET("/api/usuario/{id}/profile")
    suspend fun getUserProfile(
        @Path("id") id: Long
    ): Response<UserProfileDto>

    @PUT("/api/usuario/{id}/profile")
    suspend fun updateUserProfile(
        @Path("id") id: Long,
        @Body body: UpdateUserProfileRequestDto
    ): Response<Void>

    @DELETE("/api/usuario/{id}")
    suspend fun deleteUser(
        @Path("id") id: Long
    ): Response<Void>

    @GET("/api/usuario/{id}/today-summary")
    suspend fun getTodaySummary(
        @Path("id") id: Long
    ): Response<TodaySummaryDto>

    @GET("/api/rotas/predefinidas")
    suspend fun getPredefinedRoutes(): Response<List<PredefinedRouteDto>>

    @POST("/api/rotas/predefinidas/{rotaId}/iniciar")
    suspend fun startPredefinedRoute(
        @Path("rotaId") rotaId: Int,
        @Query("userId") userId: Long,
        @Query("tipo") tipo: String
    ): Response<CorridaGeradaDto>
}