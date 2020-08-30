package com.aryanganotra.jmcemanager.activities.courses

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aryanganotra.jmcemanager.R
import com.aryanganotra.jmcemanager.activities.MainActivity
import com.aryanganotra.jmcemanager.activities.notes.AddNoteActivity
import com.aryanganotra.jmcemanager.adapters.CourseListAdapter
import com.aryanganotra.jmcemanager.application.AppApplication.Companion.context
import com.aryanganotra.jmcemanager.listeners.OnCourseClick
import com.aryanganotra.jmcemanager.model.Course
import com.aryanganotra.jmcemanager.model.Note
import com.aryanganotra.jmcemanager.viewmodels.PageViewModel
import kotlinx.android.synthetic.main.activity_course_list.*
import kotlinx.android.synthetic.main.activity_course_list.nodata
import kotlinx.android.synthetic.main.activity_course_list.recycler_view


class CourseListActivity : AppCompatActivity(), OnCourseClick {

    private lateinit var pageViewModel: PageViewModel
    private val ADD_COURSE = 12
    private val ADD_NOTE =11
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_list)
        supportActionBar?.setTitle("Select course")
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel::class.java)
        val adapter  = CourseListAdapter(this)
        recycler_view.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        recycler_view.adapter = adapter
        pageViewModel.getCoursesLiveData().observe(this, Observer {

            if (it!=null)
            {
                progress.visibility = View.GONE

                val courses = it
                if (courses.isNullOrEmpty())
                {
                    nodata.visibility = View.VISIBLE
                    recycler_view.visibility  = View.GONE
                }
                else
                {
                    nodata.visibility = View.GONE
                    recycler_view.visibility  = View.VISIBLE
                }
                adapter.setCourses(courses as ArrayList<Course>)
            }
            else
            {
                nodata.visibility = View.VISIBLE
                recycler_view.visibility  = View.GONE
                adapter.setCourses(ArrayList())
            }


        })
        fab.setOnClickListener {
            val intent = Intent(this@CourseListActivity,
                AddCourseActivity::class.java)
            intent.putExtra("year",0)
            startActivityForResult(intent, ADD_COURSE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_NOTE)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                val bundle = data?.getBundleExtra("note")
                val note = bundle?.getParcelable<Note>("note")
                pageViewModel.addNote(note!!)
                startActivity(Intent(this@CourseListActivity,
                    MainActivity::class.java))
                finish()

            }
        }
        else if (requestCode == ADD_COURSE)
        {
            if (resultCode==Activity.RESULT_OK)
            {
                val bundle = data?.getBundleExtra("course")
                val course = bundle?.getParcelable<Course>("course")
                pageViewModel.addCourse(course!!)
            }
        }
    }

    override fun onCourseClick(course: Course) {
        val i = Intent(this@CourseListActivity, AddNoteActivity::class.java)
        i.putExtra("course_name",course.courseName)
        i.putExtra("sub_id",course.id)
        if (intent.action == Intent.ACTION_SEND)
        {
            if ("application/pdf" == intent.type)
            {
                (intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri)?.let {
                    i.putExtra("uri",it.toString())
                }
            }
        }
        startActivityForResult(i,ADD_NOTE)
    }
}
