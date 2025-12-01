package pt.iade.ei.runupsetup.network

data class UserRegisterDto(
    val nome: String,
    val email: String,
    val senha: String,
    val dataDeNascimento: String?,
    val sexo: String?,
    val peso: Double?,
    val altura: Double?,
    val experiencia: String?
)
