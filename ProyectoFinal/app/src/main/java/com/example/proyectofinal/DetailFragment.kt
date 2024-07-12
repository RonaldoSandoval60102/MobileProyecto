package com.example.proyectofinal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.navigation.findNavController
import androidx.lifecycle.lifecycleScope
import com.example.proyectofinal.databinding.FragmentDetailBinding
import com.example.proyectofinal.noteviewmodel.NoteViewModel
import kotlinx.coroutines.launch

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private lateinit var binding: FragmentDetailBinding
    private lateinit var viewModel: NoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).noteViewModel
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.noteDone.setOnClickListener {
            viewModel.saveNote()
            binding.root.findNavController().navigate(R.id.action_detailFragment_to_homeFragment)
        }

        binding.NoteTitleUpdate.addTextChangedListener {
            if (binding.NoteTitleUpdate.text.toString() != viewModel.noteTitleText.value) {
                viewModel.updateNoteTitleText(it.toString())
            }
        }

        binding.NoteBodyUpdate.addTextChangedListener {
            if (binding.NoteBodyUpdate.text.toString() != viewModel.noteBodyText.value) {
                viewModel.updateNoteBodyText(it.toString())
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.noteTitleText.collect { title ->
                updateTextIfDifferent(binding.NoteTitleUpdate, title)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.noteBodyText.collect { body ->
                updateTextIfDifferent(binding.NoteBodyUpdate, body)
            }
        }

    }

    private fun updateTextIfDifferent(editText: EditText, newText: String) {
        if (editText.text.toString() != newText) {
            val cursorPosition = editText.selectionStart
            editText.setText(newText)
            editText.setSelection(cursorPosition.coerceAtMost(newText.length))
        }
    }

}