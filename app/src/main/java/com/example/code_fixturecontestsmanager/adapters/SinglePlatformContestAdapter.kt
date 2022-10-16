package com.example.code_fixturecontestsmanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.code_fixture.models.ContestsItem
import com.example.code_fixturecontestsmanager.MainActivity
import com.example.code_fixturecontestsmanager.UtilProvider
import com.example.code_fixturecontestsmanager.databinding.ItemContestBinding

class SinglePlatformContestAdapter : RecyclerView.Adapter<SinglePlatformContestAdapter.ItemContestViewHolder>() {

    var data: ArrayList<ContestsItem>? = null
    var listener: onContestItemClickListener? = null
    var activityId:String? = null

    fun setUpRecyclerView(data: ArrayList<ContestsItem>, listener: onContestItemClickListener, activityId: String) {
        this.data = data
        this.listener = listener
        this.activityId = activityId
        notifyDataSetChanged()
    }

    inner class ItemContestViewHolder(val binding: ItemContestBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemContestViewHolder {
        val binding = ItemContestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemContestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemContestViewHolder, position: Int) {
        holder.binding.apply {

            /**
             * 1) Set listeners on individual buttons.
             * 2) Morph Duration to Hours and Minutes.
             * 3) Morph date and time to IST. (separate format for CodeChef contests)
             * */

            contestRegisterButton.setOnClickListener {
                data?.get(position)?.let { listener?.onRegisterClick(it) }
            }
            setAlarmButton.setOnClickListener {
                data?.get(position)?.let { listener?.onAlarmSetClick(it) }
            }
            saveButton.setOnClickListener {
                data?.get(position)?.let { listener?.onSaveClick(it) }
            }
            currContest = data?.get(position)
            durationMorphed = currContest?.duration?.let { UtilProvider.intoHoursAndMinutes(it) }
            siteName.text = activityId
            if(activityId == MainActivity.CODE_CHEF)
                startDateAndTimeMorphed = currContest?.start_time?.let { UtilProvider.istProviderForCodeChef(it) }
            else
                startDateAndTimeMorphed = currContest?.start_time?.let { UtilProvider.istProvider(it) }
        }
    }

    override fun getItemCount(): Int = data?.size!!

    interface onContestItemClickListener {
        fun onRegisterClick(contestsItem: ContestsItem)
        fun onSaveClick(contestsItem: ContestsItem)
        fun onAlarmSetClick(contestsItem: ContestsItem)
    }
}