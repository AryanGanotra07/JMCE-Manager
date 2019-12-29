package com.aryanganotra.jmcemanager.Firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.aryanganotra.jmcemanager.model.Tab
import com.google.firebase.database.FirebaseDatabase

class FirebaseRepo() {


    private val REF = FirebaseDatabase.getInstance().reference

    private val firebaseDatabaseLiveData = FirebaseDatabaseLiveData(REF)

    private val notesLiveData : MediatorLiveData<ArrayList<Tab>> = MediatorLiveData()

    init {
        notesLiveData.addSource(firebaseDatabaseLiveData, Observer {
            if (it!=null && it.hasChildren()) {

                val lecturesList: ArrayList<Tab> = ArrayList()
                for (lecture in it.children)
                {
                    val lect : Tab = lecture.getValue(Tab::class.java)!!
                    lecturesList.add(lect)
                }

                Thread(Runnable { notesLiveData.postValue(lecturesList) }).start()
            }
            else notesLiveData.value = null
        })

        }

    fun getNotesLiveData() : MediatorLiveData<ArrayList<Tab>>  {
        return notesLiveData
    }

    }