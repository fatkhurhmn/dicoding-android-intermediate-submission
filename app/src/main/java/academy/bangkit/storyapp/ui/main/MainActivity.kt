package academy.bangkit.storyapp.ui.main

import academy.bangkit.storyapp.R
import academy.bangkit.storyapp.databinding.ActivityMainBinding
import academy.bangkit.storyapp.ui.auth.AuthenticationActivity
import academy.bangkit.storyapp.utils.ViewModelFactory
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity(), Toolbar.OnMenuItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var factory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ViewModelFactory.getInstance(this)

        setupToolbar()
    }

    private fun setupToolbar() {
        binding.toolbarMain.setOnMenuItemClickListener(this)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.btn_logout -> {
                showLogoutDialog()
                true
            }

            else -> false
        }
    }

    private fun showLogoutDialog() {
        val mainViewModel: MainViewModel by viewModels { factory }
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
}