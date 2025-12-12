package pt.iade.ei.runupsetup.utils

object UserRegistrationState{

    // Tela 1 – RegisterPageActivity
    var nome: String = ""
    var email: String = ""
    var senha: String = ""
    var dataAniversario: String = ""

    // Tela 2 – QuestionnaireGenderActivity
    var sexo: String = ""

    // Tela 3 – QuestionnairePageActivity
    var peso: Double = 0.0
    var altura: Double = 0.0
    var experiencia: String = ""

    fun reset() {
        nome = ""
        email = ""
        senha = ""
        dataAniversario = ""
        sexo = ""
        peso = 0.0
        altura = 0.0
        experiencia = ""
    }
}