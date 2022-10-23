package ayoria.chagua.reunetapp.models

data class User(
    val email: String,
    val password: String,
    val firstname: String,
    val lastname: String,
    val role: String,
)