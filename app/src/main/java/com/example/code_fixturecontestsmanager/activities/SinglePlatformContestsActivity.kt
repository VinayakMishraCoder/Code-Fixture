package com.example.code_fixturecontestsmanager.activities

import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.os.Bundle
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.code_fixture.models.Contests
import com.example.code_fixture.models.ContestsItem
import com.example.code_fixturecontestsmanager.MainActivity
import com.example.code_fixturecontestsmanager.ModulateResponse
import com.example.code_fixturecontestsmanager.R
import com.example.code_fixturecontestsmanager.UtilProvider
import com.example.code_fixturecontestsmanager.adapters.SinglePlatformContestAdapter
import com.example.code_fixturecontestsmanager.databinding.ActivitySinglePlatformContestsBinding
import com.example.code_fixturecontestsmanager.viewmodels.SinglePlatformContestsViewModel
import java.util.*

class SinglePlatformContestsActivity : AppCompatActivity(),
    SinglePlatformContestAdapter.onContestItemClickListener {

    lateinit var binding: ActivitySinglePlatformContestsBinding
    lateinit var adapter: SinglePlatformContestAdapter
    private var activityId = "null"
    private val viewModel: SinglePlatformContestsViewModel by lazy {
        ViewModelProvider(this).get(SinglePlatformContestsViewModel::class.java)
    }
    companion object {
        val URL_KEY = "url_key"
        val UNDER_24_FILTER = "UNDER_24_FILTER"
        val LATER_CONTESTS_FILTER = "LATER_CONTESTS_FILTER"
        val ALL_CONTESTS_FILTER = "ALL_CONTESTS_FILTER"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_single_platform_contests)
        supportActionBar?.hide()

        MainActivity.ACTIVITY_SHIFTER?.let { activityId = intent?.getStringExtra(it) as String }
        binding.toolbarTitle.text = activityId + " Contests"
        binding.noResponseView.visibility = View.INVISIBLE

        UtilProvider.getGradientGoldenDescent(binding.toolbarTitle, activityId)
        UtilProvider.getGradientBlueDescent(binding.belowTools, "Start Registering in Contests!!")

        binding.filterSortBut.setOnClickListener {
            val inflater = binding.root.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.filter_popup_window, null) // pass custom layout
            val popupWindow = PopupWindow(view, 500, ConstraintLayout.LayoutParams.WRAP_CONTENT, true)
            popupWindow.elevation = 40.0f
            view.findViewById<CardView>(R.id.under24Filter).setOnClickListener {
                viewModel.setFilter(UNDER_24_FILTER)
            }
            view.findViewById<CardView>(R.id.before24Filter).setOnClickListener {
                viewModel.setFilter(LATER_CONTESTS_FILTER)
            }
            view.findViewById<CardView>(R.id.allFilter).setOnClickListener {
                viewModel.setFilter(ALL_CONTESTS_FILTER)
            }
            popupWindow.showAsDropDown(binding.filterSortBut) // view to attach with
        }

        binding.backButton.setOnClickListener { onBackPressed() }
        activityId?.let {
            viewModel.getContests(it, {
                Toast.makeText(
                    this@SinglePlatformContestsActivity,
                    "Error Occurred!",
                    Toast.LENGTH_SHORT
                ).show()
            })
        }
        adapter = SinglePlatformContestAdapter()

        viewModel.singlePlatformContests.observe(this) { contestData ->
            viewModel.listSize.observe(this) { currSize ->
                binding.progressBarCforces.visibility = (if (currSize > 0) View.GONE else View.VISIBLE)
                if (currSize > 0) {
                    setFilteredValues(contestData)
                    viewModel.filteredListSize?.let {
                        if(it == 0) binding.noResponseView.visibility = View.VISIBLE
                        else binding.noResponseView.visibility = View.INVISIBLE
                    }
                    adapter.setUpRecyclerView(
                        data = contestData,
                        listener = this@SinglePlatformContestsActivity,
                        activityId = activityId
                    )
                    binding.recyclerView.adapter = adapter
                } else {

                }
            }
        }
    }

    override fun onRegisterClick(contestsItem: ContestsItem) {
        val intent = Intent(this, BrowserScreenActivity::class.java)
        intent.putExtra(URL_KEY, contestsItem.url)
        startActivity(intent)
    }

    override fun onSaveClick(contestsItem: ContestsItem) {

    }

    override fun onAlarmSetClick(contestsItem: ContestsItem) {
        notifierForContest(contestsItem)
    }

    fun setFilteredValues(contestData: Contests) {
        if(viewModel.filteredValuesSet) return
        viewModel.singlePlatformContestsLater = ModulateResponse.after24Hours(contestData)
        viewModel.singlePlatformContestsUnder24Hours = ModulateResponse.under24Hours(contestData)
        viewModel.singlePlatformAllContests = contestData
        viewModel.filteredValuesSet = true
    }

    fun notifierForContest(contestsItem: ContestsItem) {

        val start_time = UtilProvider.istProvider(contestsItem.start_time)
        val end_time = UtilProvider.istProvider(contestsItem.end_time)

        val date: String? = start_time?.substring(0, 2)
        val month: String? = start_time?.substring(3, 5)
        val year: String? = start_time?.substring(6, 10)
        val hour: String? = start_time?.substring(11, 13)
        val minutes: String? = start_time?.substring(14, 16)

        val ending_date: String? = end_time?.substring(0, 2)
        val ending_month: String? = end_time?.substring(3, 5)
        val ending_year: String? = end_time?.substring(6, 10)
        val ending_hour: String? = end_time?.substring(11, 13)
        val ending_minutes: String? = end_time?.substring(14, 16)

        val beginCal: Calendar? = Calendar.getInstance()
        year?.toInt()?.let { _year ->
            month?.toInt()?.let { it1 ->
                date?.toInt()?.let { it2 ->
                    hour?.toInt()?.let { it3 ->
                        minutes?.toInt()?.let { it4 -> beginCal?.set(_year, it1, it2, it3, it4) }
                    }
                }
            }
        }
        val endcal: Calendar = Calendar.getInstance()
        ending_year?.toInt()?.let {
            ending_month?.toInt()?.let { it1 ->
                ending_date?.toInt()?.let { it2 ->
                    ending_hour?.toInt()?.let { it3 ->
                        ending_minutes?.toInt()?.let { it4 -> endcal.set(it, it1, it2, it3, it4) }
                    }
                }
            }
        }
        val intent = Intent(ACTION_INSERT)
        intent.data = CalendarContract.Events.CONTENT_URI
        intent.putExtra(CalendarContract.Events.TITLE, contestsItem.name)
        intent.putExtra(CalendarContract.Events.DESCRIPTION, contestsItem.in_24_hours)
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginCal?.timeInMillis)
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endcal.timeInMillis)
        startActivity(intent)
    }
}