package lucky.online.matka.app.ui.main.fragments.starline

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import lucky.online.matka.app.R
import lucky.online.matka.app.web.model.starline.StarlineBids
import lucky.online.matka.app.ui.main.adapters.StarLineBidHisAdapter
import lucky.online.matka.app.ui.main.viewmodel.StarlineViewModel
import lucky.online.matka.app.utils.BasicUtils.showErrorSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StarLineBidHistory : Fragment(R.layout.fragment_star_line_bet_history) {
    private val vm: StarlineViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private val bidListStar: ArrayList<StarlineBids> = ArrayList()
    private var bidHistoryAdapterStar: StarLineBidHisAdapter? = null
    private lateinit var _context: Context
    override fun onAttach(context: Context) {
        super.onAttach(context)
        _context = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setType(view)
        vm.fetchStarBidHistory()
        observeBidHistory()

    }

    private fun observeBidHistory() {
        vm.bids.observe(viewLifecycleOwner, {
            if (it.data != null) {
                if (it.data.isEmpty()) {
                    requireActivity().showErrorSnackBar("No Bid History Found !")
                } else initRecyclerViewWin(it.data)
            }
        })
    }

    private fun setType(view: View) {
        view.apply {
            recyclerView = findViewById(R.id.star_bids_his)
        }
    }

    private fun initRecyclerViewWin(data: List<StarlineBids>) {
        bidListStar.clear()
        bidListStar.addAll(data)
        bidHistoryAdapterStar?.notifyDataSetChanged()
        recyclerView.layoutManager = LinearLayoutManager(_context)
        bidHistoryAdapterStar = StarLineBidHisAdapter(bidListStar)
        recyclerView.adapter = bidHistoryAdapterStar
        recyclerView.setHasFixedSize(false)
    }

}