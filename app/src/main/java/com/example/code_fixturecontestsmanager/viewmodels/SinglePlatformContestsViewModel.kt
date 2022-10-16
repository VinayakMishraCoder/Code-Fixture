package com.example.code_fixturecontestsmanager.viewmodels
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.code_fixture.models.Contests
import com.example.code_fixture.models.ContestsItem
import com.example.code_fixturecontestsmanager.activities.SinglePlatformContestsActivity
import com.example.code_fixturecontestsmanager.repositories.ContestsRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class SinglePlatformContestsViewModel: ViewModel() {

    var repository: ContestsRepository = ContestsRepository()
    var singlePlatformContests = MutableLiveData<Contests>()
    var filteredValuesSet = false
    var singlePlatformContestsUnder24Hours: Contests? = null
    var singlePlatformContestsLater: Contests? = null
    var singlePlatformAllContests: Contests? = null
    var listSize = MutableLiveData<Int>(0)
    var filteredListSize : Int? = null

    fun getContests(activityId: String, onFailure: () -> Unit) {
        viewModelScope.launch {
            try {
                singlePlatformContests.value = repository.getSinglePlatformContests(activityId)
                listSize.value = singlePlatformContests.value?.size
            } catch (e: Exception) {
                onFailure.invoke()
            }
        }
    }

    fun setFilter(filterId: String) {
        when(filterId) {
            SinglePlatformContestsActivity.UNDER_24_FILTER -> {
                singlePlatformContestsUnder24Hours?.let {
                    filteredListSize = it.size
                    singlePlatformContests.value = it
                }
            }
            SinglePlatformContestsActivity.LATER_CONTESTS_FILTER -> {
                singlePlatformContestsLater?.let{
                    filteredListSize = it.size
                    singlePlatformContests.value = it
                }
            }
            SinglePlatformContestsActivity.ALL_CONTESTS_FILTER -> {
                singlePlatformAllContests?.let {
                    filteredListSize = it.size
                    singlePlatformContests.value = it
                }
            }
        }
    }
}