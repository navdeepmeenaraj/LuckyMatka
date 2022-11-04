package lucky.online.matka.app.ui.main.fragments.history

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import lucky.online.matka.app.web.model.WinHistory
import lucky.online.matka.app.databinding.FragmentWinHistoryBinding
import lucky.online.matka.app.ui.main.adapters.WinHistoryAdapter
import lucky.online.matka.app.ui.main.viewmodel.SecondViewModel
import lucky.online.matka.app.utils.BasicUtils
import lucky.online.matka.app.utils.BasicUtils.showErrorSnackBar
import lucky.online.matka.app.utils.Constants.FULL_DATE_FORMAT
import lucky.online.matka.app.utils.Constants.LOADING_MESSAGE
import lucky.online.matka.app.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class WinHistory : Fragment() {
    private val secondViewModel: SecondViewModel by viewModels()
    private lateinit var binding: FragmentWinHistoryBinding
    private lateinit var startCalendar: Calendar
    private lateinit var endCalendar: Calendar
    private lateinit var mContext: Context
    private lateinit var alertDialog: ProgressDialog

    //RecyclerView
    private val betList: ArrayList<WinHistory> = ArrayList()

    private var betHistoryAdapter: WinHistoryAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startCalendar = Calendar.getInstance()
        endCalendar = Calendar.getInstance()
        alertDialog = ProgressDialog(mContext)
        alertDialog.setMessage(LOADING_MESSAGE)
        initDateEditText()
        initDateSelector()
        initOnClick()
        observeBidHistoryData()

    }

    private fun observeBidHistoryData() {
        secondViewModel.wins.observe(viewLifecycleOwner, {
            if (it.status == Status.LOADING) {
                alertDialog.show()
            } else if (it.status == Status.SUCCESS) {
                alertDialog.dismiss()
                if (it.data!!.isEmpty()) {
                    requireActivity().showErrorSnackBar("No Win History Found")
                } else
                    initRecyclerView(it.data)
            } else if (it.status == Status.ERROR) {
                alertDialog.dismiss()
                requireActivity().showErrorSnackBar("Try Again Later !")
            }
        })
    }

    private fun initOnClick() {
        binding.buttonSubmit.setOnClickListener {
            val fromDate = binding.fromDate.text.toString()
            val toDate = binding.toDate.text.toString()
            if (fromDate == toDate) {
                requireActivity().showErrorSnackBar("Please select lower date from End Date")
                return@setOnClickListener
            }
            fetchBidHistory(fromDate, toDate)
        }
    }

    private fun fetchBidHistory(fromDate: String, toDate: String) {
        secondViewModel.fetchWinHistory(
            BasicUtils.bearerToken(),
            BasicUtils.userId(),
            from = fromDate,
            to = toDate
        )
    }

    private fun initDateSelector() {

        val day = startCalendar.get(Calendar.DAY_OF_MONTH)
        startCalendar.set(Calendar.DAY_OF_MONTH, day - 1)
        val format1 = SimpleDateFormat(FULL_DATE_FORMAT, Locale.ENGLISH)
        val fromDate1 = format1.format(startCalendar.time)
        binding.fromDate.setText(fromDate1)

        binding.fromDate.setOnClickListener {
            val date =
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    startCalendar.set(Calendar.YEAR, year)
                    startCalendar.set(Calendar.MONTH, monthOfYear)
                    startCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    BasicUtils.cool("Date : ${startCalendar.time}")
                    val format = SimpleDateFormat(FULL_DATE_FORMAT, Locale.ENGLISH)
                    val fromDate = format.format(startCalendar.time)
                    binding.fromDate.setText(fromDate)


                }

            val datePickerDialog = DatePickerDialog(
                mContext, date,
                startCalendar.get(Calendar.YEAR),
                startCalendar.get(Calendar.MONTH),
                startCalendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.maxDate = Date().time
            datePickerDialog.show()
        }

        binding.toDate.setOnClickListener {
            val date =
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    endCalendar.set(Calendar.YEAR, year)
                    endCalendar.set(Calendar.MONTH, monthOfYear)
                    endCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    BasicUtils.cool("Date : ${endCalendar.time}")
                    val format = SimpleDateFormat(FULL_DATE_FORMAT, Locale.ENGLISH)
                    val toDate = format.format(endCalendar.time)
                    binding.toDate.setText(toDate)

                }

            val datePickerDialog = DatePickerDialog(
                mContext, date,
                endCalendar.get(Calendar.YEAR),
                endCalendar.get(Calendar.MONTH),
                endCalendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.maxDate = Date().time
            datePickerDialog.show()
        }
    }

    private fun initRecyclerView(data: List<WinHistory>) {
        binding.recyclerView.visibility = View.VISIBLE
        betList.clear()
        betList.addAll(data)
        betHistoryAdapter?.notifyDataSetChanged()
        binding.recyclerView.layoutManager = LinearLayoutManager(mContext)
        betHistoryAdapter = WinHistoryAdapter(betList)
        binding.recyclerView.adapter = betHistoryAdapter
        binding.recyclerView.setHasFixedSize(false)
    }


    private fun initDateEditText() {
        binding.toDate.setText(BasicUtils.getDateForHistory())
        binding.fromDate.setText(BasicUtils.getDateForHistory())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWinHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

}