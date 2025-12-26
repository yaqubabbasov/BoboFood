package com.yaqubabbasov.bobofood.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.yaqubabbasov.bobofood.R
import com.yaqubabbasov.bobofood.databinding.FragmentFavouriteBinding
import com.yaqubabbasov.bobofood.ui.adapter.FavouriteAdapter
import com.yaqubabbasov.bobofood.ui.viewmodel.FavouriteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteFragment : Fragment() {
    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!
    private val viewmodel: FavouriteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = FavouriteAdapter(requireContext(), emptyList())
        binding.favouriteRv.adapter = adapter
        binding.favouriteRv.layoutManager = LinearLayoutManager(requireContext())
        viewmodel.mutablegfoodlist.observe(viewLifecycleOwner) {
            adapter.updatelist(it)
        }
    }

    override fun onResume() {
        super.onResume()
        viewmodel.getAllFavourites()

    }


}