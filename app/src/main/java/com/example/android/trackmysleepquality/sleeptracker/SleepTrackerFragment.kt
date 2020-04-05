
package com.example.android.trackmysleepquality.sleeptracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.databinding.FragmentSleepTrackerBinding
import com.example.android.trackmysleepquality.view.SleepNightAdapter
import com.example.android.trackmysleepquality.view.SleepNightListener
import com.google.android.material.snackbar.Snackbar
import java.util.*

class SleepTrackerFragment : Fragment() {
    /**
     * Called when the Fragment is ready to display content to the screen.
     * This function uses DataBindingUtil to inflate R.layout.fragment_sleep_quality.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentSleepTrackerBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_sleep_tracker, container, false)

        val applicationTracker = requireNotNull(this.activity).application
        val dataSource = SleepDatabase.getInstance(applicationTracker).sleepDatabaseDao
        val viewModelFactory = SleepTrackerViewModelFactory(dataSource, applicationTracker)
        val sleepTrackerViewModel =  ViewModelProvider(this, viewModelFactory)
                .get(SleepTrackerViewModel::class.java)
        binding.sleepTrackerViewModelV = sleepTrackerViewModel
        binding.lifecycleOwner = this.viewLifecycleOwner

        val adapter = SleepNightAdapter(SleepNightListener { nightId ->                         /** handle clicks to views in recyclerView */
            nightId.let {
                sleepTrackerViewModel.onSleepNightClicked(nightId)
            }
        })
        binding.sleepList.adapter = adapter

        /** Define GridLayoutManager */
        val gridManager =  GridLayoutManager(activity, 3)
        gridManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
//                TODO("Not yet implemented")
                return when (position) {
                    0 -> 3
                    else -> 1
                }

            }

        }
        binding.sleepList.layoutManager = gridManager

        sleepTrackerViewModel.nights.observe(this.viewLifecycleOwner, Observer {
            it?.let {
//                adapterUI.submitList(it)   // Submits a new list to be diffed, and displayed.
                adapter.addHeaderAndSubmitList(it)
            }
        })

        /** navigate to the SleepQuality Fragment */
            sleepTrackerViewModel.navigateToSleepQuality.observe(this.viewLifecycleOwner, Observer { nightNav ->
            nightNav?.let {
                val actionQuality = SleepTrackerFragmentDirections
                        .actionSleepTrackerFragmentToSleepQualityFragment(nightNav.nightId)
                findNavController().navigate(actionQuality)
                sleepTrackerViewModel.doneNavigating()
            }
        })

//      /** navigate to the SleepQualityDetails Fragment */
        sleepTrackerViewModel.navigateToSleepQualityDetails.observe(this.viewLifecycleOwner, Observer { nightDetails ->
            nightDetails?.let {
                findNavController().navigate(SleepTrackerFragmentDirections
                        .actionSleepTrackerFragmentToSleepDetailFragment(nightDetails))
                sleepTrackerViewModel.onSleepQualityDetailsNavigated()
            }
        })

        sleepTrackerViewModel.showSnackbar.observe(this.viewLifecycleOwner, Observer {
            if(it == true){
                Snackbar.make(activity!!.findViewById(android.R.id.content),
                getString(R.string.cleared_message),
                Snackbar.LENGTH_SHORT // How long to display the message.
                ).show()
                sleepTrackerViewModel.doneShowingSnackBar()
            }
        })
        return binding.root
    }
}
