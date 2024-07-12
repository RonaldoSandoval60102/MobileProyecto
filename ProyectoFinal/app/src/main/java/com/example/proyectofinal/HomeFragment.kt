package com.example.proyectofinal

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectofinal.databinding.FragmentHomeBinding
import com.example.proyectofinal.models.Note
import com.example.proyectofinal.noteviewmodel.NoteViewModel
import com.example.proyectofinal.noteviewmodel.NoteViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch


class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: NoteViewModel
    private lateinit var adapter: NoteRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).noteViewModel
        setupRecyclerView()
        addNoteButtons()
        observeNotes()
    }

    private fun addNoteButtons() {
        binding.addNote.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_homeFragment_to_detailFragment)
        }
    }

    private fun setupRecyclerView() {
        adapter = NoteRecyclerViewAdapter(
            emptyList(),
            { note -> onNoteClick(note) },
            { note -> onNoteDelete(note) },
            { note -> onNoteInfo(note) },
            { note -> onNoteMap(note) }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@HomeFragment.adapter
        }
    }

    private fun observeNotes() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userNotes.collect { notes ->
                    adapter.updateNotes(notes)
                    updateUI(notes)
                }
            }
        }
    }

    private fun updateUI(notes: List<Note>) {
        binding.cardView.visibility = if (notes.isEmpty()) View.VISIBLE else View.GONE
        binding.recyclerView.visibility = if (notes.isNotEmpty()) View.VISIBLE else View.GONE
    }

    private fun onNoteClick(note: Note) {
        viewModel.selectedNote(note)
        view?.findNavController()?.navigate(R.id.action_homeFragment_to_detailFragment)
    }

    private fun onNoteDelete(note: Note) {
        viewModel.deleteNoteById(note.id)
    }

    private fun onNoteInfo(note: Note) {
        val message = "ID: ${note.id}\nTitle: ${note.title}\nBody: ${note.body}\nLatitude: ${note.latitude}\nLongitude: ${note.longitude}"
        AlertDialog.Builder(requireContext())
            .setTitle("Note Info")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun onNoteMap(note: Note) {
        if (isInternetAvailable()) {
            openGoogleMaps(note.latitude, note.longitude)
        } else {
            showNoInternetSnackbar()
        }
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
    }

    private fun showNoInternetSnackbar() {
        Snackbar.make(binding.root, "No hay conexión a internet", Snackbar.LENGTH_SHORT).show()
    }

    private fun openGoogleMaps(latitud: Double, longitud: Double) {
        try {
            val geoUri = Uri.parse("geo:$latitud,$longitud?q=$latitud,$longitud(Ubicación)")
            val mapIntent = Intent(Intent.ACTION_VIEW, geoUri)

            if (mapIntent.resolveActivity(requireActivity().packageManager) == null) {
                val googleMapsUrl = "https://www.google.com/maps/search/?api=1&query=$latitud,$longitud"
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(googleMapsUrl))
                startActivity(browserIntent)
            } else {
                startActivity(mapIntent)
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error al abrir Google Maps", Toast.LENGTH_SHORT).show()
        }
    }
}