package academy.bangkit.storyapp.ui.auth.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import academy.bangkit.storyapp.R
import academy.bangkit.storyapp.databinding.FragmentLoginBinding
import academy.bangkit.storyapp.ui.auth.register.RegisterFragment
import androidx.fragment.app.commit

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        moveToRegister()
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}