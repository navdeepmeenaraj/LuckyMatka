package lucky.online.matka.app.web.model.gali

data class GaliMarkets(
    val gali_name: String,
    val id: Int,
    val is_closed: Int,
    val open_time: String,
    val result: String
)