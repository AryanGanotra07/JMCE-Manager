package com.aryanganotra.jmcemanager.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aryanganotra.jmcemanager.R
import com.aryanganotra.jmcemanager.adapters.NoteListAdapter
import com.aryanganotra.jmcemanager.listeners.DeleteNoteCallback
import com.aryanganotra.jmcemanager.model.Note
import kotlinx.android.synthetic.main.activity_notes_list.*

class NotesListActivity : AppCompatActivity(), DeleteNoteCallback {

    private lateinit var pageViewModel: PageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_list)

        val sub_id = intent.getStringExtra("sub_id")
        val adapter : NoteListAdapter = NoteListAdapter(this)
        recycler_view.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recycler_view.adapter = adapter

        pageViewModel = ViewModelProviders.of(this).get(PageViewModel::class.java)

        fab.setOnClickListener {

        }

        pageViewModel.getNotesLiveData().observe(this, Observer {
            val notes = it.filter {
                note -> note.subId == sub_id
            }
            adapter.setNotes(notes as ArrayList<Note>)

        })

    }

    override fun onDeleteNote(note: Note) {

    }
}
