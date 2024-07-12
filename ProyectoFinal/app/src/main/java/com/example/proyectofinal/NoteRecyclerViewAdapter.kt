package com.example.proyectofinal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.models.Note
import com.example.proyectofinal.databinding.NoteLayoutAdapterBinding


class NoteRecyclerViewAdapter(
    var notes: List<Note>,
    private val clickListener: (Note) -> Unit,
    private val deleteListener: (Note) -> Unit,
    private val infoListener: (Note) -> Unit,
    private val mapListener: (Note) -> Unit
) : RecyclerView.Adapter<NoteRecyclerViewAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(val binding: NoteLayoutAdapterBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = NoteLayoutAdapterBinding.inflate(layoutInflater, parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.binding.note = note
        holder.binding.cardView.setOnClickListener { clickListener(note) }
        holder.binding.btnDelete.setOnClickListener { deleteListener(note) }
        holder.binding.btnInfo.setOnClickListener { infoListener(note) }
        holder.binding.btnMap.setOnClickListener { mapListener(note) }
    }

    override fun getItemCount(): Int = notes.size

    fun updateNotes(newNotes: List<Note>) {
        notes = newNotes
        notifyDataSetChanged()
    }
}
