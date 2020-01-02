package com.aryanganotra.jmcemanager.Firebase

import android.widget.Toast
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.aryanganotra.jmcemanager.application.AppApplication
import com.aryanganotra.jmcemanager.model.Course
import com.aryanganotra.jmcemanager.model.Note
import com.aryanganotra.jmcemanager.utils.toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseRepo() {


    private val REF = FirebaseDatabase.getInstance().reference

    private val firebaseDatabaseLiveData = FirebaseDatabaseLiveData(REF.child("courses"))
    private val notesfirebaseDatabaseLiveData = FirebaseDatabaseLiveData(REF.child("notes"))

    private val coursesLiveData : MediatorLiveData<ArrayList<Course>> = MediatorLiveData()
    private val notesLiveData : MediatorLiveData<ArrayList<Note>> = MediatorLiveData()

    init {
        coursesLiveData.addSource(firebaseDatabaseLiveData, Observer {
            if (it!=null && it.hasChildren()) {

                val lecturesList: ArrayList<Course> = ArrayList()
                for (lecture in it.children)
                {
                    val lect : Course = lecture.getValue(Course::class.java)!!
                    lecturesList.add(lect)
                }

                Thread(Runnable { coursesLiveData.postValue(lecturesList) }).start()
            }
            else coursesLiveData.value = null
        })

        notesLiveData.addSource(notesfirebaseDatabaseLiveData, Observer {
            if (it!=null && it.hasChildren()) {

                val lecturesList: ArrayList<Note> = ArrayList()
                for (lecture in it.children)
                {
                    val lect : Note = lecture.getValue(Note::class.java)!!
                    lecturesList.add(lect)
                }

                Thread(Runnable { notesLiveData.postValue(lecturesList) }).start()
            }
            else notesLiveData.value = null
        })

        }

    fun getCoursesLiveData() : MediatorLiveData<ArrayList<Course>>  {
        return coursesLiveData
    }

    fun getNotesLiveData() : MediatorLiveData<ArrayList<Note>>
    {
        return notesLiveData
    }

    fun deleteCourse(course : Course)
    {
        if (REF.child("courses")!=null) {
            REF.child("courses")
                .child(course.id)
                .removeValue()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        AppApplication?.context?.toast("Deleted course")
                        deleteAllNotesWithSubId(course.id)
                    } else AppApplication?.context?.toast("Failed")
                }
        }

    }


    fun addCourse(course : Course)
    {

        val key = REF.child("courses")?.push()?.key
        if (key != null) {
            course.id = key
        }
        val childUpdates = HashMap<String, Any>()
        childUpdates["/courses/$key"] = course.toMap()
        REF?.updateChildren(childUpdates)?.addOnCompleteListener {
            if (it.isSuccessful) {


                AppApplication.context?.toast("Course Added")
            } else AppApplication.context?.toast("Error in adding course")
        }

    }

    private fun deleteAllNotesWithSubId(subId : String)
    {
        if (REF.child("notes")!=null) {
            REF.child("notes").orderByChild("subId")
                .equalTo(subId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        AppApplication?.context?.toast("Some error occurred. Please try again")
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0 != null) {
                            if (p0.hasChildren()) {
                                for (noteSnap in p0.children) {
                                    val note: Note = noteSnap.getValue(Note::class.java)!!
                                    deleteNote(note)

                                }

                            }
                        }
                    }

                })
        }
    }

    fun addNote(note : Note)
    {

            val key = REF.child("notes")?.push()?.key
            if (key != null) {
                note.id = key
            }
            val childUpdates = HashMap<String, Any>()
            childUpdates["/notes/$key"] = note.toMap()
            REF?.updateChildren(childUpdates)?.addOnCompleteListener {
                if (it.isSuccessful) {
                    AppApplication.context?.toast("Note Added")
                } else AppApplication.context?.toast("Error in adding note")
            }

    }

    fun deleteNote(note : Note)
    {
        if (REF.child("notes")!=null) {
            REF.child("notes")
                .child(note.id)
                .removeValue()
                .addOnCompleteListener {
                    if (it.isSuccessful) AppApplication?.context?.toast("Deleted note")
                    else AppApplication?.context?.toast("Failed")
                }
        }

    }

    fun editNote(note: Note) {
        if (REF.child("notes") != null) {
            REF.child("notes")
                .child(note.id)
                .setValue(note)
                .addOnCompleteListener {
                    if (it.isSuccessful) AppApplication?.context?.toast("Note Updated")
                    else AppApplication?.context?.toast("Failed")
                }
        }
    }

    }