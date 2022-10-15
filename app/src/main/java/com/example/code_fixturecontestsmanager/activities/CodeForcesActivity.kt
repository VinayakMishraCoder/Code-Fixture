package com.example.code_fixturecontestsmanager.activities

import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.graphics.Point
import android.os.Bundle
import android.provider.CalendarContract
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.code_fixture.models.ContestsItem
import com.example.code_fixturecontestsmanager.R
import com.example.code_fixturecontestsmanager.UtilProvider
import com.example.code_fixturecontestsmanager.adapters.ContestAdapter
import com.example.code_fixturecontestsmanager.databinding.ActivityCodeForcesBinding
import com.example.code_fixturecontestsmanager.viewmodels.CodeforcesContestViewmodel
import java.util.*

class CodeForcesActivity : AppCompatActivity(), ContestAdapter.onContestItemClickListener {

    lateinit var binding: ActivityCodeForcesBinding
    lateinit var adapter: ContestAdapter
    private val viewModel: CodeforcesContestViewmodel by lazy {
        ViewModelProvider(this).get(CodeforcesContestViewmodel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_code_forces)

        supportActionBar?.hide()

        binding.filterSortBut.setOnClickListener {
            val inflater = binding.root.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.filter_popup_window, null) // pass custom layout
            val popupWindow = PopupWindow(view, 500, ConstraintLayout.LayoutParams.WRAP_CONTENT,true)
            popupWindow.elevation = 40.0f
            popupWindow.showAsDropDown(binding.filterSortBut) // view to attach with
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        adapter = ContestAdapter()
        viewModel.getContests()

        viewModel.codeforcesContest.observe(this) { contestData ->
            viewModel.listSize.observe(this) { currSize ->
                binding.progressBarCforces.visibility = (if (currSize > 0) View.GONE else View.VISIBLE)
                if (currSize > 0) {
                    adapter.setUpRecyclerView(
                        data = contestData,
                        listener = this@CodeForcesActivity
                    )
                    binding.recyclerView.adapter = adapter
                }
            }
        }
    }

    override fun onRegisterClick(contestsItem: ContestsItem) {

    }

    override fun onSaveClick(contestsItem: ContestsItem) {

    }

    override fun onAlarmSetClick(contestsItem: ContestsItem) {
        notifierForContest(contestsItem)
    }

    fun onFilter(id: Int) {

    }

    fun notifierForContest(contestsItem: ContestsItem) {

        val start_time = UtilProvider.istProvider(contestsItem.start_time)
        val end_time = UtilProvider.istProvider(contestsItem.end_time)

        val date: String? = start_time?.substring(0,2)
        val month: String? = start_time?.substring(3,5)
        val year: String? = start_time?.substring(6, 10)
        val hour: String? = start_time?.substring(11, 13)
        val minutes: String? = start_time?.substring(14, 16)

        val ending_date: String? = end_time?.substring(0,2)
        val ending_month: String? = end_time?.substring(3,5)
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