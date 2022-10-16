package com.example.code_fixturecontestsmanager

import com.example.code_fixture.models.Contests
import com.example.code_fixture.models.ContestsItem

object ModulateResponse {
    fun under24Hours (list: Contests): Contests {
        var newList = Contests()
        for (x in list) {
            if(x.in_24_hours == "Yes") newList.add(x)
        }
        return newList
    }
    fun after24Hours (list: Contests): Contests {
        var newList = Contests()
        for (x in list) {
            if(x.in_24_hours == "No") newList.add(x)
        }
        return newList
    }
    fun segregate (list: Contests): Contests {
        var newList = Contests()
        for (x in list) {
            x.site?.let {
                if(x.site != "LeetCode" && x.site != "CodeChef" && x.site != "CodeForces") newList.add(x)
            } ?: kotlin.run {
                return list
            }
        }
        return newList
    }
}