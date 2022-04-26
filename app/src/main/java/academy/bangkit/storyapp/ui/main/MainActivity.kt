package academy.bangkit.storyapp.ui.main

import academy.bangkit.storyapp.R
import academy.bangkit.storyapp.databinding.ActivityMainBinding
import academy.bangkit.storyapp.ui.auth.AuthenticationActivity
import academy.bangkit.storyapp.ui.main.home.HomeFragment
import academy.bangkit.storyapp.ui.main.maps.MapsFragment
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

    private fun setupToolbar() {
        binding.toolbarMain.setOnMenuItemClickListener(this)
    }


    private fun setupFragment() {
        val token = intent.getStringExtra(EXTRA_TOKEN)
        val bundle = Bundle()
        bundle.putString(EXTRA_TOKEN, token)

        val mFragmentManager = supportFragmentManager
        val mHomeFragment = HomeFragment()
        mHomeFragment.arguments = bundle

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

            R.id.btn_map -> {
                val mFragment = supportFragmentManager
                val size = mFragment.backStackEntryCount

                if (size > 0) {
                    mFragment.popBackStack()
                    item.setIcon(R.drawable.ic_map)
                } else {
                    val token = intent.getStringExtra(EXTRA_TOKEN)
                    val bundle = Bundle()
                    bundle.putString(EXTRA_TOKEN, token)

                    val mMapFragment = MapsFragment()
                    mMapFragment.arguments = bundle

                    mFragment.commit {
                        addToBackStack(null)
                        replace(
                            R.id.main_container,
                            mMapFragment,
                            MapsFragment::class.java.simpleName
                        )
                    }
                    item.setIcon(R.drawable.ic_list)
                }
                true
            }

            else -> false
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        binding.toolbarMain.menu.getItem(0).setIcon(R.drawable.ic_map)
    }

    private fun showLogoutDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.logout))
            setMessage(getString(R.string.message_logout))
            setCancelable(false)
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                mainViewModel.deleteSession()
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