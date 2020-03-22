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

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.formatNights
import kotlinx.coroutines.*

/**
 * ViewModel for SleepTrackerFragment.
 */
class SleepTrackerViewModel(
        val databaseDao: SleepDatabaseDao,
        application: Application) : AndroidViewModel(application) {             /** AndroidViewModel takes 'Application instance as 'context' */

    //Completed_TODO (01) Declare Job() and cancel jobs in onCleared().
    private var trackerViewModelJob = Job()

    //Completed_TODO (02) Define uiScope for coroutines.
    private val uiScope = CoroutineScope(Dispatchers.Main + trackerViewModelJob)           /** this Coroutines-job will run on .Main */

    //Completed_TODO (03) Create a MutableLiveData variable tonight for one SleepNight.
    private var tonight = MutableLiveData<SleepNight?>()

    //Completed_TODO (04) Define a variable, nights. Then getAllNights() from the databaseDao, and assign to the nights variable.
//    private val nights = databaseDao.getAllNights()
    val nights = databaseDao.getAllNights()         /** changed access in this 'exercise' */


    val nightString = Transformations.map(nights){nightsInput ->
        formatNights(nightsInput, application.resources)
    }
    val startButtonVisible = Transformations.map(tonight){
        null == it
    }

    val stopButtonVisible = Transformations.map(tonight) {
        null != it
    }
    val clearButtonVisible = Transformations.map(nights) {
        it?.isNotEmpty()
    }

    private val _navigateToSleepQuality = MutableLiveData<SleepNight>()   /** Navigation: create a navigation-EVENT-liveData*/
    val navigateToSleepQuality: LiveData<SleepNight>                      /** Encapsulation */
        get() =  _navigateToSleepQuality

    fun doneNavigating(){                                                 /** reset the event variable, immediately after done navigating */
        _navigateToSleepQuality.value = null
    }

    //Completed_TODO (05) In an init block, initializeTonight(), and implement it to launch a coroutine, to getTonightFromDatabase().
    init {
        initializeTonight()
    }

    private fun initializeTonight() {
        uiScope.launch {
            tonight.value = getTonightFromDataBase()
        }
    }

    private var _showSnackbarEvent = MutableLiveData<Boolean>()
    val showSnackbar: LiveData<Boolean>
    get() = _showSnackbarEvent

    fun doneShowingSnackBar(){
        _showSnackbarEvent.value = false
    }
    private suspend fun getTonightFromDataBase(): SleepNight? {

        /** 2nd Coroutines withIn first-coroutine i.e: uiScope */
        return withContext(Dispatchers.IO){
            var night = databaseDao.getTonight()
            if(night?.endTimeMilli != night?.startTimeMilli){
                night = null
            }
            night
        }
    }

    // Completed_TODO (06): Implement the click handler for the Start button, onStartTracking(), using coroutines.
    //  Define the suspend function insert(), to insert a new night into the database
    fun onStartTracking(){
        uiScope.launch {
            val newNight = SleepNight()     // captures current time as 'start' time
            insert(newNight)
            tonight.value = getTonightFromDataBase()
        }

    }

    /** much like getTonightFromDatabase, so we can see a pattern here
     * 1. define method to get some work needed done
     * 2. call uiScope.launch, so UI can be updated
     * 3. then call a 'suspend fun()'
     * 4. in suspend() fun / method, call withContext(Dispatehrs.IO)
     *    call the longRunWork() [in here: Database-tasks, like insert, get-data]
     * */
    private suspend fun insert(night: SleepNight){
        withContext(Dispatchers.IO){
            databaseDao.insert(night)
        }
    }

    //TODO (08) Create onStopTracking() for the Stop button with an update() suspend function.
    fun onStopTracking() {
        uiScope.launch {

            /** returning from 'launch' and not from 'lambda'
             * taking advantage of Kotlin's 'label@launch' format, below
             */
            val oldNight = tonight.value ?: return@launch
            oldNight.endTimeMilli = System.currentTimeMillis()
            update(oldNight)
            _navigateToSleepQuality.value = oldNight                /** setting for Navigation, ONLY if oldNight is nonNull
                                                                        so, if we can't set it, we don't navigate
                                                                    */
        }
    }

    private suspend fun update(night: SleepNight) {
        withContext(Dispatchers.IO) {
            databaseDao.update(night)
        }
    }

    fun onClear() {
        uiScope.launch {
            clear()
            tonight.value = null
            _showSnackbarEvent.value = true
        }
    }

    suspend fun clear() {
        withContext(Dispatchers.IO) {
            databaseDao.clear()
        }
    }

    override fun onCleared() {
        super.onCleared()
        trackerViewModelJob.cancel()        /** cancelling all Coroutines-jobs */
    }

}

