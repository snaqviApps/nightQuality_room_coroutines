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
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.convertDurationToFormatted
import com.example.android.trackmysleepquality.convertNumericQualityToString
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
    class ListViewHolder private constructor(val binding: ListItemViewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SleepNight) {
            val res = itemView.context.resources
            binding.sleepLength.text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, res)
            binding.qualityString.text = convertNumericQualityToString(item.sleepQuality, res)
            binding.qualityImage.setImageResource(when (item.sleepQuality) {
                0 -> R.drawable.ic_sleep_0
                1 -> R.drawable.ic_sleep_1
                2 -> R.drawable.ic_sleep_2
                3 -> R.drawable.ic_sleep_3
                4 -> R.drawable.ic_sleep_4
                5 -> R.drawable.ic_sleep_5
                else -> R.drawable.ic_sleep_active
            })
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





