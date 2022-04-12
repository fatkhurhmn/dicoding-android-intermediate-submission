package academy.bangkit.storyapp.ui.auth

import academy.bangkit.storyapp.R
import academy.bangkit.storyapp.ui.auth.login.LoginFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit

class AuthenticationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        val mFragmentManager = supportFragmentManager
        val mLoginFragment = LoginFragment()
        val fragment = mFragmentManager.findFragmentByTag(LoginFragment::class.java.simpleName)

        if (fragment !is LoginFragment) {
            mFragmentManager.commit {
                add(R.id.auth_container, mLoginFragment, LoginFragment::class.java.simpleName)
            }
        }
    }
}