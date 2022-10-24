package com.example.code_fixturecontestsmanager.activities

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
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
import com.example.code_fixturecontestsmanager.databinding.ActivityAllOtherPlatformBinding
import com.example.code_fixturecontestsmanager.databinding.LoadingDialogBinding
import com.example.code_fixturecontestsmanager.viewmodels.SinglePlatformContestsViewModel
import java.util.*
import java.util.Locale.filter

class AllOtherPlatformActivity : AppCompatActivity(),
    SinglePlatformContestAdapter.onContestItemClickListener {

    lateinit var binding: ActivityAllOtherPlatformBinding
    lateinit var adapter: SinglePlatformContestAdapter
    private val viewModel: SinglePlatformContestsViewModel by lazy {
        ViewModelProvider(this).get(SinglePlatformContestsViewModel::class.java)
    }
    val activityId = "AllOtherContestsActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_all_other_platform)
        supportActionBar?.hide()

        binding.filterSortBut.setOnClickListener {
            val inflater =
                binding.root.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.filter_popup_window, null) // pass custom layout
            val popupWindow =
                PopupWindow(view, 500, ConstraintLayout.LayoutParams.WRAP_CONTENT, true)
            popupWindow.elevation = 40.0f
            view.findViewById<CardView>(R.id.under24Filter).setOnClickListener {
                viewModel.setFilter(SinglePlatformContestsActivity.UNDER_24_FILTER)
            }
            view.findViewById<CardView>(R.id.before24Filter).setOnClickListener {
                viewModel.setFilter(SinglePlatformContestsActivity.LATER_CONTESTS_FILTER)
            }
            view.findViewById<CardView>(R.id.allFilter).setOnClickListener {
                viewModel.setFilter(SinglePlatformContestsActivity.ALL_CONTESTS_FILTER)
            }
            popupWindow.showAsDropDown(binding.filterSortBut) // view to attach with
        }

        binding.backButton.setOnClickListener { onBackPressed() }
        val dialogBinding = LoadingDialogBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this).setView(dialogBinding.root).create()
        dialog.setCanceledOnTouchOutside(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
        dialog.show()
        viewModel.getContests(activityId, {
            Toast.makeText(
                this@AllOtherPlatformActivity,
                "Error Occurred!",
                Toast.LENGTH_SHORT
            ).show()
        })
        adapter = SinglePlatformContestAdapter()

        viewModel.singlePlatformContests.observe(this) { contestData ->
            viewModel.listSize.observe(this) { currSize ->
//                binding.progressHorz.visibility = (if (currSize > 0) View.GONE else View.VISIBLE)
                dialog.cancel()
                if (currSize > 0) {
                    setFilteredValues(contestData)
                    viewModel.filteredListSize?.let {
//                        if(it == 0) binding.noResponseView.visibility = View.VISIBLE
//                        else binding.noResponseView.visibility = View.INVISIBLE
                    }
                    adapter.setUpRecyclerView(
                        data = contestData,
                        listener = this@AllOtherPlatformActivity,
                        activityId = activityId
                    ) {
                        val controller: LayoutAnimationController =
                            AnimationUtils.loadLayoutAnimation(
                                binding.recyclerView.context,
                                R.anim.layout_anim
                            )
                        binding.recyclerView.setLayoutAnimation(controller);
                        adapter.notifyDataSetChanged()
                        binding.recyclerView.scheduleLayoutAnimation()
                    }
                    binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        android.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false
                        override fun onQueryTextChange(msg: String): Boolean {
                            var filteredData = Contests()
                            val searchText = msg.lowercase()
                            for(contest in contestData) {
                                val contestSite = contest.site?.lowercase()
                                val contestName = contest.site?.lowercase()
                                if(contestSite?.contains(searchText) == true || contestName?.contains(searchText) == true)
                                    filteredData.add(contest)
                            }
                            adapter.setUpRecyclerView(
                                data = filteredData,
                                listener = this@AllOtherPlatformActivity,
                                activityId = activityId
                            ) {
                                adapter.notifyDataSetChanged()
                            }
                            return false
                        }
                    })
                    binding.recyclerView.adapter = adapter
                } else {

                }
            }
        }
    }

    override fun onRegisterClick(contestsItem: ContestsItem) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        val colorInt: Int = Color.parseColor("#1565C0")
        val defaultColors = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(colorInt)
            .build()
        builder.setDefaultColorSchemeParams(defaultColors)
        customTabsIntent.launchUrl(this, Uri.parse(contestsItem.url))
    }

    override fun onSaveClick(contestsItem: ContestsItem) {

    }

    override fun onAlarmSetClick(contestsItem: ContestsItem) {
        notifierForContest(contestsItem)
    }

    fun setFilteredValues(contestData: Contests) {
        if (viewModel.filteredValuesSet) return
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
        val intent = Intent(Intent.ACTION_INSERT)
        intent.data = CalendarContract.Events.CONTENT_URI
        intent.putExtra(CalendarContract.Events.TITLE, contestsItem.name)
        intent.putExtra(CalendarContract.Events.DESCRIPTION, contestsItem.in_24_hours)
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginCal?.timeInMillis)
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endcal.timeInMillis)
        startActivity(intent)
    }

}