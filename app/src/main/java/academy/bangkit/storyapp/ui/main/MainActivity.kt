package academy.bangkit.storyapp.ui.main

import academy.bangkit.storyapp.R
import academy.bangkit.storyapp.databinding.ActivityMainBinding
import academy.bangkit.storyapp.ui.auth.AuthenticationActivity
import academy.bangkit.storyapp.ui.main.home.HomeFragment
import academy.bangkit.storyapp.utils.ViewModelFactory
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.commit

class MainActivity : AppCompatActivity(), Toolbar.OnMenuItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupFragment()
    }

    fun getToken(): String? {
        return intent.getStringExtra(EXTRA_TOKEN)
    }

    private fun setupToolbar() {
        binding.toolbarMain.setOnMenuItemClickListener(this)
    }


    private fun setupFragment() {
        val mFragmentManager = supportFragmentManager
        val mHomeFragment = HomeFragment()
        val fragment = mFragmentManager.findFragmentByTag(HomeFragment::class.java.simpleName)

        if (fragment !is HomeFragment) {
            mFragmentManager.commit {
                add(R.id.main_container, mHomeFragment, HomeFragment::class.java.simpleName)
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.btn_logout -> {
                showLogoutDialog()
                true
            }

            R.id.btn_locale -> {
                val localeIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(localeIntent)
                true
            }
            else -> false
        }
    }

    private fun showLogoutDialog() {
        mainViewModel.deleteSession()

        val alertDialogBuilder = AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.logout))
            setMessage(getString(R.string.message_logout))
            setCancelable(false)
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                val authIntent = Intent(this@MainActivity, AuthenticationActivity::class.java)
                startActivity(authIntent)
                finish()
            }
            setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.cancel()
            }
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    companion object {
        const val EXTRA_TOKEN = "academy.bangkit.storyapp.EXTRA_TOKEN"
        const val RESULT_CREATE = 100
        const val EXTRA_ERROR = "academy.bangkit.storyapp.ERROR"
    }
}