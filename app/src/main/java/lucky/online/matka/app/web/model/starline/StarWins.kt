package lucky.online.matka.app.web.model.starline

data class StarWins(
    val bet_amount: Int,
    val bet_date: String,
    val bet_digit: Int,
    val bet_rate: Int,
    val bet_type: Int,
    val id: Int,
    val is_win: Int,
    val market_id: Int,
    val user_id: Int
)