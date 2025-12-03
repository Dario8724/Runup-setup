package pt.iade.ei.runupsetup.network

data class UserProfileDto(
    val id: Long,
    val nome: String,
    val email: String,
    val dataNascimento: String?,
    val sexo: String?,
    val alturaCm: Double?,
    val pesoKg: Double?
)

data class UpdateUserProfileRequestDto(
    val nome: String,
    val dataNascimento: String?,
    val sexo: String?,
    val alturaCm: Double?,
    val pesoKg: Double?
)