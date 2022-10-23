package ayoria.chagua.reunetapp.models

data class LoginResponse(
    val avatar: String,
    val email: String,
    val firstname: String,
    val id: Int,
    val lastname: String,
    val role: String,
    val token: String
)