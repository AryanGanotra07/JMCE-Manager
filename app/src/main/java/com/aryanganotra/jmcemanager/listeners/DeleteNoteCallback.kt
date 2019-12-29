package com.aryanganotra.jmcemanager.listeners

import com.aryanganotra.jmcemanager.model.Note

interface DeleteNoteCallback {
    fun onDeleteNote(note : Note)
}