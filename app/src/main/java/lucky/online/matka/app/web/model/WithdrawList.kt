package lucky.online.matka.app.web.model

data class WithdrawList(
    val id: String,
    val request_amount: String,
    val created_at: String,
    val request_message: String,
    val payment_number: String,
    val request_status: String
)
