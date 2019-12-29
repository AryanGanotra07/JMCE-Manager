package com.aryanganotra.jmcemanager.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize


@Keep
@Parcelize
data class Tab(var year : String = "", var courses : List<Course> = arrayListOf()) : Parcelable {


    @Keep
     @Parcelize
    data class Course(var id : String = "",var courseName : String = "" , var notes : List<Note> = arrayListOf()) : Parcelable{

        @Keep
         @Parcelize
        data class Note(var noteName : String = "" , var downloadUrl : String = "") :Parcelable

     }
}