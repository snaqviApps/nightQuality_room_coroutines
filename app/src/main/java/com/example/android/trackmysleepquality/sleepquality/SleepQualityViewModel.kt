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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import kotlinx.coroutines.*

// Completed_TODO: Create SleepQualityViewModel

class SleepQualityViewModel(
        private val sleepNightKey: Long = 0L
        , val databaseDao: SleepDatabaseDao): ViewModel() {

    private val sleepQuality_viewModel_Job = Job()          /** Job for Coroutines */

    private val uiScope_SleepQuality = CoroutineScope(Dispatchers.Main + sleepQuality_viewModel_Job)
    private val _navigateToSleepTracker_Fragment = MutableLiveData<Boolean?>()
    val navigateToSleepTracker_Fragment: LiveData<Boolean?>
        get() = _navigateToSleepTracker_Fragment

    fun doneNavigating(){
        _navigateToSleepTracker_Fragment.value = null
    }

    // TODO: Using the code in SleepTrackerViewModel as an example, however here doing in one step
    fun onSetSleepQuality(quality:Int){
        uiScope_SleepQuality.launch {
            withContext(Dispatchers.IO){
                val tonight = databaseDao.get(sleepNightKey) ?: return@withContext
                tonight.sleepQuality = quality
                databaseDao.update(tonight)
            }
            _navigateToSleepTracker_Fragment.value = true
        }
    }


    override fun onCleared() {
        super.onCleared()
        sleepQuality_viewModel_Job.cancel()         // cancelling
    }

}