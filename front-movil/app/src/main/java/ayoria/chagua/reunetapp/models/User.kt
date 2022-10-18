package ayoria.chagua.reunetapp.models

data class User(
    val id: Int,
    val email: String,
    val avatar: String,
    val firstname: String,
    val lastname: String,
    val role: String,
    val token: String
)