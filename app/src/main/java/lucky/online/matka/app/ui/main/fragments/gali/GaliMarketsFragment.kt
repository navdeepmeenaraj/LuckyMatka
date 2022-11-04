package lucky.online.matka.app.ui.main.fragments.gali

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import lucky.online.matka.app.R
import lucky.online.matka.app.web.model.gali.GaliMarkets
import lucky.online.matka.app.databinding.FragmentGaliMarketsBinding
import lucky.online.matka.app.ui.main.adapters.GaliMarketsAdapter
import lucky.online.matka.app.ui.main.viewmodel.GaliViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GaliMarketsFragment : Fragment() {

    private lateinit var binding: FragmentGaliMarketsBinding
    private val marketList: ArrayList<GaliMarkets> = ArrayList()
    private var marketAdapter: GaliMarketsAdapter? = null
    private val viewModel: GaliViewModel by viewModels()

    private lateinit var _context: Context
    override fun onAttach(context: Context) {
        super.onAttach(context)
        _context = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGaliMarketsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeGaliMarketData()
        setOnClick()
        getGaliMarkets()
    }

    private fun observeGaliMarketData() {
        viewModel.galis.observe(viewLifecycleOwner, {
            if (it.data != null) {
                initRecyclerView(it.data)
            }
        })
    }

    private fun setOnClick() {
        binding.btnBidsGali.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_galiMarketsFragment_to_galiBids)
        }

        binding.btnWinsGali.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_galiMarketsFragment_to_galiWins)
        }
    }

    private fun getGaliMarkets() {
        binding.progress.visibility = View.VISIBLE
        viewModel.getGaliMarkets()
    }

    private fun initRecyclerView(gali: List<GaliMarkets>) {
        val recyclerView = binding.galiRecView
        marketList.clear()
        marketAdapter?.notifyDataSetChanged()
        marketList.addAll(gali)
        recyclerView.layoutManager = LinearLayoutManager(_context)
        marketAdapter = GaliMarketsAdapter(marketList)
        recyclerView.adapter = marketAdapter
        recyclerView.setHasFixedSize(false)
        binding.progress.visibility = View.GONE
    }
}