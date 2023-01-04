package com.qikkle.barcodemapping.ui.fragments.addInventory

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.qikkle.barcodemapping.api.Resource
import com.qikkle.barcodemapping.databinding.FragmentAddInventoryBinding
import com.qikkle.barcodemapping.ui.BarcodeMappingActivity
import java.text.SimpleDateFormat
import java.util.*


class AddInventoryFragment: Fragment() {

    private var _binding: FragmentAddInventoryBinding? = null;
    private val binding get() = _binding!!
    private var calendar: Calendar = Calendar.getInstance()

    private lateinit var viewModel: AddInventoryViewModel

    private val date =
        OnDateSetListener { view, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            updateLabel()
        }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(p0: Editable?) {
            viewModel.onDataChange(
                binding.etDate.text.toString(),
                binding.etPoNumber.text.toString(),
                binding.etCategory.text.toString(),
                binding.etProduct.text.toString(),
                binding.etMake.text.toString(),
                binding.etReceiver.text.toString(),
                binding.etSerialNo.text.toString()
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddInventoryBinding.inflate(
            layoutInflater,
            container,
            false
        )

        viewModel = ViewModelProvider(this)[AddInventoryViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            etDate.addTextChangedListener(textWatcher)
            etPoNumber.addTextChangedListener(textWatcher)
            etCategory.addTextChangedListener(textWatcher)
            etProduct.addTextChangedListener(textWatcher)
            etMake.addTextChangedListener(textWatcher)
            etSerialNo.addTextChangedListener(textWatcher)
            etReceiver.addTextChangedListener(textWatcher)

            scanIcon.setOnClickListener{
                (requireActivity() as BarcodeMappingActivity).scanBarcode()
            }

            btnSave.setOnClickListener {
                viewModel.saveAsset(
                    binding.etDate.text.toString(),
                    binding.etPoNumber.text.toString(),
                    binding.etCategory.text.toString(),
                    binding.etProduct.text.toString(),
                    binding.etMake.text.toString(),
                    binding.etReceiver.text.toString(),
                    binding.etSerialNo.text.toString()
                )
            }

            calenderIcon.setOnClickListener{
                calendar = Calendar.getInstance()
                DatePickerDialog(
                    requireContext(),
                    date,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        }

        (requireActivity() as BarcodeMappingActivity).barcodeListener = object : BarcodeMappingActivity.BarcodeListener {
            override fun onScanSuccess(result: String) {
                binding.etSerialNo.setText(result)
            }

            override fun onScanFail() {
                binding.etSerialNo.setText("NA")
            }

        }

        viewModel.getSaveBtnStatus().observe(viewLifecycleOwner){ enable->
            binding.btnSave.isEnabled = enable
        }

        viewModel.getCreateAssetResult().observe(viewLifecycleOwner) { response->
            when(response){
                is Resource.Loading->{
                    showProgressBar()
                }
                is Resource.Error -> {
                    hideProgressBar()
                    Snackbar.make(
                        binding.root,
                        response.errorMessage!!,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        view.findNavController().popBackStack()
                    }
                }
            }
        }
    }

    private fun hideProgressBar() {
        binding.loading.root.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.loading.root.visibility = View.VISIBLE
    }

    private fun updateLabel() {
        val myFormat = "yyyy-MM-dd"
        val dateFormat = SimpleDateFormat(myFormat, Locale.US)
        binding.etDate.setText(dateFormat.format(calendar.time))
    }
}