package academy.bangkit.storyapp.ui.auth.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import academy.bangkit.storyapp.R
import academy.bangkit.storyapp.data.Result
import academy.bangkit.storyapp.databinding.FragmentLoginBinding
import academy.bangkit.storyapp.ui.auth.register.RegisterFragment
import academy.bangkit.storyapp.ui.main.MainActivity
import academy.bangkit.storyapp.utils.Extension.showMessage
import academy.bangkit.storyapp.utils.ViewModelFactory
import android.content.Intent
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        moveToRegister()
        handleLogin()
    }

    private fun handleLogin() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext())
        val loginViewModel: LoginViewModel by viewModels { factory }

        binding.btnLogin.setOnClickListener {
            val email = binding.edtLoginEmail.text.toString()
            val password = binding.edtLoginPassword.text.toString()

            if (isFormCorrectly(email, password)) {
                loginViewModel.loginUser(email, password).observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is Result.Loading -> {
                            binding.btnLogin.isClickable = false
                            binding.progressBarLogin.visibility = View.VISIBLE
                        }

                        is Result.Success -> {
                            binding.btnLogin.isClickable = true
                            binding.progressBarLogin.visibility = View.GONE
                            if (!result.data.error) {
                                loginViewModel.saveAuthToken(result.data.loginResult.token)
                                val mainIntent = Intent(requireContext(), MainActivity::class.java)
                                startActivity(mainIntent)
                            }
                        }

                        is Result.Error -> {
                            binding.btnLogin.isClickable = true
                            binding.progressBarLogin.visibility = View.GONE
                            result.error.showMessage(binding.root)
                        }
                    }
                }
            } else {
                getString(R.string.fill_form).showMessage(binding.root)
            }
        }
    }

    private fun isFormCorrectly(email: String, password: String): Boolean {
        return email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                password.isNotEmpty() && password.length >= 6
    }

    private fun moveToRegister() {
        binding.btnToRegister.setOnClickListener {
            val mRegisterFragment = RegisterFragment()
            val mFragmentManager = parentFragmentManager
            mFragmentManager.commit {
                addToBackStack(null)
                replace(
                    R.id.auth_container,
                    mRegisterFragment,
                    RegisterFragment::class.java.simpleName
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}