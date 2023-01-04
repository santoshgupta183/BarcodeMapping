package com.qikkle.barcodemapping.ui.fragments.addInventory

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private val categories = mutableListOf<String>()
    private val products = mutableListOf<String>()
    private val receivers = mutableListOf<String>()

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
                binding.etSerialNo.text.toString(),
                binding.etQuantity.text.toString()
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
            etQuantity.addTextChangedListener(textWatcher)

            scanIcon.setOnClickListener{
                (requireActivity() as BarcodeMappingActivity).scanBarcode()
            }

            btnSave.setOnClickListener {
                viewModel.saveAsset(
                    binding.etDate.text.toString(),
                    binding.etPoNumber.text.toString(),
                    binding.etMake.text.toString(),
                    binding.etSerialNo.text.toString(),
                    binding.etQuantity.text.toString()
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

            etCategory.setOnClickListener {
                showCategoriesDialog()
            }

            etProduct.setOnClickListener {
                showProductsDialog()
            }

            etReceiver.setOnClickListener {
                showReceiversDialog()
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

        viewModel.getCategoriesLiveData().observe(viewLifecycleOwner) { resource->
            when(resource){
                is Resource.Loading->{
                    showProgressBar()
                }
                is Resource.Error -> {
                    hideProgressBar()
                    Snackbar.make(
                        binding.root,
                        resource.errorMessage!!,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                is Resource.Success -> {
                    hideProgressBar()
                    resource.data?.let {
                        categories.clear()
                        for (category in resource.data){
                            categories.add(category.category)
                        }
                    }
                }
            }
        }

        viewModel.getProductsLiveData().observe(viewLifecycleOwner) { resource->
            when(resource){
                is Resource.Loading->{
                    showProgressBar()
                }
                is Resource.Error -> {
                    hideProgressBar()
                    Snackbar.make(
                        binding.root,
                        resource.errorMessage!!,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                is Resource.Success -> {
                    hideProgressBar()
                    resource.data?.let {
                        products.clear()
                        for (product in resource.data){
                            products.add(product.product)
                        }
                    }
                }
            }
        }

        viewModel.getReceiversLiveData().observe(viewLifecycleOwner) { resource->
            when(resource){
                is Resource.Loading->{
                    showProgressBar()
                }
                is Resource.Error -> {
                    hideProgressBar()
                    Snackbar.make(
                        binding.root,
                        resource.errorMessage!!,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                is Resource.Success -> {
                    hideProgressBar()
                    resource.data?.let {
                        receivers.clear()
                        for (receiver in resource.data){
                            receivers.add("${receiver.firstName} ${receiver.lastName}")
                        }
                    }
                }
            }
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

    private fun showCategoriesDialog(){
        AlertDialog.Builder(requireContext())
            .setTitle("Select Category")
            .setItems(categories.toTypedArray()) { dialogInterface, index ->
                binding.etCategory.setText(categories[index])
                viewModel.onCategorySelected(categories[index], index)
            }
            .create()
            .show()
    }

    private fun showProductsDialog(){
        AlertDialog.Builder(requireContext())
            .setTitle("Select Product")
            .setItems(products.toTypedArray()) { dialogInterface, index ->
                binding.etProduct.setText(products[index])
                viewModel.onProductSelected(products[index], index)
            }
            .create()
            .show()
    }

    private fun showReceiversDialog(){
        AlertDialog.Builder(requireContext())
            .setTitle("Select Receiver")
            .setItems(receivers.toTypedArray()) { dialogInterface, index ->
                binding.etReceiver.setText(receivers[index])
                viewModel.onReceiverSelected(receivers[index], index)
            }
            .create()
            .show()
    }
}