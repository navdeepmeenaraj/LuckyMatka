package lucky.online.matka.app.web.model

data class PasswordReset(
    val mobile: String,
    val password: String,
    val confirmPassword: String
)
