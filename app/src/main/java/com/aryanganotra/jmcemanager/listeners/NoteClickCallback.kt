package com.aryanganotra.jmcemanager.listeners

import com.aryanganotra.jmcemanager.model.Note

interface NoteClickCallback {

    fun onNoteClick(note : Note)
}