package com.aryanganotra.jmcemanager.viewmodels

import androidx.lifecycle.*
import com.aryanganotra.jmcemanager.firebaserealtimedata.FirebaseRepo
import com.aryanganotra.jmcemanager.model.Course
import com.aryanganotra.jmcemanager.model.Note

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

    fun getCoursesLiveData() : MediatorLiveData<ArrayList<Course>> {
        return repo.getCoursesLiveData()
    }

    fun deleteCourse(course : Course)
    {
        repo.deleteCourse(course)
    }

    fun addCourse(course : Course)
    {
        repo.addCourse(course)
    }

    fun addNote(note : Note)
    {
        repo.addNote(note)
    }

    fun deleteNote(note: Note)
    {
        repo.deleteNote(note)
    }

    fun getNotesLiveData() : MediatorLiveData<ArrayList<Note>>
    {
        return repo.getNotesLiveData()
    }

    fun editNote(note : Note)
    {
        return repo.editNote(note)
    }
 }