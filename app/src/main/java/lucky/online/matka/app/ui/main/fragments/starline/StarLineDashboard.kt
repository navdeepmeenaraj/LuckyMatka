package lucky.online.matka.app.ui.main.fragments.starline

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import lucky.online.matka.app.R
import lucky.online.matka.app.web.model.starline.StarlineMarkets
import lucky.online.matka.app.databinding.FragmentStarLineDashboardBinding
import lucky.online.matka.app.ui.main.adapters.StarlineMarketAdapter
import lucky.online.matka.app.ui.main.viewmodel.StarlineViewModel
import lucky.online.matka.app.utils.BasicUtils.bearerToken
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StarLineDashboard : Fragment() {
    private val starViewModel: StarlineViewModel by viewModels()
    private lateinit var _context: Context
    private val starMarketList: ArrayList<StarlineMarkets> = ArrayList()
    private var starMarketsAdapter: StarlineMarketAdapter? = null
    private lateinit var progressDialog: ProgressDialog

    private lateinit var binding: FragmentStarLineDashboardBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        _context = context
    }

    override fun onResume() {
        super.onResume()
        starViewModel.starMarkets.observe(this, Observer {
            if (it.data != null) {
                initRecyclerView(it.data)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStarLineDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(_context)
        setOnClick()
        observeStarMarketData()
        fetchStarLineMarketData()
    }

    private fun observeStarMarketData() {
        starViewModel.starMarkets.observe(viewLifecycleOwner, {
            if (it.data != null) {
                initRecyclerView(it.data)
            }
        })
    }

    private fun setOnClick() {
        binding.starWinHisButton.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_starLineDashboard_to_starLineWinHistory)
        }

        binding.starBidHisButton.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_starLineDashboard_to_starLineBetHistory)
        }

        binding.starlineRatesButton.setOnClickListener {
            MaterialDialog(_context, MaterialDialog.DEFAULT_BEHAVIOR).show {
                cornerRadius(20f)
                customView(R.layout.fragment_star_rates)
            }
        }
    }

    private fun fetchStarLineMarketData() {
        showProgressDialog()
        starViewModel.getStarMarkets(bearerToken())
    }

    private fun initRecyclerView(markets: List<StarlineMarkets>) {

        val recyclerView = binding.starlineRecylerview

        starMarketList.clear()
        starMarketsAdapter?.notifyDataSetChanged()
        starMarketList.addAll(markets)
        recyclerView.layoutManager = LinearLayoutManager(_context)
        starMarketsAdapter = StarlineMarketAdapter(starMarketList)
        recyclerView.adapter = starMarketsAdapter
        recyclerView.setHasFixedSize(false)
        progressDialog.dismiss()
    }

    private fun showProgressDialog() {
        progressDialog.show()
        progressDialog.setContentView(R.layout.custom_progress_dialog)
        progressDialog.window!!.setBackgroundDrawableResource(
            android.R.color.transparent
        )
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)

    }

}