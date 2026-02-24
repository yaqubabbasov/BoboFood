package com.yaqubabbasov.bobofood.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.airbnb.lottie.LottieDrawable
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.yaqubabbasov.bobofood.R
import com.yaqubabbasov.bobofood.data.entity.Yemekler
import com.yaqubabbasov.bobofood.databinding.FragmentHomeBinding
import com.yaqubabbasov.bobofood.ui.home.ProductAdapter

import com.yaqubabbasov.bobofood.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding?=null
    private val binding get() = _binding!!
    private val viewmodel : HomeViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
       _binding= FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.productrv.layoutManager=
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        val adapter = ProductAdapter(requireContext(), emptyList()) { yemek ->
            if (isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                try {
                    val action = HomeFragmentDirections.homdetbridge(yemek)
                    findNavController().navigate(action)
                } catch (e: Exception) {
                    Log.e("HomeFragment", "Navigation failed", e)
                }
            }
        }
        binding.productrv.post {
            binding.productrv.adapter = adapter
        }

        viewmodel.productlist.observe(viewLifecycleOwner){
            adapter.updateList(it)
        }

        imageslider()
        search()
        
        val foodlottie=binding.foodAnimation
        with(binding){
            foodlottie.setMinProgress(0.0f)
            foodlottie.setMaxProgress(1.0f)
            foodlottie.repeatMode= LottieDrawable.RESTART
            foodlottie.playAnimation()
        }
    }

    private fun search() {
        binding.searchview.setupWithSearchBar(binding.searchBar)

        val searchAdapter = ProductAdapter(requireContext(), emptyList()) { yemek ->
            if (isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                try {
                    val action = HomeFragmentDirections.homdetbridge(yemek)
                    findNavController().navigate(action)
                } catch (e: Exception) {
                    Log.e("HomeFragment", "Search navigation failed", e)
                }
            }
        }
        binding.searchResultsRv.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.searchResultsRv.adapter = searchAdapter

        binding.searchview.editText.addTextChangedListener { text ->
            val query = text?.toString()?.trim().orEmpty()
            if (query.isEmpty()) {
                binding.searchResultsRv.visibility = View.GONE
                searchAdapter.updateList(emptyList())
            }else{
                val filtered = viewmodel.productlist.value?.filter {
                    it.yemekAdi.contains(query, ignoreCase = true)
                } ?: emptyList()
                binding.searchResultsRv.visibility = View.VISIBLE
                searchAdapter.updateList(filtered)
            }
        }
    }

    fun imageslider(){
        val imagelist= ArrayList<SlideModel>()
        imagelist.add(SlideModel("https://dynamic-media-cdn.tripadvisor.com/media/photo-o/2c/60/d7/e4/kebab-fries-fried-chicken.jpg?w=1400&h=-1&s=1"))
        imagelist.add(SlideModel("https://d17wu0fn6x6rgz.cloudfront.net/img/w/tarif/mgt/firinda-sebzeli-kofte.webp"))
        imagelist.add(SlideModel("https://dynamic-media-cdn.tripadvisor.com/media/photo-o/21/c5/fd/83/1998-shaurma-for-every.jpg?w=1400&h=800&s=1"))
        binding.imageslider.setImageList(imagelist, ScaleTypes.CENTER_CROP)
    }

    override fun onResume() {
        super.onResume()
        viewmodel.productdown()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
