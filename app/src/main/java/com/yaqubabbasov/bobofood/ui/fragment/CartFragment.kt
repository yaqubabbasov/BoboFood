package com.yaqubabbasov.bobofood.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.yaqubabbasov.bobofood.MainActivity
import com.yaqubabbasov.bobofood.R
import com.yaqubabbasov.bobofood.databinding.FragmentCartBinding
import com.yaqubabbasov.bobofood.ui.adapter.BasketAdapter
import com.yaqubabbasov.bobofood.ui.viewmodel.CartViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private val viewmodel: CartViewmodel by viewModels ()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_cart,container, false)
        binding.cartfr=this

            val adapter= BasketAdapter(requireContext(),emptyList(),viewmodel)
           binding.cartadapt=adapter
        binding.cartconstraint.isVisible = false
        binding.emptyview.isVisible = false

        viewmodel.basketLive.observe(viewLifecycleOwner){list->

            val isEmpty = list.isNullOrEmpty()

            binding.cartconstraint.isVisible = !isEmpty
            binding.emptyview.isVisible = isEmpty

            if (!isEmpty) {
                adapter.updatelist(list)
                viewmodel.calculateTotal()
            }
        }
        viewmodel.totalPrice.observe(viewLifecycleOwner){
            binding.cartcost.text="$it ₺"

        }


        return binding.root

    }



    override fun onResume() {
        super.onResume()
        viewmodel.getcatuser("jacob")
    }




}