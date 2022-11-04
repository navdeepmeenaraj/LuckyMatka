package lucky.online.matka.app.ui.main.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import lucky.online.matka.app.web.PaPaDetails
import lucky.online.matka.app.web.model.DepositModel
import lucky.online.matka.app.databinding.ActivityPABinding
import lucky.online.matka.app.ui.main.viewmodel.MainViewModel
import lucky.online.matka.app.utils.BasicUtils
import lucky.online.matka.app.utils.BasicUtils.cool
import lucky.online.matka.app.utils.BasicUtils.hideKeyboard
import lucky.online.matka.app.utils.BasicUtils.showErrorSnackBar
import lucky.online.matka.app.utils.BasicUtils.showSuccessSnackBar
import lucky.online.matka.app.utils.BasicUtils.toast
import lucky.online.matka.app.utils.Status
import com.shreyaspatil.easyupipayment.EasyUpiPayment
import com.shreyaspatil.easyupipayment.exception.AppNotFoundException
import com.shreyaspatil.easyupipayment.listener.PaymentStatusListener
import com.shreyaspatil.easyupipayment.model.PaymentApp
import com.shreyaspatil.easyupipayment.model.TransactionDetails
import com.shreyaspatil.easyupipayment.model.TransactionStatus
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PActivity : AppCompatActivity(), PaymentStatusListener {

    private val viewmodel: MainViewModel by viewModels()

    private var _binding: ActivityPABinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPABinding.inflate(layoutInflater)
        setContentView(binding.root)
        initOnClick()
        observePaymentResponse()

    }


    private fun observePaymentResponse() {
        viewmodel.deposit.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    finish()
                    showSuccessSnackBar(it.data!!.message)
                }
                Status.ERROR -> {
                    finish()
                    showErrorSnackBar(it.message.toString())
                }
                Status.LOADING -> {
                    cool("Loading")
                }
            }
        })
    }


    private fun initOnClick() {
        binding.submitPoints.setOnClickListener {
            validate()
        }
    }

    private fun validate() {

        getPaymentDetails()?.observe(this, {

            val points = binding.pointsToAdd.text.toString()

            if (points.isEmpty()) {
                hideKeyboard(this)
                showErrorSnackBar("Please enter points")
                return@observe
            }

            if (points.toInt() < it.min_amount) {
                hideKeyboard(this)
                showErrorSnackBar("Minimum deposit ${it.min_amount}")
                return@observe

            }

            if (points.toInt() > it.max_amount) {
                hideKeyboard(this)
                showErrorSnackBar("Maximum deposit by UPI ${it.max_amount}")
                return@observe
            }

            hideKeyboard(this)
            pay(points)
        })


    }

    private fun pay(points: String) {

        getPaymentDetails()?.observe(this, {
            try {
                val easyUpiPayment = EasyUpiPayment(this) {
                    this.paymentApp = getPaymentApp()
                    this.payeeVpa = it.upi_id
                    this.payeeName = it.business_name
                    this.payeeMerchantCode = it.merchant_code
                    this.transactionId = "TID" + (System.currentTimeMillis() / 1000).toString()
                    this.transactionRefId = "TREF" + (System.currentTimeMillis() / 1000).toString()
                    this.description = it.payment_desc
                    this.amount = "$points.00"

                }
                easyUpiPayment.startPayment()
                easyUpiPayment.setPaymentStatusListener(this)
            } catch (e: AppNotFoundException) {
                toast(e.message.toString())
            }

        })
    }

    private fun getPaymentApp(): PaymentApp {
        return when {
            binding.phonepeButton.isChecked -> {
                PaymentApp.PHONE_PE
            }
            binding.paytmButton.isChecked -> {
                PaymentApp.PAYTM
            }

            binding.gpayButton.isChecked -> {
                PaymentApp.GOOGLE_PAY
            }

            binding.otherButton.isChecked -> {
                PaymentApp.ALL
            }

            else -> return PaymentApp.ALL
        }
    }


    override fun onTransactionCancelled() {
        showErrorSnackBar("Transaction Failed ! If Paid contact us on Whatsapp")
        binding.pointsToAdd.setText("")
    }

    override fun onTransactionCompleted(transactionDetails: TransactionDetails) {
        binding.pointsToAdd.setText("")
        if (transactionDetails.transactionStatus == TransactionStatus.SUCCESS) {
            viewmodel.addFunds(
                DepositModel(
                    BasicUtils.userId().toString(),
                    transactionDetails.amount.toString(),
                    transactionDetails.transactionId.toString(),
                    binding.paymentOptions.checkedRadioButtonId.toString(),
                )
            )
        }

    }


    private fun getPaymentDetails(): LiveData<PaPaDetails>? {
        return viewmodel.dbPaymentData(this)
    }

}