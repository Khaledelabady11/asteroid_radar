package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.Network.AsteroidApiFilter
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        val app = requireNotNull(this.activity).application
        val viewModelFactory = MainViewModelFactory(app)
        ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
    }

    private lateinit var asteroidListAdapter: MainAsteroidAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        asteroidListAdapter = MainAsteroidAdapter(OnClickListener {
            viewModel.showAsteroidDetails(it)
        })
        binding.asteroidRecycler.adapter = asteroidListAdapter

        viewModel.goToDetails.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.showAsteroidDetailsComplete()
            }
        })
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.asteroidListing.observe(viewLifecycleOwner) { asteroid ->
            asteroid.apply {
                asteroidListAdapter.submitList(this)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.changeFilter(when (item.itemId) {
            R.id.show_rent_menu -> AsteroidApiFilter.WEEK
            R.id.show_buy_menu -> AsteroidApiFilter.TODAY
            else -> AsteroidApiFilter.SAVED
        })
        return true
    }
}
