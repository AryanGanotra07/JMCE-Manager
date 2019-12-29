package com.aryanganotra.jmcemanager.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aryanganotra.jmcemanager.R
import com.aryanganotra.jmcemanager.listeners.DeleteCourseCallback
import com.aryanganotra.jmcemanager.model.Course
import com.aryanganotra.jmcemanager.model.Tab
import com.aryanganotra.jmcemanager.ui.main.PageViewModel
import kotlinx.android.synthetic.main.subject_item.view.*

class SubjectListAdapter(val deleteCourseCallback : DeleteCourseCallback) : RecyclerView.Adapter<SubjectListAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subject_name = itemView.findViewById<TextView>(R.id.subject_name)
        val delete_btn = itemView.findViewById<ImageView>(R.id.delete_btn)


    }

    private lateinit var courses : ArrayList<Course>
    //private lateinit var callback : NotesFragment.CourseClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.subject_item, parent, false)

        val viewHolder = ViewHolder(v)

        return viewHolder
    }

//    fun setCallback (callback : NotesFragment.CourseClickListener){
//        this.callback = callback
//
//    }

    override fun getItemCount(): Int {
       if (::courses.isInitialized){
           return courses.size
       }
        else{
           return 0
       }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {



        holder.subject_name.setText(courses.get(position).courseName)
        holder.itemView.delete_btn.setOnClickListener {
            deleteCourseCallback.onDeleteCourse(courses.get(position))
        }

    }

    fun setCourses(courses : ArrayList<Course>){
        this.courses = courses
        notifyDataSetChanged()
    }




}