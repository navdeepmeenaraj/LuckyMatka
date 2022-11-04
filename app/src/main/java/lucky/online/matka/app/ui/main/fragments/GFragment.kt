package lucky.online.matka.app.ui.main.fragments

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.gson.JsonObject
import com.koushikdutta.ion.Ion
import lucky.online.matka.app.ui.main.viewmodel.PViewModel
import lucky.online.matka.app.utils.BasicUtils.hideSoftKeyboard
import lucky.online.matka.app.utils.BasicUtils.showErrorSnackBar
import lucky.online.matka.app.utils.BasicUtils.toast
import lucky.online.matka.app.utils.Constants
import lucky.online.matka.app.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import lucky.online.matka.app.utils.BasicUtils
import lucky.online.matka.app.utils.BasicUtils.cool
import lucky.online.matka.app.databinding.FragmentGPAyBinding

@AndroidEntryPoint
class GFragment : Fragment() {

    private val viewmodel: PViewModel by viewModels()
    private lateinit var mView: View
    private lateinit var mContext: Context
    private lateinit var progressDialog: ProgressDialog
    private lateinit var binding: FragmentGPAyBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
        progressDialog = ProgressDialog(mContext)
        progressDialog.setMessage(Constants.LOADING_MESSAGE)
        observePaymentDetailUpdateResponse()
        initButtons()
        getGpayDetails()
    }

    private fun initButtons() {
        binding.btnSubmitGpay.setOnClickListener {
            val number = binding.editTextGpay.text.toString()
            if (number.length < 10) {
                requireActivity().showErrorSnackBar("Enter Valid Number")
                return@setOnClickListener
            }
            if (number.isEmpty()) {
                requireActivity().showErrorSnackBar("Enter Number")
                return@setOnClickListener
            }

            updatePaymentDetails(3, number)
        }
    }

    private fun getGpayDetails() {
        val json = JsonObject()
        json.addProperty("user_id", BasicUtils.userId())

        Ion.with(context)
            .load(Constants.SERVER_URL + "gpay_number")
            .setJsonObjectBody(json)
            .asJsonObject()
            .setCallback { e, result ->
                if (e != null) {
                    binding.editTextGpay.setText("")
                } else {
                    val isSuccess = result["success"].asBoolean
                    if (isSuccess) {
                        try {
                            val paytmNumber = result["gpay"].toString().removeRange(0, 1)
                            val paytmNumber2 = paytmNumber.removeRange(10, 11)
                            binding.editTextGpay.setText(paytmNumber2)

                        } catch (e: Exception) {
                            cool("error : ${e.message.toString()}")
                        }
                    } else {
                        binding.editTextGpay.setText("")
                    }

                }
            }
    }

    private fun observePaymentDetailUpdateResponse() {
        viewmodel.paymentDetails.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dismiss()
                    it.data?.let { data ->
                        mContext.toast(data.message)
                    }
                }
                Status.LOADING -> {
                    progressDialog.show()
                }
                Status.ERROR -> {
                    progressDialog.dismiss()
                    requireActivity().hideSoftKeyboard(mView)
                    mContext.toast(it.message.toString())

                }
            }
        }

    }

    private fun updatePaymentDetails(paymentType: Int, paymentNumber: String) {
        viewmodel.updatePaymentDetails(paymentType, paymentNumber)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGPAyBinding.inflate(inflater, container, false)
        return binding.root
    }

}