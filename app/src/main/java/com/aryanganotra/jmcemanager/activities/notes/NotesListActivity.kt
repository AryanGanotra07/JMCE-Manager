package com.aryanganotra.jmcemanager.activities.notes

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aryanganotra.jmcemanager.R
import com.aryanganotra.jmcemanager.adapters.NoteListAdapter
import com.aryanganotra.jmcemanager.listeners.DeleteNoteCallback
import com.aryanganotra.jmcemanager.listeners.NoteClickCallback
import com.aryanganotra.jmcemanager.model.Note
import com.aryanganotra.jmcemanager.viewmodels.PageViewModel
import kotlinx.android.synthetic.main.activity_notes_list.*

class NotesListActivity : AppCompatActivity(), DeleteNoteCallback, NoteClickCallback {

    private lateinit var pageViewModel: PageViewModel
    private val ADD_NOTE =11
    private val EDIT_NOTE =12

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_list)

        val sub_id = intent.getStringExtra("sub_id")
        val course_name = intent.getStringExtra("course_name")
        supportActionBar?.setTitle(course_name)
        val adapter : NoteListAdapter = NoteListAdapter(this, this)
        recycler_view.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recycler_view.adapter = adapter

        pageViewModel = ViewModelProviders.of(this).get(PageViewModel::class.java)

        fab.setOnClickListener {
            val i = Intent(this@NotesListActivity, AddNoteActivity::class.java)
            i.putExtra("course_name",course_name)
            i.putExtra("sub_id",sub_id)
            startActivityForResult(i,ADD_NOTE)
        }

        pageViewModel.getNotesLiveData().observe(this, Observer {

            if (it!=null) {
                val notes = it.filter { note ->
                    note.subId == sub_id
                }
                if (notes.isNullOrEmpty())
                {
                    nodata.visibility = View.VISIBLE
                    recycler_view.visibility = View.GONE
                }
                else
                {
                    nodata.visibility = View.GONE
                    recycler_view.visibility = View.VISIBLE
                }
                adapter.setNotes(notes as ArrayList<Note>)
            }
            else
            {
                nodata.visibility = View.VISIBLE
                recycler_view.visibility = View.GONE
                adapter.setNotes(ArrayList())
            }

        })

    }

    override fun onDeleteNote(note: Note) {
        pageViewModel.deleteNote(note)
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

            }
        }
        else if (requestCode==EDIT_NOTE)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                val bundle = data?.getBundleExtra("note")
                val note = bundle?.getParcelable<Note>("note")
                pageViewModel.editNote(note!!)
            }
        }
    }

    override fun onNoteClick(note: Note) {
            val i  = Intent(this@NotesListActivity,
                EditNoteActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable("note",note)
        i.putExtra("note",bundle)
        startActivityForResult(i,EDIT_NOTE)

    }
}
