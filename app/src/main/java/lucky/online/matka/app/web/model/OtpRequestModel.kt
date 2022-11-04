package lucky.online.matka.app.web.model

data class OtpRequestModel(
    val msg: String,
    val otp: Int,
    val status: Boolean

)