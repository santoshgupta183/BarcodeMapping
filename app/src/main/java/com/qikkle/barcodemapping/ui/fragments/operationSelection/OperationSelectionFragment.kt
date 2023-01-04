package com.qikkle.barcodemapping.ui.fragments.operationSelection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.qikkle.barcodemapping.databinding.FragmentOperationSelectionBinding

class OperationSelectionFragment: Fragment() {

    private var _binding: FragmentOperationSelectionBinding? = null;
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOperationSelectionBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cvAddInventory.setOnClickListener {
            val action =
                OperationSelectionFragmentDirections.actionOperationSelectionFragmentToAddInventoryFragment()
            view.findNavController().navigate(action)
        }

        binding.cvSearchProduct.setOnClickListener {
            val action =
                OperationSelectionFragmentDirections.actionOperationSelectionFragmentToSearchProductFragment()
            view.findNavController().navigate(action)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}