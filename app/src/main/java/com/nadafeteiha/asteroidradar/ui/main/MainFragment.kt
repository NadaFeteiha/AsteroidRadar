package com.nadafeteiha.asteroidradar.ui.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.nadafeteiha.asteroidradar.R
import com.nadafeteiha.asteroidradar.domain.Asteroid
import com.nadafeteiha.asteroidradar.databinding.FragmentMainBinding
import com.nadafeteiha.asteroidradar.ui.AsteroidAdapter


class MainFragment : Fragment(), AsteroidAdapter.OnClickListener {

    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: AsteroidAdapter
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(
            this,
            MainViewModel.Factory(requireActivity().application)
        )[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        //initialize recyclerview and add adapter
        binding.asteroidRecycler.layoutManager = GridLayoutManager(context, 1)
        adapter = AsteroidAdapter(this)
        binding.asteroidRecycler.adapter = adapter

        setHasOptionsMenu(true)

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
        }
        return true
    }

    override fun onClick(item: Asteroid) {
        findNavController().navigate(MainFragmentDirections.actionShowDetail(item))
    }
}
