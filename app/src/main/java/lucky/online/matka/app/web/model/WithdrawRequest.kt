package lucky.online.matka.app.web.model

data class WithdrawRequest(
    val user_id: Int,
    val amount: String,
    val number: String,

    )
