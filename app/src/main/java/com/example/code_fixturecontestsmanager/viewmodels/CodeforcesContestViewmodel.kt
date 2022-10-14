package com.example.code_fixturecontestsmanager.viewmodels
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.code_fixture.models.Contests
import com.example.code_fixturecontestsmanager.repositories.CodeforcesContestsRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class CodeforcesContestViewmodel: ViewModel() {

    var repository: CodeforcesContestsRepository = CodeforcesContestsRepository()
    var codeforcesContest = MutableLiveData<Contests>()
    var listSize = MutableLiveData<Int>(0)
    fun getContests() {
        viewModelScope.launch {
            try {
                codeforcesContest.value = repository.getCodeForcesContests()
                listSize.value = codeforcesContest.value?.size
            } catch (e: Exception) {
                Log.d("ERROR", e.toString())
            }
        }
    }
}