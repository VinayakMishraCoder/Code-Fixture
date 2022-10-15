package com.example.code_fixturecontestsmanager.viewmodels
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.code_fixture.models.Contests
import com.example.code_fixturecontestsmanager.repositories.ContestsRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class SinglePlatformContestsViewModel: ViewModel() {

    var repository: ContestsRepository = ContestsRepository()
    var singlePlatformContests = MutableLiveData<Contests>()
    var listSize = MutableLiveData(0)
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
}