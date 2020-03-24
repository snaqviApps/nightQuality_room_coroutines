package com.example.android.trackmysleepquality.sleeptracker

/** My resource for the changrs
 *
 * https://classroom.udacity.com/courses/ud9012/lessons/ee5a525f-0ba3-4d25-ba29-1fa1d6c567b8/concepts/f13214fb-2d67-4155-adee-ea7b36458c36
 *
 */

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.ListItemViewBinding

class SleepNightAdapter: ListAdapter<SleepNight, SleepNightAdapter.ListViewHolder>(SleepNightDiffCallback()) {

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder.from(parent)
    }

    /** below added key-word 'private constructor' so it could only be accessed via Companion Object */
    class ListViewHolder private constructor(val bindingVH: ListItemViewBinding): RecyclerView.ViewHolder(bindingVH.root) {
        fun bind(item: SleepNight) {

//            TODO("Replace the code in SleepNightAdapter.ViewHolder.bind with a single binding to the SleepNight item, followed by executePendingBindings():")
            bindingVH.sleepAdapterView = item           // dataBinding variable in list_item_view.xml
            bindingVH.executePendingBindings()          // expedite the
        }

        /** to create factory-pattern
         * private contstructor is needed in ListViewHolder declaration
         **/
        companion object {
            fun from(parent: ViewGroup): ListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val lViewBinding = ListItemViewBinding.inflate(layoutInflater, parent, false)
                return ListViewHolder(lViewBinding)         // used 'intention-menu' to change 'itemView-type' to ListItemViewBinding
            }
        }
    }
}





