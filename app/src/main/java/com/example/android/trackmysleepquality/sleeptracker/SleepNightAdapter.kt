package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.convertDurationToFormatted
import com.example.android.trackmysleepquality.convertNumericQualityToString
import com.example.android.trackmysleepquality.database.SleepNight

class SleepNightAdapter: RecyclerView.Adapter<SleepNightAdapter.ListViewHolder>() {
    var data =  listOf<SleepNight>()
        set(value) {                    /** customer Setter, slower approach to reDraw soon any data update happened*/
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
//        return ListViewHolder.Companion.from(parent)      /** 'Companion' here is redundant, as Kotlin is smart enough to work even without it */
        return ListViewHolder.from(parent)
    }


//    class ListViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {                    // Old Implementation
    /** below added 'private constructor' so it could only be accessed via Companion Object */
    class ListViewHolder private constructor(itemView: View): RecyclerView.ViewHolder(itemView) {
        val sleepLength: TextView = itemView.findViewById(R.id.sleep_length)
        val quality: TextView = itemView.findViewById(R.id.quality_string)
        val qualityImage: ImageView = itemView.findViewById(R.id.quality_image)

        fun bind(item: SleepNight) {
            /** Another 'Encapsulation' moved to view details from Adapter class
             * so multiple 'ViewHolders' can be created used
             * later
             *  Now this is an 'Extension function'
             *
             **/
            val res = itemView.context.resources
            sleepLength.text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, res)
            quality.text = convertNumericQualityToString(item.sleepQuality, res)
            qualityImage.setImageResource(when (item.sleepQuality) {
                0 -> R.drawable.ic_sleep_0
                1 -> R.drawable.ic_sleep_1
                2 -> R.drawable.ic_sleep_2
                3 -> R.drawable.ic_sleep_3
                4 -> R.drawable.ic_sleep_4
                5 -> R.drawable.ic_sleep_5
                else -> R.drawable.ic_sleep_active
            })
        }

        /** to create factory like pattern
         * To do that, private contstructor is needed in ListViewHolder declaration
         *
         **/
        companion object {
            fun from(parent: ViewGroup): ListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val listView = layoutInflater.inflate(R.layout.list_item_view, parent, false)
                return ListViewHolder(listView)
            }
        }
    }

}