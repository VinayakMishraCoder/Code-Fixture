package com.example.code_fixturecontestsmanager.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.code_fixturecontestsmanager.Constants
import com.example.code_fixturecontestsmanager.models.AvailablePlatforms
import com.example.code_fixturecontestsmanager.models.SavedPlatformsContainer
import com.example.code_fixturecontestsmanager.models.UserDetailsContainer
import com.example.code_fixturecontestsmanager.repositories.PlatformsRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AvailablePlatformsViewModel : ViewModel() {

    val repository = PlatformsRepository()
    var listSize = MutableLiveData(0)
    var platformList = MutableLiveData<AvailablePlatforms>()

    /**
    * Firebase Info and collections.
    **/
    val currUserLoggedIn = Firebase.auth.currentUser
    var listOfSites = ArrayList<String>()

    /**
     * Lists related to DB.
     **/
    val siteCollection = Firebase.firestore.collection(Constants.FIREBASE_COLLECTION_REFERENCE1)
    var listOfAlreadySelectedSites = ArrayList<String>()

    fun getAvailablePlatforms() {
        retrieveSelectedSitesList()
        equaliseLists()
        viewModelScope.launch {
            platformList.value = repository.getAvailablePlatforms()
            listSize.value = platformList.value?.size
        }
    }

    fun onCheckBoxStateChanged(siteName: String, isChecked: Boolean) {
        when (isChecked) {
            true -> listOfSites.add(siteName)
            else -> listOfSites.remove(siteName)
        }
        Log.d("drag", isChecked.toString())
    }

    fun onSaveClick() = CoroutineScope(Dispatchers.IO).launch {
        currUserLoggedIn?.email?.let {
            val docRef = siteCollection.document(it)
            withContext(Dispatchers.Main) {
                docRef.get().addOnSuccessListener { document ->
                    document?.let { snap ->
                        var temp: UserDetailsContainer?
                        snap.toObject<UserDetailsContainer>()?.let { userDetails ->
                            temp = userDetails
                            temp?.listOfSavedSites = listOfSites
                            temp?.let { docRef.set(it) }
                            equaliseLists()
                        }
                    }
                }.addOnFailureListener { exception ->
                    Log.d("ffg_on_save", exception.message.toString())
                }
            }
        }
    }

    fun retrieveSelectedSitesList() = CoroutineScope(Dispatchers.IO).launch {
        currUserLoggedIn?.email?.let {
            val docRef = siteCollection.document(it)
            withContext(Dispatchers.Main) {
                docRef.get().addOnSuccessListener { document ->
                    document?.let { snap ->
                        snap.toObject<UserDetailsContainer>()?.listOfSavedSites?.let { list ->
                            listOfAlreadySelectedSites = list
                            equaliseLists()
                        }
                    }
                }.addOnFailureListener { exception ->
                    Log.d("ffg_on_retrieve", exception.message.toString())
                }
            }
        }
    }

    fun equaliseLists() {
        listOfSites = listOfAlreadySelectedSites
    }
}