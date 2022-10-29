package com.example.code_fixturecontestsmanager.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.code_fixturecontestsmanager.databinding.PlatformItemBinding
import com.example.code_fixturecontestsmanager.models.AvailablePlatforms
import com.google.android.material.radiobutton.MaterialRadioButton

class PlatformAdapter : RecyclerView.Adapter<PlatformAdapter.PlatformViewHolder>() {

    var platformsList: AvailablePlatforms? = null
    var listener: onPlatformClickListeners? = null
    var listOfAlreadySelectedSites: ArrayList<String>? = null

    fun setUp(platformsList: AvailablePlatforms, listener: onPlatformClickListeners, listOfAlreadySelectedSites: ArrayList<String>) {
        this.platformsList = platformsList
        this.listener = listener
        this.listOfAlreadySelectedSites = listOfAlreadySelectedSites
        notifyDataSetChanged()
    }

    inner class PlatformViewHolder(val binding: PlatformItemBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlatformViewHolder {
        val binding =
            PlatformItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlatformViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlatformViewHolder, position: Int) {
        holder.binding.apply {
            siteName.text = platformsList?.get(position)?.get(0)
            platformsList?.get(position)?.get(0)?.let { name ->
                if(listOfAlreadySelectedSites?.contains(name) == true) saveSiteSwitch.isChecked = true
            }
            saveSiteSwitch.setOnClickListener {
                platformsList?.get(position)?.get(0)?.let { name ->
                    listener?.onCheckBoxClick(name, saveSiteSwitch.isChecked)
                }
            }
            visitLink.setOnClickListener {
                platformsList?.get(position)?.get(2)?.let { it1 -> listener?.onPlatformClick(it1) }
            }
        }
    }

    override fun getItemCount(): Int = platformsList?.size ?: 0

    interface onPlatformClickListeners {
        fun onPlatformClick(url: String)
        fun onCheckBoxClick(siteName: String, isChecked: Boolean)
    }
}