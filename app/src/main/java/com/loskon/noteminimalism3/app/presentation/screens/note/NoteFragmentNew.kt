package com.loskon.noteminimalism3.app.presentation.screens.note

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.databinding.FragmentNoteBinding
import com.loskon.noteminimalism3.viewbinding.viewBinding

class NoteFragmentNew : Fragment(R.layout.fragment_note) {

    private val binding by viewBinding(FragmentNoteBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}