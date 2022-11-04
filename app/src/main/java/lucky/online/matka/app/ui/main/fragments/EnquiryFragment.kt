package lucky.online.matka.app.ui.main.fragments

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import lucky.online.matka.app.databinding.FragmentEnquiryBinding
import lucky.online.matka.app.utils.Constants
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EnquiryFragment : Fragment() {

    private lateinit var progressDialog: ProgressDialog
    private lateinit var binding: FragmentEnquiryBinding
    private lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(mContext)
        progressDialog.setMessage(Constants.LOADING_MESSAGE)
        initButtonClick()

    }

    private fun initButtonClick() {
        binding.buttonSendEnquiry.setOnClickListener {
            progressDialog.show()
            Handler().postDelayed(
                {
                    progressDialog.dismiss()
                }, 3000
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEnquiryBinding.inflate(inflater, container, false)
        return binding.root
    }

}