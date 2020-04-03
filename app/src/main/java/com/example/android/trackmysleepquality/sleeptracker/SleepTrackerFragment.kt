/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.sleeptracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.databinding.FragmentSleepTrackerBinding
import com.google.android.material.snackbar.Snackbar

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
        binding.sleepTrackerViewModelXML = sleepTrackerViewModel
        binding.lifecycleOwner = this.viewLifecycleOwner

        val adapterUI = SleepNightAdapter(SleepNightListener { nightId ->                         /** handle clicks to views in recyclerView */
            nightId.let {
                sleepTrackerViewModel.onSleepNightClicked(nightId)
            }
        })
        binding.sleepList.adapter = adapterUI

        /** Define GridLayoutManager */
        val gridManager =  GridLayoutManager(activity, 3)
        binding.sleepList.layoutManager = gridManager

        sleepTrackerViewModel.nights.observe(this.viewLifecycleOwner, Observer {
            it?.let {
                adapterUI.submitList(it)   // Submits a new list to be diffed, and displayed.
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

//        /** navigate to the SleepQualityDetails Fragment */
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
