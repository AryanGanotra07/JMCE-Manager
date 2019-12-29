package com.aryanganotra.jmcemanager.listeners

import com.aryanganotra.jmcemanager.model.Course
import com.aryanganotra.jmcemanager.model.Note

interface DeleteCourseCallback {

    fun onDeleteCourse(course : Course)

}