package academy.bangkit.storyapp.ui.auth.login

import academy.bangkit.storyapp.R
import academy.bangkit.storyapp.data.Result
import academy.bangkit.storyapp.databinding.FragmentLoginBinding
import academy.bangkit.storyapp.ui.auth.register.RegisterFragment
import academy.bangkit.storyapp.ui.main.MainActivity
import academy.bangkit.storyapp.utils.Extension.showMessage
import academy.bangkit.storyapp.utils.ViewModelFactory
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding
    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory.getInstance(
            requireContext()
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginCheck()
        moveToRegister()
        handleLogin()
        playAnimation()
    }

    private fun playAnimation() {
        val translation =
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) View.TRANSLATION_X else View.TRANSLATION_Y
        ObjectAnimator.ofFloat(binding?.imgWelcomeIllustration, translation, -30F, 30F)
            .apply {
                duration = 6000
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }.start()

        ObjectAnimator.ofFloat(binding?.imgWelcomeIllustration, View.ALPHA, 1F, 0.7F).apply {
            duration = 1000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    private fun loginCheck() {
        binding?.root?.visibility = View.GONE
        loginViewModel.getAuthToken().observe(viewLifecycleOwner) { token ->
            if (token != "") {
                moveToMain(token)
            } else {
                binding?.root?.visibility = View.VISIBLE
            }
        }
    }

    private fun handleLogin() {
        binding?.btnLogin?.setOnClickListener {
            val email = binding?.edtLoginEmail?.text.toString()
            val password = binding?.edtLoginPassword?.text.toString()

            if (isFormCorrectly(email, password)) {
                loginViewModel.loginUser(email, password).observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is Result.Loading -> {
                            setupLoading(true)
                        }

                        is Result.Success -> {
                            setupLoading(false)
                            if (!result.data.error) {
                                val token = result.data.loginResult.token
                                loginViewModel.saveAuthToken(token)
                                moveToMain(token)
                            }
                        }

                        is Result.Error -> {
                            setupLoading(false)
                            binding?.root?.let { view -> result.error.showMessage(view) }
                        }
                    }
                }
            } else {
                binding?.root?.let { view -> getString(R.string.fill_form).showMessage(view) }
            }
        }
    }

    private fun isFormCorrectly(email: String, password: String): Boolean {
        return email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                password.isNotEmpty() && password.length >= 6
    }

    private fun moveToRegister() {
        binding?.btnToRegister?.setOnClickListener {
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

    private fun moveToMain(token: String) {
        val mainIntent = Intent(requireContext(), MainActivity::class.java)
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        mainIntent.putExtra(MainActivity.EXTRA_TOKEN, token)
        startActivity(mainIntent)
        activity?.finish()
    }

    private fun setupLoading(isLoading: Boolean) {
        binding?.apply {
            btnLogin.isClickable = !isLoading
            btnToRegister.isClickable = !isLoading
        }
        binding?.progressBarLogin?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}