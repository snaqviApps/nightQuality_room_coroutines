package com.example.android.trackmysleepquality.sleeptracker

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.ItemViewHolder
import com.example.android.trackmysleepquality.convertDurationToFormatted
import com.example.android.trackmysleepquality.convertNumericQualityToString
import com.example.android.trackmysleepquality.database.SleepNight

class SleepNightAdapter: RecyclerView.Adapter<ItemViewHolder>() {

    var data =  listOf<SleepNight>()
        set(value) {                    /** customer Setter, slower approach to reDraw soon any data update happened*/
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        // The onBindViewHolder()function is called by RecyclerView to display the data for one list item at the specified position.
        val item = data[position]

/**
 * Old Implementation
//        if(item.sleepQuality <= 1){
//            holder.textView.setTextColor(RED)
//        } else {
//            // reset
//            holder.textView.setTextColor(BLACK)
//
//        }
//        holder.textView.text = item.sleepQuality.toString()
 *
 */
        /**  New Implementation */
        val res = holder.itemView.context.resources
//        bind(holder, item, res)                                     // before converting 'holder' parameter to receiver
        holder.bind(item, res)
    }

//    private fun bind(holder: ItemViewHolder, item: SleepNight, res: Resources) {      Original form
    private fun ItemViewHolder.bind(item: SleepNight, res: Resources) {        /** convert holder parameter to receiver */
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
//        val view = layoutInflater.inflate(R.layout.list_item_view, parent, false) as TextView    // Old Impelementation
        val listView = layoutInflater.inflate(R.layout.list_item_view, parent, false)
        return ItemViewHolder(listView)
    }
}