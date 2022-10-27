package com.example.code_fixturecontestsmanager.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.code_fixturecontestsmanager.Constants
import com.example.code_fixturecontestsmanager.models.AvailablePlatforms
import com.example.code_fixturecontestsmanager.models.SavedPlatformsContainer
import com.example.code_fixturecontestsmanager.repositories.PlatformsRepository
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
    var platformList = MutableLiveData<AvailablePlatforms>()
    var listSize = MutableLiveData(0)
    var listOfSites = ArrayList<String>()
    val siteCollection = Firebase.firestore.collection(Constants.FIREBASE_COLLECTION_REFERENCE1)
    var listOfAlreadySelectedSites = ArrayList<String>()

    fun getAvailablePlatforms() {
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
    fun onSaveClick() {
        siteCollection.document(Constants.FIREBASE_DOCUMENT_REFERENCE)
            .set(SavedPlatformsContainer(listOfSites))
        Log.d("ffg", listOfSites.toString())
    }
    fun retrieveSelectedSitesList() = CoroutineScope(Dispatchers.IO).launch {
        val x = siteCollection.get().await()
        val y = x.documents
        withContext(Dispatchers.Main) {
            y.get(0).toObject<SavedPlatformsContainer>()?.listOfSavedSites?.let {
                listOfAlreadySelectedSites = it
                equaliseLists()
            }
        }
    }
    fun equaliseLists() {
        listOfSites.clear()
        listOfSites.addAll(listOfAlreadySelectedSites)
    }
}