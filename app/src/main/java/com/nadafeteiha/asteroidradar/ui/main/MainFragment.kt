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
import com.nadafeteiha.asteroidradar.util.snack


class MainFragment : Fragment(), AsteroidAdapter.OnClickListener {

    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: AsteroidAdapter
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
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

        viewModel.asteroidsList.observe(viewLifecycleOwner) { asteroids ->
            if (asteroids != null) {
                adapter.submitList(asteroids)
            }
        }
        viewModel.status.observe(viewLifecycleOwner) { displayStatus ->
            if (viewModel.showEvent.value == true)
                when (displayStatus) {
                    ApiStatus.LOADING -> {
                        requireView().snack(resources.getString(R.string.updating_data))
                    }
                    ApiStatus.ERROR -> {
                        requireView().snack(resources.getString(R.string.Error_data))
                        viewModel.doneShowEvent()
                    }
                    else -> {
                        requireView().snack(resources.getString(R.string.done_data))
                        viewModel.doneShowEvent()
                    }
                }
        }

        setHasOptionsMenu(true)

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_today_menu -> {
                viewModel.showTodayAsteroid()
            }
            R.id.show_next_week -> {
                viewModel.showNextWeekAsteroid()
            }
            R.id.show_all_asteroids_menu -> {
                viewModel.showSavedAsteroid()
            }
        }
        return true
    }

    override fun onClick(item: Asteroid) {
        findNavController().navigate(MainFragmentDirections.actionShowDetail(item))
    }
}
