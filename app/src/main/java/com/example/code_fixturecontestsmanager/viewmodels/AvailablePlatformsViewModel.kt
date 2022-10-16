package com.example.code_fixturecontestsmanager.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.code_fixturecontestsmanager.models.AvailablePlatforms
import com.example.code_fixturecontestsmanager.repositories.PlatformsRepository
import kotlinx.coroutines.launch

class AvailablePlatformsViewModel: ViewModel() {

    val repository = PlatformsRepository()
    var platformList = MutableLiveData<AvailablePlatforms>()
    var listSize = MutableLiveData(0)

    fun getAvailablePlatforms() {
        viewModelScope.launch {
            platformList.value = repository.getAvailablePlatforms()
            listSize.value = platformList.value?.size
        }
    }

}