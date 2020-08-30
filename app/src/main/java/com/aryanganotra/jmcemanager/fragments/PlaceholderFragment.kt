package com.aryanganotra.jmcemanager.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aryanganotra.jmcemanager.R
import com.aryanganotra.jmcemanager.activities.notes.NotesListActivity
import com.aryanganotra.jmcemanager.adapters.SubjectListAdapter
import com.aryanganotra.jmcemanager.listeners.DeleteCourseCallback
import com.aryanganotra.jmcemanager.listeners.OnCourseClick
import com.aryanganotra.jmcemanager.model.Course
import com.aryanganotra.jmcemanager.viewmodels.PageViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment(), DeleteCourseCallback, OnCourseClick {



    private val subject = "JMCE-Manager-Support"
    private val emailids = arrayOf("aryanganotra7@gmail.com")
    private lateinit var pageViewModel: PageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)
        val adapter : SubjectListAdapter = SubjectListAdapter(this, this)
        root.recycler_view.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
        root.recycler_view.adapter = adapter

        // val textView: TextView = root.findViewById(R.id.section_label)
//        pageViewModel.text.observe(this, Observer<String> {
//         //   textView.text = it
//            index = it+1
//
//        })
      pageViewModel.getCoursesLiveData().observe(this, Observer {
          root.progress_circular.visibility = View.GONE
          if (it!=null)
          {
              val courses = it.filter { course -> course.year == pageViewModel.getIndex() }
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
        //var index = 1

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }



    override fun onDeleteCourse(course: Course) {
        pageViewModel.deleteCourse(course)
    }

    override fun onCourseClick(course: Course) {
        val i = Intent(context,
            NotesListActivity::class.java)
        i.putExtra("sub_id",course.id)
        i.putExtra("course_name",course.courseName)
        startActivity(i)
    }
//
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//
//        inflater.inflate(R.menu.main_menu, menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId)
//        {
//            R.id.logout -> {
//                FirebaseAuth.getInstance().signOut()
//                startActivity(Intent(context, LoginActivity::class.java))
//                activity?.finish()
//            }
//            R.id.contact -> {
//                composeEmail(emailids, subject)
//            }
//        }
//        return true
//    }
//
//    private fun composeEmail(addresses: Array<String>, subject: String) {
//        val intent = Intent(Intent.ACTION_SENDTO).apply {
//            data = Uri.parse("mailto:") // only email apps should handle this
//            putExtra(Intent.EXTRA_EMAIL, addresses)
//            putExtra(Intent.EXTRA_SUBJECT, subject)
//
//        }
//
//        startActivity(intent)
//
//    }

}