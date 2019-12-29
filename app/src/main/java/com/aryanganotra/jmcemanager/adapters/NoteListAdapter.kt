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
import com.aryanganotra.jmcemanager.listeners.DeleteNoteCallback
import com.aryanganotra.jmcemanager.model.Course
import com.aryanganotra.jmcemanager.model.Note
import com.aryanganotra.jmcemanager.model.Tab
import com.aryanganotra.jmcemanager.ui.main.PageViewModel
import kotlinx.android.synthetic.main.subject_item.view.*

class NoteListAdapter(val deleteNoteCallback : DeleteNoteCallback) : RecyclerView.Adapter<NoteListAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val note_name = itemView.findViewById<TextView>(R.id.subject_name)
        val delete_btn = itemView.findViewById<ImageView>(R.id.delete_btn)


    }

    private lateinit var notes : ArrayList<Note>
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
       if (::notes.isInitialized){
           return notes.size
       }
        else{
           return 0
       }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {



        holder.note_name.setText(notes.get(position).noteName)
        holder.itemView.delete_btn.setOnClickListener {
            deleteNoteCallback.onDeleteNote(notes.get(position))
        }

    }

    fun setNotes(notes : ArrayList<Note>){
        this.notes = notes
        notifyDataSetChanged()
    }




}