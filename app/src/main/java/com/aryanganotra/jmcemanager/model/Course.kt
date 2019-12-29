package com.aryanganotra.jmcemanager.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class Course(var id : String = "",var year : Int = -1,var courseName : String = "" ) :
    Parcelable {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "courseName" to courseName,
            "year" to year
        )
    }
}