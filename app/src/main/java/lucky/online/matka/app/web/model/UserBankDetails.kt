package lucky.online.matka.app.web.model

data class UserBankDetails(
    val error: Boolean,
    val gpay: String,
    val message: String,
    val paytm: String,
    val phonepe: String,
    val status: Boolean
)