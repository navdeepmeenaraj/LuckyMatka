package lucky.online.matka.app.ui.main.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import lucky.online.matka.app.R
import lucky.online.matka.app.web.model.MarketData
import lucky.online.matka.app.utils.BasicUtils.checkIfUserIsVerified
import lucky.online.matka.app.utils.BasicUtils.lastDigit
import lucky.online.matka.app.utils.BasicUtils.sumOfDigits
import lucky.online.matka.app.utils.BasicUtils.toDate
import lucky.online.matka.app.utils.Constants.PREFS_CHART_ID
import lucky.online.matka.app.utils.Constants.PREFS_CHART_TITLE
import lucky.online.matka.app.utils.Constants.PREFS_MARKET_ID
import lucky.online.matka.app.utils.Constants.PREFS_MARKET_NAME
import com.pixplicity.easyprefs.library.Prefs
import lucky.online.matka.app.utils.BasicUtils.toast
import kotlinx.android.synthetic.main.item_market_list.view.*

class MarketsAdapter(private var markets: List<MarketData>) :
    RecyclerView.Adapter<MarketsAdapter.MarketViewHolder>() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MarketViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_market_list_2, viewGroup, false)
        return MarketViewHolder(view)
    }

    override fun getItemCount() = markets.size

    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {
        holder.bind(markets[position])
    }

    class MarketViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val marketName = view.market_name
        private val closeResult = view.close_result
        private val openResult = view.open_result
        private val openPanel = view.open_panel
        private val closePanel = view.close_panel
        private val marketOpenTime = view.market_open_time
        private val marketCloseTime = view.market_close_time
        private val playButton = view.play_button
        private val marketChart = view.chart
//        private val marketStatus = view.market_status

        @SuppressLint("SetTextI18n")
        fun bind(markets: MarketData) {
            marketName.text = markets.market_name
            openPanel.text = markets.open_pana
            closePanel.text = markets.close_pana

            if (!checkIfUserIsVerified()) {
                playButton.visibility = View.GONE
//                marketStatus.visibility = View.GONE
            }

            if (markets.market_status == 0) {
                playButton.setBackgroundResource(R.drawable.lucky_btn_play_red)
//                marketStatus.setTextColor(Color.parseColor("#ff3838"))
//                marketStatus.text = "Closed"
                playButton.setOnClickListener {
//                    YoYo.with(Techniques.Shake)
//                        .duration(500)
//                        .playOn(marketStatus)
                    val vibe = it.context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    vibe.vibrate(100)

                    it.context.toast("Market is Closed")
                }
            } else {
                playButton.setBackgroundResource(R.drawable.lucky_btn_play_green)
//                marketStatus.setTextColor(Color.parseColor("#2ed573"))
//                marketStatus.text = "Running"
                playButton.setOnClickListener { view ->
                    handleButtonOnClick(markets.market_id, markets.market_name, view)
                }

            }

            try {
                openResult.text = if (markets.open_pana != "***") {
                    lastDigit(sumOfDigits(markets.open_pana)).toString()
                } else {
                    "*"
                }

                closeResult.text = if (markets.close_pana != "***") {
                    lastDigit(sumOfDigits(markets.close_pana)).toString()
                } else {
                    "*"
                }

            } catch (e: NumberFormatException) {
                throw e
            }


            marketChart.setOnClickListener {
                showMarketChart(markets.market_id, markets.market_name, it)
            }

            marketOpenTime.text = "Open: " + toDate(markets.market_open_time)
            marketCloseTime.text = "Close: " + toDate(markets.market_close_time)
        }

        private fun showMarketChart(marketId: Int, marketName: String, view: View) {
            Prefs.putInt(PREFS_CHART_ID, marketId)
            Prefs.putString(PREFS_CHART_TITLE, marketName)
            Navigation.findNavController(view)
                .navigate(R.id.chartFragment)
        }

        private fun handleButtonOnClick(marketId: Int, marketName: String, view: View) {
            Prefs.putInt(PREFS_MARKET_ID, marketId)
            Prefs.putString(PREFS_MARKET_NAME, marketName)
            Navigation.findNavController(view)
                .navigate(R.id.betTypeFragment)
        }
    }
}