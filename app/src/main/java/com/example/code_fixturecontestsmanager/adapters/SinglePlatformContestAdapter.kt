package com.example.code_fixturecontestsmanager.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.recyclerview.widget.RecyclerView
import com.example.code_fixture.models.ContestsItem
import com.example.code_fixturecontestsmanager.MainActivity
import com.example.code_fixturecontestsmanager.R
import com.example.code_fixturecontestsmanager.UtilProvider
import com.example.code_fixturecontestsmanager.databinding.ItemContestBinding

class SinglePlatformContestAdapter :
    RecyclerView.Adapter<SinglePlatformContestAdapter.ItemContestViewHolder>() {

    var data: ArrayList<ContestsItem>? = null
    var listener: onContestItemClickListener? = null
    var activityId: String? = null

    fun setUpRecyclerView(
        data: ArrayList<ContestsItem>,
        listener: onContestItemClickListener,
        activityId: String,
        setLayoutAnimation: () -> Unit
    ) {

        this.data = data
        this.listener = listener
        this.activityId = activityId
        setLayoutAnimation.invoke()
    }

    inner class ItemContestViewHolder(val binding: ItemContestBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemContestViewHolder {
        val binding = ItemContestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemContestViewHolder(binding)
    }

    /**
     * 1) Set listeners on individual buttons.
     * 2) Morph Duration to Hours and Minutes.
     * 3) Morph date and time to IST. (separate format for CodeChef contests)
     * */
    override fun onBindViewHolder(holder: ItemContestViewHolder, position: Int) {
        val contest = data?.get(position)

        holder.binding.apply {
            when (contest?.in_24_hours) {
                "Yes" -> {
                    itemContestBg.setBackgroundResource(R.color.under24bg)
                }
                "No" -> {
                    itemContestBg.setBackgroundResource(R.color.bg_color2)
                }
            }
            currStatus.text = when (contest?.status) {
                "CODING" -> {
                    "• Ongoing"
                }
                else -> {
                    "• Yet To Start"
                }
            }

            contestRegisterButton.setOnClickListener {
                contest?.let { listener?.onRegisterClick(it) }
            }
            setAlarmButton.setOnClickListener {
                contest?.let { listener?.onAlarmSetClick(it) }
            }
            saveButton.setOnClickListener {
                contest?.let { listener?.onSaveClick(it) }
            }
            currContest = contest
            durationMorphed = currContest?.duration?.let { UtilProvider.intoHoursAndMinutes(it) }

            if (contest?.site == null) siteName.text = "• " + activityId
            else siteName.text = "• " + contest.site

            if (activityId == MainActivity.CODE_CHEF || contest?.site == "CodeChef")
                startDateAndTimeMorphed =
                    currContest?.start_time?.let { UtilProvider.istProviderForCodeChef(it) }
            else
                startDateAndTimeMorphed =
                    currContest?.start_time?.let { UtilProvider.istProvider(it) }
        }
    }

    override fun getItemCount(): Int = data?.size!!

    interface onContestItemClickListener {
        fun onRegisterClick(contestsItem: ContestsItem)
        fun onSaveClick(contestsItem: ContestsItem)
        fun onAlarmSetClick(contestsItem: ContestsItem)
    }
}