package com.example.code_fixturecontestsmanager.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.code_fixture.models.Contests
import com.example.code_fixturecontestsmanager.Constants
import com.example.code_fixturecontestsmanager.activities.SinglePlatformContestsActivity
import com.example.code_fixturecontestsmanager.models.UserDetailsContainer
import com.example.code_fixturecontestsmanager.repositories.ContestsRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class ContestsViewModel : ViewModel() {

    var repository: ContestsRepository = ContestsRepository()
    var singlePlatformContests = MutableLiveData<Contests>()
    var filteredValuesSet = false
    var singlePlatformContestsUnder24Hours: Contests? = null
    var singlePlatformContestsLater: Contests? = null
    var singlePlatformAllContests: Contests? = null
    var listSize = MutableLiveData<Int>(0)
    var filteredListSize: Int? = null

    /**
     * Favourite Ste Contests Related content.
     * */
    val siteCollection = Firebase.firestore.collection(Constants.FIREBASE_COLLECTION_REFERENCE1)
    val currUser = Firebase.auth.currentUser
    var favouritesActivity = false

    fun getContests(activityId: String, onFailure: () -> Unit) {
        viewModelScope.launch {
            try {
                var temporaryContestsListResult = repository.getSinglePlatformContests(activityId)
                var contestList = Contests()
                if(favouritesActivity) {
                    currUser?.email?.let {
                        val docRef = siteCollection.document(it)
                        docRef.get().addOnSuccessListener { document ->
                            document.let { snap ->
                                snap.toObject<UserDetailsContainer>().let { userDetails ->
                                    for (contest in temporaryContestsListResult) {
                                        userDetails?.listOfSavedSites?.contains(contest.site)?.let {
                                            if(it) contestList.add(contest)
                                        }
                                    }
                                    singlePlatformContests.value = contestList
                                    listSize.value = singlePlatformContests.value?.size
                                }
                            }
                        }.addOnFailureListener { exception ->
                            Log.d("ffg_favsitefilt", exception.message.toString())
                        }
                    }
                }
                else {
                    singlePlatformContests.value = temporaryContestsListResult
                    listSize.value = singlePlatformContests.value?.size
                }

            } catch (e: Exception) {
                onFailure.invoke()
            }
        }
    }

    fun setFilter(filterId: String) {
        when (filterId) {
            SinglePlatformContestsActivity.UNDER_24_FILTER -> {
                singlePlatformContestsUnder24Hours?.let {
                    filteredListSize = it.size
                    singlePlatformContests.value = it
                }
            }
            SinglePlatformContestsActivity.LATER_CONTESTS_FILTER -> {
                singlePlatformContestsLater?.let {
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