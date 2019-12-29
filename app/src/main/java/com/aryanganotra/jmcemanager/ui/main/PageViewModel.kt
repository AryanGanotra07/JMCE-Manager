package com.aryanganotra.jmcemanager.ui.main

import androidx.lifecycle.*
import com.aryanganotra.jmcemanager.Firebase.FirebaseRepo
import com.aryanganotra.jmcemanager.model.Tab

class PageViewModel : ViewModel() {

    private val repo = FirebaseRepo()
    private val _index = MutableLiveData<Int>()
    val text: LiveData<String> = Transformations.map(_index) {
        "Hello world from section: $it"
    }

    fun setIndex(index: Int) {
        _index.value = index
    }

    fun getIndex() : Int {
        return _index.value!!
    }

    fun getNotesLiveData() : MediatorLiveData<ArrayList<Tab>> {
        return repo.getNotesLiveData()
    }
 }