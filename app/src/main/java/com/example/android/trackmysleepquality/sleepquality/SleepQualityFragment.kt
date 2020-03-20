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

package com.example.android.trackmysleepquality.sleepquality

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.databinding.FragmentSleepQualityBinding

/**
 * Fragment that displays a list of clickable icons,
 * each representing a sleep quality rating.
 * Once the user taps an icon, the quality is set in the current sleepNight
 * and the database is updated.
 */
class SleepQualityFragment : Fragment() {
    private lateinit var viewModelQuality_Factory:SleepQualityViewModelFactory
    private lateinit var sleepQualityViewModel: SleepQualityViewModel
    /**
     * Called when the Fragment is ready to display content to the screen.
     *
     * This function uses DataBindingUtil to inflate R.layout.fragment_sleep_quality.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val bindingQuality: FragmentSleepQualityBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_sleep_quality, container, false)

        val applicationQuality = requireNotNull(this.activity).application

        val arguments = SleepQualityFragmentArgs.fromBundle(arguments!!).sleepNightKey
        val dataSource = SleepDatabase.getInstance(applicationQuality).sleepDatabaseDao

        viewModelQuality_Factory = SleepQualityViewModelFactory(arguments, dataSource)
        sleepQualityViewModel = ViewModelProvider(this, viewModelQuality_Factory)
                .get(SleepQualityViewModel::class.java)
        bindingQuality.sleepQualityViewModelView = sleepQualityViewModel

        // TODO: Call binding.setLifecycleOwner to make the data binding lifecycle aware:
//        bindingQuality.lifecycleOwner = this.viewLifecycleOwner   /** 'above Line' */
        bindingQuality.setLifecycleOwner(this)                      /** Alternate approach to 'above Line' */

/**
 * using 'default-it'
 *          sleepQualityViewModel.navigateToSleepTracker_Fragment.observe(this.viewLifecycleOwner,  Observer {
            if (it == true) { // Observed state is true.
                this.findNavController().navigate(
//                findNavController().navigate(
                        SleepQualityFragmentDirections
                                .actionSleepQualityFragmentToSleepTrackerFragment())
                sleepQualityViewModel.doneNavigating()
            }
        })
 */

        sleepQualityViewModel.navigateToSleepTracker_Fragment.observe(this.viewLifecycleOwner, Observer { navigate ->
            navigate?.let {
                if(navigate){
                    findNavController().navigate(
                            SleepQualityFragmentDirections.actionSleepQualityFragmentToSleepTrackerFragment())
                    sleepQualityViewModel.doneNavigating()
                }
            }
        })
        return bindingQuality.root
    }
}
