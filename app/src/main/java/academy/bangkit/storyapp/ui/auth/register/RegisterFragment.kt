package academy.bangkit.storyapp.ui.auth.register

import academy.bangkit.storyapp.R
import academy.bangkit.storyapp.data.Result
import academy.bangkit.storyapp.databinding.FragmentRegisterBinding
import academy.bangkit.storyapp.utils.Extension.showMessage
import academy.bangkit.storyapp.utils.ViewModelFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        moveToLogin()
        handleRegister()
    }

    private fun handleRegister() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext())
        val registerViewModel: RegisterViewModel by viewModels { factory }

        binding.btnRegister.setOnClickListener {
            val name = binding.edtRegisterName.text.toString()
            val email = binding.edtRegisterEmail.text.toString()
            val password = binding.edtRegisterPassword.text.toString()

            if (isFormCorrectly(name, email, password)) {
                registerViewModel.createAccount(name, email, password)
                    .observe(viewLifecycleOwner) { result ->
                        when (result) {
                            is Result.Loading -> {
                                setupLoading(true)
                            }

                            is Result.Success -> {
                                setupLoading(false)
                                result.data.message.showMessage(binding.root)
                                if (!result.data.error) {
                                    val mFragmentManager = parentFragmentManager
                                    mFragmentManager.popBackStack()
                                }
                            }

                            is Result.Error -> {
                                setupLoading(false)
                                result.error.showMessage(binding.root)
                            }
                        }
                    }
            } else {
                getString(R.string.fill_form).showMessage(binding.root)
            }
        }

    }

    private fun isFormCorrectly(name: String, email: String, password: String): Boolean {
        return name.isNotEmpty() && email.isNotEmpty() &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                password.isNotEmpty() && password.length >= 6
    }

    private fun moveToLogin() {
        binding.btnToLogin.setOnClickListener {
            val mFragmentManager = parentFragmentManager
            mFragmentManager.popBackStack()
        }
    }

    private fun setupLoading(isLoading: Boolean) {
        with(binding) {
            btnRegister.isClickable = !isLoading
            btnToLogin.isClickable = !isLoading
        }
        binding.progressBarRegister.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}