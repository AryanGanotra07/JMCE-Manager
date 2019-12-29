package com.aryanganotra.jmcemanager.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class Note(var id : String = "",var noteName : String = "" , var downloadUrl : String = "", var subId : String = "") :
    Parcelable
{
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "noteName" to noteName,
            "downloadUrl" to downloadUrl,
            "subId" to subId
        )
    }
}
