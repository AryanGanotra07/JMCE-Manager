package com.aryanganotra.jmcemanager.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.aryanganotra.jmcemanager.R
import com.aryanganotra.jmcemanager.model.Course
import com.aryanganotra.jmcemanager.ui.main.PageViewModel
import kotlinx.android.synthetic.main.activity_add_course.*



class AddCourseActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)
        getSupportActionBar()?.setTitle("Add Course");



        val list  = ArrayList<String>()
        list.add("First Semester")
        list.add("Second Semester")
        list.add("Third Semester")
        list.add("Fourth Semester")
        list.add("Fifth Semester")
        list.add("Sixth Semester")
        val adapter : ArrayAdapter<String> = ArrayAdapter(this, R.layout.spinner_item,list)
        spinner.adapter = adapter

        val year = intent.getIntExtra("year",0)
        spinner.setSelection(year)

        save_btn.setOnClickListener {
            if (name_et.text!=null || name_et.text.toString().isNotEmpty()) {
                val course = Course()
                course.courseName = name_et.text.toString()
                course.year = spinner.selectedItemPosition + 1
                val bundle = Bundle()
                bundle.putParcelable("course", course)
                val returnIntent = Intent()
                returnIntent.putExtra("course", bundle)
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
            else
            {
                name_et.setError("Name field can't be blank")
            }
        }

        cancel_btn.setOnClickListener {
            val returnIntent = Intent()
            setResult(Activity.RESULT_CANCELED, returnIntent)
            finish()
        }
    }
}
