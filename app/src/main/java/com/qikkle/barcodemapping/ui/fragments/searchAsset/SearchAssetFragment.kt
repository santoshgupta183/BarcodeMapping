package com.qikkle.barcodemapping.ui.fragments.searchAsset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.qikkle.barcodemapping.api.Resource
import com.qikkle.barcodemapping.databinding.FragmentSearchProductBinding
import com.qikkle.barcodemapping.ui.BarcodeMappingActivity

class SearchAssetFragment: Fragment() {

    private var _binding: FragmentSearchProductBinding? = null;
    private val binding get() = _binding!!

    private lateinit var viewModel: SearchAssetViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchProductBinding.inflate(
            layoutInflater,
            container,
            false
        )

        viewModel = ViewModelProvider(this)[SearchAssetViewModel::class.java]

        (requireActivity() as BarcodeMappingActivity).barcodeListener = object : BarcodeMappingActivity.BarcodeListener {
            override fun onScanSuccess(result: String) {
                binding.tvSerialNo.setText(result)
                viewModel.getBarcodeDetails(barcode = result)
            }

            override fun onScanFail() {
                binding.tvSerialNo.setText("NA")
            }

        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSearchResult().observe(viewLifecycleOwner) { response->
            when(response){
                is Resource.Loading->{
                    showProgressBar()
                }
                is Resource.Error -> {
                    hideProgressBar()
                    Snackbar.make(
                        binding.root,
                        response.errorMessage!!,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Resource.Success -> {
                    response.data?.let { barcodeDetailsResponse ->
                        if (barcodeDetailsResponse.isNotEmpty()){
                            val barcodeDetails = barcodeDetailsResponse[0]
                            binding.tvDate.text = barcodeDetails.deliveryDate
                            binding.tvPoNumber.text = barcodeDetails.pONo
                            binding.tvCategory.text = barcodeDetails.category
                            binding.tvProduct.text = barcodeDetails.product
                            binding.tvMake.text = barcodeDetails.make
                            binding.tvSerialNo.text = barcodeDetails.serialNo
                        } else {
                            Snackbar.make(
                                binding.root,
                                "Error fetching data!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    hideProgressBar()
                }
            }
        }

        binding.scanIcon.setOnClickListener{
            (requireActivity() as BarcodeMappingActivity).scanBarcode()
        }
    }

    private fun hideProgressBar() {
        binding.loading.root.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.loading.root.visibility = View.VISIBLE
    }
}