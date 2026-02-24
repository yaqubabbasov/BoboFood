package com.yaqubabbasov.bobofood.ui.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.yaqubabbasov.bobofood.databinding.FragmentFavouriteBinding
import com.yaqubabbasov.bobofood.ui.favourite.FavouriteAdapter
import com.yaqubabbasov.bobofood.ui.favourite.FavouriteViewModel
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
        val adapter = FavouriteAdapter(requireContext(), emptyList(), viewmodel)
        binding.favouriteRv.adapter = adapter
        binding.favouriteConstraint.isVisible=false
        binding.emptyview.isVisible=false
        binding.favouriteRv.layoutManager = LinearLayoutManager(requireContext())
        viewmodel.mutablegfoodlist.observe(viewLifecycleOwner) {list->
            val isempty= list.isNullOrEmpty()
            binding.favouriteConstraint.isVisible=!isempty
            binding.emptyview.isVisible=isempty
            adapter.updatelist(list)
        }
    }

    override fun onResume() {
        super.onResume()
        viewmodel.getAllFavourites()

    }


}