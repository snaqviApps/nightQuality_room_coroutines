package com.example.android.trackmysleepquality.sleepdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.databinding.FragmentSleepDetailBinding

/**
 * A simple [Fragment] subclass.
 */
class SleepDetailFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val bindingDetails: FragmentSleepDetailBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_sleep_detail, container, false)

        val applicationDetails = requireNotNull(this.activity).application
        val argumentsDetails = SleepDetailFragmentArgs.fromBundle(arguments!!)

        // Create an instance of the ViewModel Factory.
        val dataSourceDetails = SleepDatabase.getInstance(applicationDetails).sleepDatabaseDao
        val viewModelDetailFactory = SleepDetailsViewModelFactory(argumentsDetails.navNightQuality, dataSourceDetails)
        val sleepDetailViewModel = ViewModelProvider(this, viewModelDetailFactory)
                .get(SleepDetailViewModel::class.java)

//        val sleepDetailViewModel =                                                    // this works too
//                ViewModelProviders.of(
//                        this, viewModelDetailFactory).get(SleepDetailViewModel::class.java)

        bindingDetails.sleepDetailViewModelV= sleepDetailViewModel
        bindingDetails.setLifecycleOwner(this)

        // Add an Observer to the state variable for Navigating when a Quality icon is tapped.
        sleepDetailViewModel.navigateToSleepTracker.observe(this.viewLifecycleOwner, Observer {
            if(it == true){
                findNavController().navigate(
                        SleepDetailFragmentDirections.actionSleepDetailFragmentToSleepTrackerFragment())
                sleepDetailViewModel.doneNavigating()
            }

        })

//        return inflater.inflate(R.layout.fragment_sleep_detail, container, false)   // default
        return bindingDetails.root
    }

}

