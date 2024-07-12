package com.example.proyectofinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.proyectofinal.databinding.FragmentLoginBinding
import com.example.proyectofinal.noteviewmodel.NoteViewModel
import com.example.proyectofinal.noteviewmodel.NoteViewModelFactory
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: NoteViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = NoteViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this, factory)[NoteViewModel::class.java]

        setupListeners()
        observeLoginResult()
    }

    private fun setupListeners() {
        binding.email.addTextChangedListener { updateLoginButtonState() }
        binding.password.addTextChangedListener { updateLoginButtonState() }

        binding.loginButton.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            viewModel.login(email, password)
        }
    }

    private fun updateLoginButtonState() {
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()
        binding.loginButton.isEnabled = email.isNotBlank() && password.isNotBlank()
    }

    private fun observeLoginResult() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loginResult.collect { result ->
                result?.let {
                    if (it.isSuccess) {
                        handleSuccessfulLogin(it.getOrThrow())
                    } else {
                        handleFailedLogin(it.exceptionOrNull()!!)
                    }
                }
            }
        }
    }

    private fun handleSuccessfulLogin(data: String) {
        Toast.makeText(context, "Login exitoso", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
    }

    private fun handleFailedLogin(exception: Throwable) {
        Toast.makeText(context, "Login fallido: ${exception.message}", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}