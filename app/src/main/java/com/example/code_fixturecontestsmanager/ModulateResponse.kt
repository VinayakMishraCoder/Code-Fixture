package com.example.code_fixturecontestsmanager

import com.example.code_fixture.models.ContestsItem
import com.example.code_fixture.models.MultiplatformContestsItem

object ModulateResponse {
    fun under24Hours (list: ArrayList<ContestsItem>): ArrayList<ContestsItem> {
        var newList = ArrayList<ContestsItem>()
        for (x in list) {
            if(x.in_24_hours == "Yes") newList.add(x)
        }
        return newList
    }
    fun after24Hours (list: ArrayList<ContestsItem>): ArrayList<ContestsItem> {
        var newList = ArrayList<ContestsItem>()
        for (x in list) {
            if(x.in_24_hours == "No") newList.add(x)
        }
        return newList
    }
    fun under24HoursForAllPlatforms (list: ArrayList<MultiplatformContestsItem>): ArrayList<MultiplatformContestsItem> {
        var newList = ArrayList<MultiplatformContestsItem>()
        for (x in list) {
            if(x.in_24_hours == "Yes") newList.add(x)
        }
        return newList
    }
    fun after24HoursForAllPlatforms (list: ArrayList<MultiplatformContestsItem>): ArrayList<MultiplatformContestsItem> {
        var newList = ArrayList<MultiplatformContestsItem>()
        for (x in list) {
            if(x.in_24_hours == "No") newList.add(x)
        }
        return newList
    }
    fun segregate (list: ArrayList<MultiplatformContestsItem>): ArrayList<MultiplatformContestsItem> {
        var newList = ArrayList<MultiplatformContestsItem>()
        for (x in list) {
            if(x.site != "LeetCode" && x.site != "CodeChef" && x.site != "CodeForces") newList.add(x)
        }
        return newList
    }
}